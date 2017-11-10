package com.coma.go.Custom;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coma.go.Model.Message;
import com.coma.go.R;

import java.util.List;

/**
 * Created by Koma on 29.09.2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    private Activity activity;
    public List<Message> questList;


    public MessageAdapter(Activity context, List<Message> questList){
        super(context, R.layout.row_message, questList);
        this.activity = context;
        this.questList = questList;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View row = inflater.inflate(R.layout.row_message, null, true);

        TextView textViewName = (TextView) row.findViewById(R.id.textView_sender);
        TextView textViewDesc = (TextView) row.findViewById(R.id.textView_message);
        TextView textViewTime = (TextView) row.findViewById(R.id.textView_time);

        textViewName.setText(questList.get(position).getSender());
        textViewDesc.setText(questList.get(position).getMessage());
        textViewTime.setText(questList.get(position).getTime());
        return row;
    }




}