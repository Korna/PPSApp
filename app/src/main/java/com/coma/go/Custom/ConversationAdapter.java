package com.coma.go.Custom;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.coma.go.Model.Conversation;
import com.coma.go.R;
import com.coma.go.Service.FBIO;
import com.coma.go.View.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Created by Koma on 29.09.2017.
 */

public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private Activity activity;
    public List<Conversation> conversationList;


    public ConversationAdapter(Activity context, List<Conversation> questList){
        super(context, R.layout.row_conversation, questList);
        this.activity = context;
        this.conversationList = questList;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.row_conversation, null, true);


        TextView textViewName = (TextView) row.findViewById(R.id.textView_conversation_name);
        textViewName.setText(conversationList.get(position).getName());

        Button buttonChat = (Button) row.findViewById(R.id.button_chat);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("Conversation", conversationList.get(position));
                getContext().startActivity(intent);

            }
        });



        return row;
    }


    public void notifyData(){
        notifyDataSetChanged();
    }


}