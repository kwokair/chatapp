package com.sandbox.tewu.myfirstchatapp;

import android.app.ListActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by te.wu on 22/12/2016.
 */

public class ActivityChatRoom extends ListActivity {

    private static final String FIREBASE_URL = "https://*****.firebaseio.com/";
    private Firebase mFirebaseRef;
    private String strNickname;
    private String strChatroom;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            Firebase.setAndroidContext(this);
            strNickname = bundle.getString("nickname");
            strChatroom = bundle.getString("chatroom");

            TextView txtRoom = (TextView) findViewById(R.id.txtRoom);
            txtRoom.setText(strChatroom);
            TextView txtNick = (TextView) findViewById(R.id.txtNick);
            txtNick.setText(strNickname);

            // Setup our Firebase mFirebaseRef
            mFirebaseRef = new Firebase(FIREBASE_URL).child(strChatroom);

            EditText txtSendMessage = (EditText) findViewById(R.id.txtSendMessage);
            txtSendMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        sendMessage();
                    }
                    return true;
                }
            });

            findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMessage();
                }
            });

        }
        else
        {
            Toast.makeText(this, "No nickname or chatroom", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.txtSendMessage);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            ChatLine chat = new ChatLine(input, strNickname);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limitToLast(50), this, R.layout.list_chatmessage, strNickname);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(ActivityChatRoom.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityChatRoom.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }
}
