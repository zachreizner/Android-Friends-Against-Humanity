package com.zach297.friendsagainsthumanity.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

public class GameBrowserDialog extends DialogFragment {
    public interface GameBrowserListener {
        public void onChooseGame(GameDescription desc);
    }

    private GameDescription[] games;
    private GameBrowserListener listener;

    public void setGameDescriptions(GameDescription[] games) {
        this.games = games;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> gameNames = new ArrayList<String>();
        for (GameDescription game : games) {
            gameNames.add(game.Name);
        }

        String[] gameNamesArray = gameNames.toArray(new String[gameNames.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.action_game_browser)
                .setItems(gameNamesArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            ((GameBrowserListener)getTargetFragment()).onChooseGame(games[which]);
                        }
                    }
                });
        return builder.create();
    }
}
