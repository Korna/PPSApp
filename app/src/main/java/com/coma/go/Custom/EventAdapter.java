package com.coma.go.Custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coma.go.View.EventActivity;
import com.coma.go.View.MainActivity;
import com.coma.go.Model.Event;
import com.coma.go.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Koma on 23.09.2017.
 */


public class EventAdapter extends ArrayAdapter<Event> {

    private Activity activity;
    public List<Event> questList;

    public EventAdapter(Activity context, List<Event> questList){
        super(context, R.layout.row_event, questList);
        this.activity = context;
        this.questList = questList;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View row = inflater.inflate(R.layout.row_event, null, true);

        ImageView imageViewPhoto = (ImageView) row.findViewById(R.id.imageView);
       //Drawable d = getPhotoById(questList.get(position).getPhoto_id());
        //imageViewPhoto.setImageDrawable(drawable);

        TextView textViewName = (TextView) row.findViewById(R.id.textView_event_name);
        TextView textViewDesc = (TextView) row.findViewById(R.id.textView_description);
        Button buttonGetQuest = (Button) row.findViewById(R.id.button_info);

        textViewName.setText(questList.get(position).getName());
        textViewDesc.setText(questList.get(position).getDescription());

        buttonGetQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = questList.get(position);

                Intent intent = new Intent(getContext(), EventActivity.class);
                intent.putExtra("clickedEvent", event);
                activity.startActivity(intent);


            }
        });



        return row;
    }




}