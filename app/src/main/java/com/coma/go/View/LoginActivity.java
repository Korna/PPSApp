package com.coma.go.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.coma.go.View.User.NewLoginActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_login);
        //ButterKnife.bind(this);

        Intent intent = new Intent(getApplicationContext(), NewLoginActivity.class);
        startActivity(intent);
        finish();
    }










}

