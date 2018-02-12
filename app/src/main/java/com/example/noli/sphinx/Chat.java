package com.example.noli.sphinx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat extends AppCompatActivity {

    private String username;
    private String dosti;
    private String room_name;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ImageButton sendBTN;
    private TextView textView;
    private TextView textView2;
    private EditText msgTF;
    private DatabaseReference dhoma;
    private String temp_key;
    private String msg;
    private String sender;
    private boolean dhoma_created = false;
    private int lineCount = 0;
    private ScrollView scroll;
    private ArrayList<Message> messages;
    private MessageMeAdapter adapter;
    private ListView listView;
    private ImageView notti_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        notti_img = (ImageView) findViewById(R.id.imageView12);
        messages = new ArrayList<>();
        username = getIntent().getExtras().getString("username");
        adapter = new MessageMeAdapter(this, messages, username);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        sendBTN = (ImageButton) findViewById(R.id.sendBTN);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        msgTF = (EditText) findViewById(R.id.msgTF);
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        dosti = getIntent().getExtras().getString("dosti");
        room_name = getIntent().getExtras().getString("room_name");
        dhoma = FirebaseDatabase.getInstance().getReference().child("chat").child(room_name);
        scroll = (ScrollView) findViewById(R.id.scroll);
        myRef.child("msg_noti").child(username).child(dosti).setValue("0");


        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!msgTF.getText().toString().trim().isEmpty()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = dhoma.push().getKey();
                    dhoma.updateChildren(map);


                    DatabaseReference msg = dhoma.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("sender",username);
                    map2.put("msg",msgTF.getText().toString());
                    msg.updateChildren(map2);
                    msgTF.setText("");

                    myRef.child("chat").child(room_name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myRef.child("msg_noti").child(dosti).child(username).setValue("1");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    msgTF.setText("");
                }
            }
        });

        dhoma.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateTextView(dataSnapshot);
                //Toast.makeText(getApplicationContext(),"OLEEEEEEEEE!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateTextView(dataSnapshot);
                //Toast.makeText(getApplicationContext(),"OLEEEEEEEEE!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateTextView(DataSnapshot dataSnapshot) {
        Iterator it = dataSnapshot.getChildren().iterator();
        while(it.hasNext()) {
            msg = (String) ((DataSnapshot) it.next()).getValue();
            sender = (String) ((DataSnapshot) it.next()).getValue();
            messages.add(new Message(msg, sender));
            adapter.notifyDataSetChanged();
            listView.setSelection(listView.getCount() - 1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myRef.child("msg_noti").child(username).child(dosti).setValue("0");
        Intent select = new Intent(this, SelectChatFriend.class);
        startActivity(select);
        finish();

    }
}
