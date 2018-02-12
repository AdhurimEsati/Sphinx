package com.example.noli.sphinx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.noli.sphinx.R.id.cancel_action;
import static com.example.noli.sphinx.R.id.parent;

public class SendInvitationActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ArrayList<String> friends;
    private ArrayAdapter<String> adapter;
    private Button button;
    private String username;
    private String quiz;
    private Player p;
    private ProgressDialog progressDialog;
    private ListView frList;
    private String selectedFriend;
    private int selectedposition = 0;
    //private CountDownTimer timer;
    private int playing = 0;
    private DataSnapshot temp;
    private Handler handler;
    private Runnable run;
    private int timer;
    private ValueEventListener loja;
    private FloatingActionButton floatBtn;

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

        setContentView(R.layout.activity_send_invitation);
        p = new Player();

        View current = getCurrentFocus();
        if (current != null) current.clearFocus();


        frList = (ListView) findViewById(R.id.listFr);
        floatBtn = (FloatingActionButton) findViewById(R.id.floatInvite);
        //button = (Button) findViewById(R.id.floatInvite);
        quiz = getIntent().getExtras().getString("quiz");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        handler = new Handler();


        friends = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                friends);
        frList.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);

        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){

                    if(d.getKey().trim().equals(mAuth.getCurrentUser().getUid().trim())){
                        User user = d.getValue(User.class);

                        username = user.getUsername().trim();
                        //System.out.println(user.getUsername().toString());
                        gameResult.setMyUsername(username);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        frList.setBackgroundColor(Color.DKGRAY);

        frList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                View v = getViewByPosition(selectedposition, frList);
                v.setBackgroundColor(Color.DKGRAY);
                selectedposition = position;

                view.setBackgroundColor(Color.parseColor("#ff80cbc4"));
                selectedFriend = adapter.getItem(position);
                gameResult.setOponentUsername(selectedFriend);



            }
        });

        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {

                    if(!d.getKey().trim().equals(username)){
                        friends.add(d.getKey());
                        adapter.notifyDataSetChanged();
                        //friends.add("hasan");
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFriend != null){
                    progressDialog.setMessage("Waiting for opponent");
                    show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    DatabaseReference db = myRef.child("games");
                    Game g = new Game(username,selectedFriend,1,0,-1,-1,"","",0,false,quiz);
                    DatabaseReference pushedPostRef = db.push();
                    pushedPostRef.setValue(g);
                    final String gameId = pushedPostRef.getKey();
                    //selected = spinner.getSelectedItem().toString();
                    //System.out.println(selected);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());

                    Invitation i = new Invitation(username,gameId,false,currentDateandTime );
                    List<Invitation> invitations = new ArrayList<Invitation>();
                    invitations.add(i);
                    //p.setInvitations(invitations);
                    //p.setName(selectedFriend);
                    myRef.child("players").child(selectedFriend).child("invitations").setValue(invitations);
                    timer = 30;

                    //ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
                    run = new Runnable() {
                        @Override
                        public void run() {
                            if(timer <= 0){
                                //progressDialog.setMessage("...11....");
                                Toast.makeText(SendInvitationActivity.this, "Invitation Failed! Opponent didn't accept in time!" , Toast.LENGTH_SHORT).show();
                                //return;
                                myRef.child("players").child(selectedFriend).child("invitations").removeValue();
                                myRef.child("games").child(gameId).child("connect1").setValue(0);
                                //myRef.removeEventListener(loja);
                                handler.removeCallbacks(run);
                                handler.removeCallbacksAndMessages(null);
                                progressDialog.dismiss();
                            }else {
                                progressDialog.setMessage("Waiting for opponent   " + timer);
                                myRef.child("games").child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot d : dataSnapshot.getChildren()){
                                            System.out.println(d.getKey());
                                            //System.out.println(gameId);

                                            if(d.getKey().equalsIgnoreCase("connect2")){
                                                //System.out.println("testim");
                                                //String x = d.getValue(String.class);
                                                int value = d.getValue(Integer.class);
                                                if(value == 1){
                                                    temp = d;
                                                    //myRef.removeEventListener(valueEventListener);
                                                    if(playing == 0) {
                                                        playing = 1;
                                                        handler.removeCallbacks(run);
                                                        progressDialog.cancel();
                                                        Intent mIntent = new Intent(getApplicationContext(), QuizActivity.class);
                                                        //System.out.println("test");
                                                        mIntent.putExtra("quiz", quiz);
                                                        mIntent.putExtra("key", gameId);
                                                        mIntent.putExtra("oponent", selectedFriend);

                                                        finish();
                                                        startActivity(mIntent);

                                                        //myRef = null;

                                                    }
                                                }
                                            }
                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SendInvitationActivity.this, "Invite Failed!" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                                timer--;

                                handler.postDelayed(this, 1000);
                            }
                        }
                    };
                    run.run();
                    //System.out.println(gameId);
                    //progressDialog.setMessage("123");
                /*timer = new CountDownTimer(30000, 1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        progressDialog.setMessage("Waiting for opponent   " + millisUntilFinished/1000);
                        if(temp.getKey().equalsIgnoreCase("connect2")){
                            //System.out.println("testim");
                            //String x = d.getValue(String.class);
                            int value = temp.getValue(Integer.class);
                            if(value == 1){
                                t

                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        Toast.makeText(SendInvitationActivity.this, "Invitation Failed! Opponent didn't accept in time!" , Toast.LENGTH_SHORT).show();
                        myRef.child("players").child(selectedFriend).child("invitations").removeValue();
                    }
                }.start();*/



                }
                }
        });


    }

    public void show() {
        // Set the dialog to not focusable.
        progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


        // Show the dialog with NavBar hidden.
        progressDialog.show();

        // Set the dialog to focusable again.
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        progressDialog.getWindow().getDecorView().setSystemUiVisibility(
                this.getWindow().getDecorView().getSystemUiVisibility());
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
