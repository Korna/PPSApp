package com.coma.go.Custom.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.coma.go.Entity.Event;


/**
 * Created by Koma on 23.09.2017.
 */


public class OldEventAdapter extends ArrayAdapter<Event> {


    public OldEventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}