package com.coma.go.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.Entity.Profile;
import com.coma.go.Misc.App;
import com.coma.go.R;
import com.coma.go.Utils.ViewUtils;
import com.coma.go.View.Fragments.ImageDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.coma.go.View.ChatActivity.DIALOG_ID;

public class UserProfileActivity extends AppCompatActivity {
    public static String PROFILE = "Profile";
    String TAG = "UserProfileActivity";



    @BindView(R.id.textView_description) TextView editText_description;

    @BindView(R.id.textView_name) TextView editText_username;

    @BindView(R.id.textView_city) TextView editText_city;

    @BindView(R.id.button_write)
    Button button_write;

    @BindView(R.id.imageView_avatar)
    ImageView imageView_avatar;

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        profile = (Profile)getIntent().getSerializableExtra(PROFILE);

        loadInfoToUi(profile);

        button_write.setOnClickListener(view -> {
            toDialog(profile.get_id());
        });
        imageView_avatar.setOnClickListener(view -> {
            ImageDialog dialog = new ImageDialog();
            Bundle bundle = new Bundle();
            bundle.putString("image", profile.getImage());
            dialog.setArguments(bundle);

            dialog.show(getSupportFragmentManager().beginTransaction(), "DialogFragment");
        });
    }

    private void loadInfoToUi(Profile profile){
        editText_city.setText(profile.getCity());
        editText_description.setText(profile.getDescription());
        editText_username.setText(profile.getUsername());
        Glide.with(this)
                .asBitmap()
                .load(profile.getImage())
                .apply(ViewUtils.getAvatarOptions())
                .transition(withCrossFade())
                .into(imageView_avatar);
    }

    @SuppressLint("CheckResult")
    private void toDialog(String companionId){
        App.getApp().getComponent().userApi().createDialog(companionId)
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);
    }
    private void subscribe(Response<String> profileResponse) {
        if(profileResponse.isSuccessful()){
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(DIALOG_ID, profileResponse.body());
            intent.putExtra(PROFILE, profile);
            startActivity(intent);
        }
        else
            ViewUtils.showToast(this, "Cant create dialog");
    }
    private void error(Throwable throwable) {
        ViewUtils.showToast(this, throwable.getMessage());
        Log.d(TAG, throwable.getMessage());
    }
}

