package com.zach297.friendsagainsthumanity.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by root on 4/19/14.
 */
public class GameFragment extends Fragment implements Game.GameListener {
    private Game game;
    private ArrayList<Integer> hand = new ArrayList<Integer>();
    private TextView titleView;
    private HandFragment handFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        titleView = (TextView)view.findViewById(R.id.title);
        titleView.setText(game.getGameDescription().Name);
        handFragment = (HandFragment)getFragmentManager().findFragmentById(R.id.hand);
        handFragment.setHand(hand);
    }

    @Override
    public void onChange(Game game) {
        hand.clear();
        int[] handArray = game.getHand();
        for (int card : handArray) {
            hand.add(card);
        }
        if (handFragment != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handFragment.notifyCardsChanged();
                }
            });
        }
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.setListener(this);
        this.game.join();
    }
}
