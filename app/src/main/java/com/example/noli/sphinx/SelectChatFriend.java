package com.example.noli.sphinx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectChatFriend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ArrayList<String> friends;
    //private ArrayAdapter<String> adapter;
    private OnlineFriendsAdapter adapter;
    private String username;
    private ListView listView;
    private String room_name;
    private DatabaseReference dhoma;
    private ImageView notti_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_select_chat_friend);

        notti_img = (ImageView) findViewById(R.id.imageView12);
        listView = (ListView) findViewById(R.id.listView);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        friends = new ArrayList<>();
        adapter = new OnlineFriendsAdapter(this, friends, username);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(adapter);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){

                    if(d.getKey().trim().equals(mAuth.getCurrentUser().getUid().trim())){
                        User user = d.getValue(User.class);
                        username = user.getUsername().trim();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {

                    if(!d.getKey().trim().equals(username)){
                        friends.add(d.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String dosti = adapter.getItem(position);


                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("chat").hasChild(username+dosti)) {
                            room_name = username+dosti;
                            dhoma = FirebaseDatabase.getInstance().getReference().child("chat").child(room_name);
                            //dhoma_created = true;
                            //Toast.makeText(getApplicationContext(),"EMRI DHOM'S: " + room_name, Toast.LENGTH_LONG).show();
                        }
                        else if(dataSnapshot.child("chat").hasChild(dosti+username)) {
                            room_name = dosti+username;
                            dhoma = FirebaseDatabase.getInstance().getReference().child("chat").child(room_name);
                            //dhoma_created = true;
                            //Toast.makeText(getApplicationContext(),"EMRI DHOM'S: " + room_name, Toast.LENGTH_LONG).show();
                        }
                        //if(!dataSnapshot.child("chat").hasChild(username+dosti) && !dataSnapshot.child("chat").hasChild(dosti+username)) {
                        else {
                            myRef.child("chat").child(username+dosti).setValue("");
                            room_name = username+dosti;
                            dhoma = FirebaseDatabase.getInstance().getReference().child("chat").child(room_name);
                            //dhoma_created = true;
                            //Toast.makeText(getApplicationContext(),"EMRI DHOM'S: " + room_name, Toast.LENGTH_LONG).show();
                            //Toast.makeText(SelectChatFriend.this, "ignoreChat room with name: " + username+dosti + "  EXISTS.",Toast.LENGTH_SHORT).show();
                        }

                        Intent chati = new Intent(SelectChatFriend.this, Chat.class);
                        chati.putExtra("username", username);
                        chati.putExtra("dosti", dosti);
                        chati.putExtra("room_name",room_name);
                        startActivity(chati);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }
        });

    }
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, DashBoardActivity.class));
    }
}
