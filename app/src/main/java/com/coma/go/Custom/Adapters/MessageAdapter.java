package com.coma.go.Custom.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coma.go.Custom.Holders.DialogHolder;
import com.coma.go.Custom.Holders.MessageHolder;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Message;

import java.util.List;


/**
 * Created by Koma on 19.03.2018.
 */

public class MessageAdapter extends RecyclerAdapter<Message, MessageHolder> {

    public MessageAdapter(List<Message> ticketList, int res, OnRecyclerViewItemClickListener<Message> listener){
        super(ticketList, res, listener);

    }


    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        MessageHolder h = new MessageHolder(v);
        h.itemView.setOnClickListener(this);

        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder h, int position) {
        h.itemView.setTag(get(position));
      //  h.getTextView_tag().setTag(get(position));
        h.bind(get(position));

    }


}
