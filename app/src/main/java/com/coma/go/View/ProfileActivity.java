package com.coma.go.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coma.go.Model.Event;
import com.coma.go.Model.UserInfo;
import com.coma.go.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @Bind(R.id.textView_name)
    TextView textViewName;
    @Bind(R.id.textView_description)
    TextView textViewDescription;
    @Bind(R.id.textView_city)
    TextView textViewCity;
    @Bind(R.id.button_write)
    Button buttonWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        final UserInfo userInfo = (UserInfo)getIntent().getSerializableExtra("clickedEvent");




        textViewName.setText(userInfo.getNickname());
        textViewDescription.setText(userInfo.getDescription());
        try {
            textViewCity.setText(userInfo.getCity());
        }catch(NullPointerException npe){
            Log.e("npe", npe.toString());
            textViewCity.setText("Not Found");
        }

        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                intent.putExtra("toChat", userInfo.getUid());
                startActivity(intent);
            }
        });
    }
}
