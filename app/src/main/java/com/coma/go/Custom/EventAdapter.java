package com.coma.go.Custom;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coma.go.Service.FBIO;
import com.coma.go.Service.Singleton;
import com.coma.go.View.EventActivity;
import com.coma.go.Model.Event;
import com.coma.go.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_EVENTS;
import static com.coma.go.Misc.Constants.FB_DIRECTORY_USERS;


/**
 * Created by Koma on 23.09.2017.
 */


public class EventAdapter extends ArrayAdapter<Event> {

    Singleton singleton = Singleton.getInstance();
    private Activity activity;
    public List<Event> eventList;
    private String category;

    public EventAdapter(Activity context, List<Event> eventList, String category){
        super(context, R.layout.row_event, eventList);
        this.eventList = eventList;
        this.activity = context;
        this.category = category;

        if(category.equals("My")){
            String uid = singleton.user.userInfo.getUid();
            getMyEvents(uid);
        } else{
            getEvents();
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {// TODO здесь может быть баг, если удалили мероприятие во время просмотра
        LayoutInflater inflater = activity.getLayoutInflater();

        View row = inflater.inflate(R.layout.row_event, null, true);

        ImageView imageViewPhoto = (ImageView) row.findViewById(R.id.imageView);
        TextView textViewName = (TextView) row.findViewById(R.id.textView_conversation_name);
        TextView textViewDesc = (TextView) row.findViewById(R.id.textView_description);
        Button buttonGetQuest = (Button) row.findViewById(R.id.button_info);

        final Event event = eventList.get(position);

        try {
            if (singleton.user != null)
                if (event.getAuthor_id().equals(singleton.user.userInfo.getUid())) {
                    ImageView imageViewDelete = (ImageView) row.findViewById(R.id.imageView_delete);
                    imageViewDelete.setVisibility(View.VISIBLE);
                    imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            singleton.user.participation.remove(event);
                            FBIO.deleteEvent(null, event.getCategory(), eventList.get(position).getId());
                            eventList.remove(position);
                            refreshAdapter();
                        }
                    });
                }

            textViewName.setText(event.getName());
            textViewDesc.setText(event.getDescription());

        } catch(NullPointerException npe){
                Log.e("getView", npe.toString());

            textViewName.setText(null);
            textViewDesc.setText(null);
            }


        buttonGetQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event != null) {
                    Intent intent = new Intent(getContext(), EventActivity.class);
                    intent.putExtra("clickedEvent", event);
                    activity.startActivity(intent);
                }
            }
        });







        return row;
    }


    public boolean getEvents(){

        DatabaseReference ref = null;
        try {
            ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_EVENTS).child(this.category);
        }catch (NullPointerException npe){
            Log.e("npe", npe.toString());
            return false;
        }

        if(ref != null)
            ref.addValueEventListener(//глобальный и постоянный прослушиватель всех данных marks
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            eventList.clear();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Event object = snapshot.getValue(Event.class);
                                eventList.add(object);
                                Log.v("received&added", object.toString());
                                refreshAdapter();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });


        return true;
    }
    public void refreshAdapter(){
        this.notifyDataSetChanged();
    }


    public boolean getMyEvents(String uid){

        DatabaseReference ref = null;
        try {
            ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS).child(uid).child("participation");
        }catch (NullPointerException npe){
            Log.e("npe", npe.toString());
            return false;
        }


        if(ref != null)
            ref.addValueEventListener(//глобальный и постоянный прослушиватель всех данных marks
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            eventList.clear();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Event object = snapshot.getValue(Event.class);
                                eventList.add(object);
                                Log.v("received&added", object.toString());
                                refreshAdapter();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

        return true;

    }


}