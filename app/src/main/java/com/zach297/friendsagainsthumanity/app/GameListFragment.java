package com.zach297.friendsagainsthumanity.app;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class GameListFragment extends ListFragment implements GameBrowserDialog.GameBrowserListener {
    private ArrayList<Game> games = new ArrayList<Game>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new GameAdapter(getActivity(), games));
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText("You Have No Game");
    }

    @Override
    public void onResume() {
        super.onResume();
        setListShown(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.game_list_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_game_browser:
                openGameBrowser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openGameBrowser() {
        Ion.with(getActivity())
                .load("http://zach297.com:8080/AvailableGames")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        Log.d("cards", result.toString());
                        Gson gson = new Gson();
                        GameDescription[] descs = gson.fromJson(result.get("Games"), GameDescription[].class);
                        GameBrowserDialog d = new GameBrowserDialog();
                        d.setTargetFragment(GameListFragment.this, 0);
                        d.setGameDescriptions(descs);
                        d.show(getFragmentManager(), "game_browser");
                    }
                });
    }

    public void onChooseGame(GameDescription desc) {
        PlayerFragment playerFragment = (PlayerFragment)getFragmentManager().findFragmentByTag("player");
        Game newGame = new Game(desc, playerFragment.getPlayerId(), playerFragment.getPlayerAuthId());
        games.add(newGame);
        getFragmentManager().findFragmentByTag("game_list");
        notifyGamesChanged();

        GameFragment gameFragment = new GameFragment();
        gameFragment.setGame(newGame);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, gameFragment, "game")
                .addToBackStack(null)
                .commit();
    }

    public void notifyGamesChanged() {
        GameAdapter gameAdapter = (GameAdapter)getListAdapter();
        gameAdapter.notifyDataSetChanged();
    }

    static class ViewHolder {
        private TextView name;
    }

    private class GameAdapter extends ArrayAdapter<Game> {
        ArrayList<Game> games;
        public GameAdapter(Context context, ArrayList<Game> games) {
            super(context, R.layout.game_list_item, games);
            this.games = games;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.game_list_item, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView)v.findViewById(R.id.title);
                v.setTag(viewHolder);
            }
            GameDescription desc = games.get(position).getGameDescription();
            if (desc != null) {
                ViewHolder viewHolder = (ViewHolder)v.getTag();
                viewHolder.name.setText(desc.Name);
            }
            return v;
        }
    }


}
