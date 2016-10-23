package com.github.shareme.greenandroid.stateprogressbarapp.not_stateprogressbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.shareme.greenandroid.stateprogressbarapp.R;
import com.github.shareme.greenandroid.stateprogressbarapp.not_stateprogressbar.utils.Utils;

import java.util.List;

/**
 * Created by Kofi Gyan on 7/12/2016.
 */

public class StatesListAdapter extends RecyclerView.Adapter<StatesListAdapter.ItemViewHolder> {

    List<String> items;
    Context context;

    public StatesListAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        public ItemClickListener listener;

        ItemViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getLayoutPosition());
        }

        public static interface ItemClickListener {
            public void onItemClick(View v, int position);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_states_list, viewGroup, false);
        ItemViewHolder ivh = new ItemViewHolder(v, new ItemViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, Utils.selectActivity(position , Utils.basicActivities));
                context.startActivity(intent);

            }
        });
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.title.setText(items.get(i));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}