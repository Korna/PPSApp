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

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_USERS;

public class NewLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser firebaseUser = null;

    @Bind(R.id.button_register_user)
    Button buttonRegister;
    @Bind(R.id.button_signup)
    Button buttonSignUp;
    @Bind(R.id.editText_email)
    EditText textEmail;
    @Bind(R.id.editText_pass)
    EditText textPass;

    @Bind(R.id.layout_signup)
    RelativeLayout layout_signup;
    @Bind(R.id.layout_loading)
    RelativeLayout layout_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        ButterKnife.bind(this);
        layout_signup.setVisibility(View.INVISIBLE);
        layout_loading.setVisibility(View.VISIBLE);

        setListener();
        buttonRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
              clickRegister();
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
               clickSignUp();
            }
        });
    }

    private void clickSignUp(){
        //показываем слой экрана с уведомлением о загрузке
        layout_signup.setVisibility(View.INVISIBLE);
        layout_loading.setVisibility(View.VISIBLE);
        //получаем почту и пароль
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();
        //вызываем функцию отправки запроса о верности данных
        signUp(email, pass);
        //очищаем поля
        textEmail.setText("");
        textPass.setText("");
    }

    private void clickRegister(){
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();
        Task task = createAccount(email, pass).getTask();
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                AuthResult authResult = (AuthResult) task.getResult();
                String uid = authResult.getUser().getUid();
                User user = new User(new UserInfo(uid, "nickname", "photo", "im user", "spb"));
                FBIO.createUserInfo(uid, user);
            }
        });
    }

    private void setListener(){
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
        //вызываем метод авторизации у класса облачной аутентификации
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//если успешно найден пользователь с таким паролем
                            //получаем аутентификацию
                            AuthResult authResult = task.getResult();
                            //получаем уникальный идентификатор пользователя
                            String uid = authResult.getUser().getUid();
                            //вызываем функцию для перехода в новый экран
                            proceedToMainActivity(uid);
                        }else{
                            Toast.makeText(NewLoginActivity.this, "Wrong login or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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


    private void proceedToMainActivity(String uid){
        //вызываем функцию для получения информации о пользователе
        Task taskUser = getUserTask(uid).getTask();

        taskUser.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){//в случае, если информация была успешно найдена и скачена
                    //получаем пользователя из задачи
                    User user = (User) task.getResult();
                    //заносим пользователя в синглтон
                    Singleton singleton = Singleton.getInstance();
                    singleton.setUser(user);
                    //вывод идентификатора в консоль
                    Log.i("uid", user.userInfo.getUid());

                    Toast.makeText(NewLoginActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                    //создаем новый экран и запускаем его
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    //закрываем текущий экран
                    finish();
                }else{
                    Toast.makeText(NewLoginActivity.this, "Not found info on server", Toast.LENGTH_SHORT).show();
                    //возвращаем видимость формы ввода
                    layout_signup.setVisibility(View.VISIBLE);
                    layout_loading.setVisibility(View.INVISIBLE);
                }

            }
        });
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
}