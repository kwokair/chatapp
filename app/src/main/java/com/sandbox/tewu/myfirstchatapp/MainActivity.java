package com.sandbox.tewu.myfirstchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterChatroom();
            }
        });
    }

    private void enterChatroom() {
        EditText txtNickname = (EditText) findViewById(R.id.txtNickname);
        EditText txtChatroom = (EditText) findViewById(R.id.txtChatroom);

        String strNickname = txtNickname.getText().toString();
        String strChatroom = txtChatroom.getText().toString();

        if (!strNickname.equals("") && !strChatroom.equals("")) {
            Toast.makeText(this, strNickname + " - " + strChatroom, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ActivityChatRoom.class);
            intent.putExtra("nickname", strNickname);
            intent.putExtra("chatroom", strChatroom);

            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please enter nickname and chatroom!", Toast.LENGTH_LONG).show();
        }

    }
}
