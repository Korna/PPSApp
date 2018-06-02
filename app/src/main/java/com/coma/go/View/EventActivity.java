package com.coma.go.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coma.go.Entity.Event;
import com.coma.go.Misc.App;
import com.coma.go.R;
import com.coma.go.Utils.Logger;
import com.coma.go.Utils.ViewUtils;
import com.coma.go.View.Fragments.ImageDialog;
import com.coma.go.View.Fragments.MapDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

import static com.coma.go.View.UserProfileActivity.PROFILE;

public class EventActivity extends AppCompatActivity {

   @BindView(R.id.button_profile) Button buttonProfile;
   @BindView(R.id.button_join) Button buttonJoin;
   @BindView(R.id.textView_conversation_name) TextView textView_name;
   @BindView(R.id.textView_event_description) TextView textView_description;
   @BindView(R.id.textView_category) TextView textView_category;


   @BindView(R.id.imageView_map)
   ImageView imageView_map;
    @BindView(R.id.imageView_image)
    ImageView imageView_image;

    public static String EVENT = "Event";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        final Event event = (Event)getIntent().getSerializableExtra(EVENT);


        imageView_map.setOnClickListener(view -> {
            MapDialog dialog = new MapDialog();
            Bundle bundle = new Bundle();

            bundle.putDouble("lat", event.getLatitude());
            bundle.putDouble("lon", event.getLongitude());
            dialog.setArguments(bundle);


            dialog.show(getSupportFragmentManager().beginTransaction(), "DialogFragment");
        });

        imageView_image.setOnClickListener(view -> {
            ImageDialog dialog = new ImageDialog();
            Bundle bundle = new Bundle();
            bundle.putString("image", event.getImage());
            dialog.setArguments(bundle);

            dialog.show(getSupportFragmentManager().beginTransaction(), "DialogFragment");
        });

        buttonJoin.setOnClickListener(view -> {
            clickJoin(event.get_id());
        });
        buttonProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra(PROFILE, event.getAuthorId());
            startActivity(intent);
        });

        loadInfoToUi(event);

    }

    @SuppressLint("CheckResult")
    private void clickJoin(String id) {
        Log.d("click", "ID:" + id);
        if(id != null)
            App.getApp().getComponent().userApi()
                    .joinEvent(id)
                    .subscribeOn(App.getApp().getNetworkScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(voidResponse -> {
                        if (voidResponse.isSuccessful())
                            ViewUtils.showToast(EventActivity.this, "You are attending!");
                    }, throwable -> Logger.e("join", throwable));
    }


    private void loadInfoToUi(Event event){
        textView_name.setText(event.getName());
        textView_description.setText(event.getText());
        textView_category.setText(event.getCategory());
    }


}
