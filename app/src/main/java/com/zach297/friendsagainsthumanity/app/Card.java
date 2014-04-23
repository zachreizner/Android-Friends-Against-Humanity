package com.zach297.friendsagainsthumanity.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 4/19/14.
 */
public class Card {
    public int CardId;
    public int NumAnswers;
    public String Text;

    public static Map<Integer, Card> CardMap = new HashMap<Integer, Card>();
    public static void LoadCards(JsonArray cards) {
        CardMap.clear();
        for (int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
            JsonObject cardJson = cards.get(cardIndex).getAsJsonObject();
            Card card = new Card();
            card.CardId = cardJson.get("id").getAsInt();
            card.NumAnswers = cardJson.get("numAnswers").getAsInt();
            card.Text = cardJson.get("text").getAsString();
            CardMap.put(card.CardId, card);
        }
    }
}
