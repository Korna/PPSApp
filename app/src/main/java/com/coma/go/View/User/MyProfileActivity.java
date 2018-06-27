package com.coma.go.View.User;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coma.go.Entity.Profile;
import com.coma.go.Misc.App;
import com.coma.go.R;
import com.coma.go.Utils.Logger;
import com.coma.go.Utils.ViewUtils;
import com.coma.go.View.LoadImageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class MyProfileActivity extends LoadImageActivity {

    @BindView(R.id.textView_email) TextView textView_email;

    @BindView(R.id.editText_description) EditText editText_description;

    @BindView(R.id.editText_username) EditText editText_username;

    @BindView(R.id.editText_city) EditText editText_city;

    @BindView(R.id.button_update) Button buttonUpdate;

    @BindView(R.id.imageView_avatar) ImageView imageView_avatar;

    String TAG = "PRofileACtivity";
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);


        App.getApp().getComponent().webApi().getProfile()
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);

        buttonUpdate.setOnClickListener(view -> App.getApp().getComponent().webApi().updateProfile(getProfileFromUi())
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(voidResponse -> {
                    if(voidResponse.isSuccessful())
                        ViewUtils.showToast(MyProfileActivity.this, "Updated");
                    else
                        ViewUtils.showToast(MyProfileActivity.this, "Not updated");
                },
                        throwable -> Logger.d(TAG, "" + throwable.getMessage())));

        imageView_avatar.setOnClickListener(view -> chooseImage());
    }

    private void subscribe(Response<Profile> profileResponse) {
        if(profileResponse.isSuccessful())
            loadProfileToUi(profileResponse.body());
        else
            ViewUtils.showToast(this, "Cant load profile");
    }


    private void loadProfileToUi(Profile profile){
        textView_email.setText(profile.getEmail());
        editText_username.setText(profile.getUsername());
        editText_city.setText(profile.getCity());
        editText_description.setText(profile.getDescription());
        image = profile.getImage();
        loadImage(image);
    }

    private Profile getProfileFromUi(){
        String username = editText_username.getText().toString();
        String city = editText_city.getText().toString();
        String description = editText_description.getText().toString();
        return new Profile(username, city, description, image);
    }


    private void error(Throwable throwable) {
        ViewUtils.showToast(this, throwable.getMessage());
        Log.d(TAG, throwable.getMessage());
    }



    @Override
    protected void loadImage(String path){
        Glide.with(this)
                .asBitmap()
                .load(path)
                .apply(ViewUtils.getAvatarOptions())
                .transition(withCrossFade())
                .into(imageView_avatar);
    }
    @Override
    protected void loadImage(Uri path){
        Glide.with(this)
                .asBitmap()
                .load( path)
                .apply(ViewUtils.getAvatarOptions())
                .transition(withCrossFade())
                .into(imageView_avatar);
    }
}
