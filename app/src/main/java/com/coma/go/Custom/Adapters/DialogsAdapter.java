package com.coma.go.Custom.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.coma.go.Custom.Holders.DialogHolder;
import com.coma.go.Custom.Holders.EventHolder;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.Utils.Logger;

import java.util.List;


/**
 * Created by Koma on 19.03.2018.
 */

public class DialogsAdapter extends RecyclerAdapter<Dialog, DialogHolder> {

    public DialogsAdapter(List<Dialog> ticketList, int res, OnRecyclerViewItemClickListener<Dialog> listener){
        super(ticketList, res, listener);

    }


    @Override
    public DialogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        DialogHolder h = new DialogHolder(v);
        h.itemView.setOnClickListener(this);

        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull DialogHolder h, int position) {
        h.itemView.setTag(get(position));
      //  h.getTextView_tag().setTag(get(position));
        h.bind(get(position));

    }


}
