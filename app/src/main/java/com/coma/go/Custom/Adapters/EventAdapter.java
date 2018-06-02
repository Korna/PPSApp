package com.coma.go.Custom.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coma.go.Custom.Holders.EventHolder;
import com.coma.go.Entity.Event;
import com.coma.go.Utils.Logger;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Koma on 19.03.2018.
 */

public class EventAdapter extends RecyclerAdapter<Event, EventHolder> {

    public EventAdapter(List<Event> ticketList, int res, OnRecyclerViewItemClickListener<Event> listener){
        super(ticketList, res, listener);

    }


    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        EventHolder h = new EventHolder(v);
        h.itemView.setOnClickListener(this);

        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder h, int position) {
        h.itemView.setTag(get(position));
      //  h.getTextView_tag().setTag(get(position));
        h.bind(get(position));

    }
    @Override
    public void onViewRecycled(@NonNull EventHolder holder) {
        super.onViewRecycled(holder);
        try {
            Glide.with(holder.itemView)
                    .clear(holder.imageView_event);
        }catch (IllegalArgumentException iae){
            Logger.e("TopicPageAdapter", iae.getMessage(), iae);
        }
    }



}
