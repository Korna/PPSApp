package com.coma.go.View;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coma.go.Model.User;
import com.coma.go.Model.UserInfo;
import com.coma.go.R;
import com.coma.go.Service.FBIO;
import com.coma.go.Service.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_USERS;

public class NewLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button buttonRegister;
    private Button buttonSignUp;

    private EditText textEmail;
    private EditText textPass;

    Singleton instance = Singleton.getInstance();

    FirebaseUser firebaseUser = null;

    RelativeLayout layout_signup;
    RelativeLayout layout_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        layout_signup = (RelativeLayout) findViewById(R.id.layout_signup);
        layout_loading = (RelativeLayout) findViewById(R.id.layout_loading);



        layout_signup.setVisibility(View.INVISIBLE);
        layout_loading.setVisibility(View.VISIBLE);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d("init", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    proceedToMainActivity(firebaseUser.getUid());



                } else {

                    layout_signup.setVisibility(View.VISIBLE);
                    layout_loading.setVisibility(View.INVISIBLE);

                    Log.d("init", "onAuthStateChanged:signed_out");
                }

            }
        };




        textEmail = (EditText) findViewById(R.id.editText_email);
        textPass = (EditText) findViewById(R.id.editText_pass);

        buttonRegister = (Button) findViewById(R.id.button_register_user);
        buttonRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String email = textEmail.getText().toString();
                String pass = textPass.getText().toString();

                Task task = createAccount(email, pass).getTask();
                task.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        AuthResult authResult = (AuthResult) task.getResult();
                        String uid = authResult.getUser().getUid();
                        User user = new User(new UserInfo(uid, "nickname", "photo", "im fag", "spb"));
                        FBIO.createUserInfo(uid, user);
                    }
                });



               // String FBHandler = new UserDataFBHandler(mAuth.getCurrentUser().getUid());

            }
        });

        buttonSignUp = (Button) findViewById(R.id.button_signup);
        buttonSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String email = textEmail.getText().toString();
                String pass = textPass.getText().toString();
                textEmail.setText("");
                textPass.setText("");
                signUp(email, pass);
            }
        });
    }


    private TaskCompletionSource createAccount(String email, String password){
        final TaskCompletionSource taskCompletionSource = new TaskCompletionSource();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            AuthResult authResult = task.getResult();
                            taskCompletionSource.setResult(authResult);

                            Toast.makeText(NewLoginActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                            //TODO offerToUpdateAccountData();

                        }else{
                            Toast.makeText(NewLoginActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        return taskCompletionSource;
    }



    private void signUp(String email, String password){

        layout_signup.setVisibility(View.INVISIBLE);
        layout_loading.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            AuthResult authResult = task.getResult();
                            String uid = authResult.getUser().getUid();

                            proceedToMainActivity(uid);



                        }else{
                            Toast.makeText(NewLoginActivity.this, "Wrong login or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    private void getCurrentUser(){
        FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

    }



    private TaskCompletionSource getUserTask(String uid){
        final TaskCompletionSource taskCompletionSource = new TaskCompletionSource();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);

        ref.child(uid).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user == null){
                            taskCompletionSource.setException(new FirebaseNetworkException("not found user"));
                            Log.e("not loaded", "user data");
                        }

                        else{
                            taskCompletionSource.setResult(user);
                            Log.d("loaded", "user data");
                        }


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


        return taskCompletionSource;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void proceedToMainActivity(String uid){



        Task taskUser = getUserTask(uid).getTask();

        taskUser.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {



                if(task.isSuccessful()){
                    User user = (User) task.getResult();
                    Singleton singleton = Singleton.getInstance();
                    singleton.setUser(user);
                    Log.i("uid", user.userInfo.getUid());

                    Toast.makeText(NewLoginActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    layout_signup.setVisibility(View.VISIBLE);
                    layout_loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(NewLoginActivity.this, "Not found info on server", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}