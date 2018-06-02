package com.coma.go.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.coma.go.Entity.Dialog;
import com.coma.go.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    Dialog dialog;
    String cid;
    @BindView(R.id.listView_message_history)
    ListView listView;
    @BindView(R.id.editText_message)
    EditText editTextMessage;
    @BindView(R.id.button_send)
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


//
      //  try {
      //      dialog = (Dialog) getIntent().getSerializableExtra("Dialog");
      //      cid  = dialog.getCid();
      //  }catch(NullPointerException npe){
      //      Log.e("npe", npe.toString());
      //      dialog = new Dialog();
      //      cid = "SampleConv";
      //  }
//
      //  MessageAdapter chatAdapter = new MessageAdapter(this, dialog.getMessageHistory());
      //  listView.setAdapter(chatAdapter);
//
//
      //  buttonSend.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          String msgText = editTextMessage.getText().toString();
      //          editTextMessage.setText("");
//
      //          Message message = new Message();
      //          message.setMessage(msgText);
      //          message.setReaded(false);
//
      //          Singleton singleton = Singleton.getInstance();
//
      //          String uid = singleton.getUser().getid();
      //          message.setSender(uid);
//
      //          String s  = "date.2017";
      //          message.setTime(s);
//
      //          dialog.getMessageHistory().add(message);//может быть по времени рассинхрон.
//
      //          DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);
//
      //          ref.child(cid).setValue(dialog);
//
//
//
      //      }
      //  });
    }


}
