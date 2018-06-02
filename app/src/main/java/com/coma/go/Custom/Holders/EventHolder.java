package com.coma.go.Custom.Holders;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.coma.go.BuildConfig;
import com.coma.go.Entity.Event;
import com.coma.go.R;
import com.coma.go.Utils.ViewUtils;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;


/**
 * Created by Koma on 19.03.2018.
 */

public class EventHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.textView_description) TextView textView_description;
    @BindView(R.id.textView_conversation_name) TextView textView_name;
    @BindView(R.id.textView_category) TextView textView_category;

    @BindView(R.id.imageView_delete) ImageView imageView_delete;
    @BindView(R.id.imageView_event) public ImageView imageView_event;

    @BindView(R.id.constraintlayout_event) ConstraintLayout constraintLayout;

    public EventHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }




    public void bind(Event item){
        if(item != null) {
            textView_description.setText(item.getText());
            textView_name.setText(item.getName());
            textView_category.setText(item.getCategory());

            Glide.with(itemView)
                    .asBitmap()
                    .load(item.getImage())
                    .apply(ViewUtils.getImageOptions())
                    .transition(withCrossFade())
                    .into(imageView_event);
        }else
            Glide.with(itemView).clear(imageView_event);

    }



}
