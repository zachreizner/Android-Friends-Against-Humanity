package com.zach297.friendsagainsthumanity.app;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class PlayerFragment extends Fragment implements NameDialog.NameListener {
    private int playerId;
    private int playerAuthId;
    private String playerName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        playerId = prefs.getInt("player_id", 0);
        playerAuthId = prefs.getInt("player_auth_id", 0);
        playerName = prefs.getString("player_name", "");

        if (playerId == 0) {
            Ion.with(getActivity())
                    .load(Game.BACKEND_URL + "/CreatePlayer")
                    .setBodyParameter("Name", "Default Player")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject jsonObject) {
                            if (e != null) {
                                e.printStackTrace();
                                return;
                            }
                            Log.d("cards", jsonObject.toString());
                            playerId = jsonObject.get("PlayerId").getAsInt();
                            playerAuthId = jsonObject.get("PlayerAuthId").getAsInt();
                            playerName = jsonObject.get("PlayerName").getAsString();
                            savePlayerInfo();
                        }
                    });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.player_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_user:
                openChangeName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPlayerAuthId() {
        return playerAuthId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void onChangeName(String name) {
        playerName = name;
        savePlayerInfo();
    }

    public void savePlayerInfo() {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("player_id", playerId)
                .putInt("player_auth_id", playerAuthId)
                .putString("player_name", playerName)
                .commit();
    }

    public void openChangeName() {
        NameDialog d = new NameDialog();
        d.setTargetFragment(this, 0);
        d.setName(playerName);
        d.show(getFragmentManager(), "change_name");
    }
}
