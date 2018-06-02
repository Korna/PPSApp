package com.coma.go.Custom.Holders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;


/**
 * Created by Koma on 19.03.2018.
 */

public class DialogHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.textView_conversation_name) TextView textView_name;
    @BindView(R.id.button_chat)
    Button button;

    public DialogHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }




    public void bind(Dialog item){
        if(item != null) {
            textView_name.setText(item.getName());
        }
    }
}
