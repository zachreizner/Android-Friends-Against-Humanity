package com.zach297.friendsagainsthumanity.app;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HandFragment extends Fragment {
    private GridView gridView;
    private Set<Integer> selectedCards = new HashSet<Integer>();

    public void setHand(ArrayList<Integer> hand) {
        gridView.setAdapter(new CardAdapter(getActivity(), hand));
    }

    public void notifyCardsChanged() {
        CardAdapter cardAdapter = (CardAdapter)gridView.getAdapter();
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gridView = new GridView(getActivity());
        gridView.setColumnWidth(396);
        gridView.setVerticalSpacing(32);
        gridView.setGravity(0x10 | 0x01);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CardAdapter cardAdapter = (CardAdapter) adapterView.getAdapter();
                int cardId = cardAdapter.getCards().get(i);
                if (selectedCards.contains(cardId)) {
                    selectedCards.remove(cardId);
                } else {
                    selectedCards.add(cardId);
                }

                cardAdapter.notifyDataSetChanged();
                Log.d("cards", "card selected " + cardAdapter.getCards().get(i));
            }
        });
        return gridView;
    }

    static class ViewHolder {
        private TextView text;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class CardAdapter extends ArrayAdapter<Integer> {
        ArrayList<Integer> cards;
        public CardAdapter(Context context, ArrayList<Integer> cards) {
            super(context, R.layout.card, cards);
            this.cards = cards;
        }

        ArrayList<Integer> getCards() {
            return cards;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.card, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView)v.findViewById(R.id.card_text);
                v.setTag(viewHolder);
            }
            Integer cardId = cards.get(position);
            if (cardId != null) {
                ViewHolder viewHolder = (ViewHolder)v.getTag();
                viewHolder.text.setText(Card.CardMap.get(cardId).Text);
                if (selectedCards.contains(cardId)) {
                    v.setBackgroundDrawable(getResources().getDrawable(R.drawable.card_border_selected));
                } else {
                    v.setBackgroundDrawable(getResources().getDrawable(R.drawable.card_border));
                }
            }
            return v;
        }
    }
}
