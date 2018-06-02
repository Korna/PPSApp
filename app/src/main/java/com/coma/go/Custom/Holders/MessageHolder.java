package com.coma.go.Custom.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Message;
import com.coma.go.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Koma on 19.03.2018.
 */

public class MessageHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.textView_sender) TextView textView_name;
    @BindView(R.id.textView_message) TextView textView_message;
    @BindView(R.id.textView_time) TextView textView_date;

    public MessageHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }




    public void bind(Message item){
        if(item != null) {
            //textView_name.setText(item.get);
        }
    }
}
