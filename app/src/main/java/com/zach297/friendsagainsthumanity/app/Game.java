package com.zach297.friendsagainsthumanity.app;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.io.Serializable;

public class Game {
    public static final String BACKEND_URL = "http://zach297.com:8080";
    public static final String BACKEND_URL_WS = "ws://zach297.com:8080";

    public interface GameListener {
        public void onChange(Game game);
    }

    private class Join {
        public int GameId;
        public int PlayerId;
        public int AuthToken;
    }

    private class GameMessage {
        public int GameId;
        public int RoundNumber;
        public int CurrentBlackCard;
        public int CurrentJudge;
        public int PlayerCount;
        public int PreviousWinningCard;
        public String[] Players;
        public int[] Scores;
        public int[] Hand;
        public boolean End;
    }

    private GameListener listener;
    private GameDescription gameDesc;
    private int playerId;
    private int playerAuthId;

    private int[] hand;
    private int[] playerScores;
    private String[] playerNames;
    private int currentBlackCardId;
    private int judgePlayerId;
    private int roundNumber;
    private boolean ended = false;

    public Game(GameDescription gameDesc, int playerId, int playerAuthId)
    {
        this.gameDesc = gameDesc;
        this.playerId = playerId;
        this.playerAuthId = playerAuthId;
    }

    public GameDescription getGameDescription() {
        return gameDesc;
    }

    public int[] getHand() {
        return hand;
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public void join() {
        AsyncHttpClient.getDefaultInstance().websocket(BACKEND_URL + "/GameState", "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                Gson gson = new Gson();
                Join j = new Join();
                j.GameId = gameDesc.GameId;
                j.PlayerId = playerId;
                j.AuthToken = playerAuthId;
                webSocket.send(gson.toJson(j));
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String s) {
                        Log.d("cards", s);
                        Gson gson = new Gson();
                        GameMessage msg = gson.fromJson(s, GameMessage.class);
                        hand = msg.Hand;
                        playerScores = msg.Scores;
                        playerNames = msg.Players;
                        currentBlackCardId = msg.CurrentBlackCard;
                        judgePlayerId = msg.CurrentJudge;
                        roundNumber = msg.RoundNumber;
                        ended = msg.End;
                        onMessage();
                    }
                });
                webSocket.setEndCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                        Log.d("cards", "websocket closed");
                    }
                });
            }
        });
    }

    private void onMessage() {
        if (listener != null) {
            listener.onChange(this);
        }
    }

    public void leave()
    {

    }

    public void submitCards(int[] cardIds)
    {

    }

    public void judgeCard(int card)
    {

    }
}
