package com.example.noli.sphinx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ProgressDialog pg;
    private Handler handler;
    private Runnable run;
    private String key;
    private Button showResults;
    private ViewGroup transitionsContainer;
    private TextView playerOne;
    private TextView playerTwo;
    private TextView correct1;
    private TextView correct2;
    private TextView incorrect1;
    private TextView incorrect2;
    private TextView winLose;
    private TextView waiting;
    private ProgressBar pb;
    private String inviter;

    //ME ndreq me ngu cili kreator eshte!!!

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
        setContentView(R.layout.activity_result);
        //final TextView txt1 = (TextView) findViewById(R.id.sakt);
        //final TextView txt2 = (TextView) findViewById(R.id.pasakt);
        playerOne = (TextView) findViewById(R.id.playerOne);
        playerTwo = (TextView) findViewById(R.id.playerTwo);
        correct1 = (TextView) findViewById(R.id.saktO);
        incorrect1 = (TextView) findViewById(R.id.paSaktO);
        correct2 = (TextView) findViewById(R.id.saktT);
        incorrect2 = (TextView) findViewById(R.id.paSaktT);
        winLose = (TextView) findViewById(R.id.winLose);
        waiting= (TextView) findViewById(R.id.waiting);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        mDatabase = FirebaseDatabase.getInstance();
        handler = new Handler();
        showResults = (Button) findViewById(R.id.showResults);
        transitionsContainer = (ViewGroup) findViewById(R.id.cons);

        myRef = mDatabase.getReference();
        pg = new ProgressDialog(this);
        final String s = getIntent().getStringExtra("sakt");
        final String s1 = getIntent().getStringExtra("pasakt");
        inviter = getIntent().getExtras().getString("inviter");
        key = getIntent().getStringExtra("key");
        //System.out.println(s  + "-" + s1);
        //txt1.setText("Sakte : " +s);
        //txt2.setText("Pasakte : " + s1);
        playerOne.setText(gameResult.getMyUsername());
        playerTwo.setText(gameResult.getOponentUsername());

        correct1.setText(s);
        incorrect1.setText(s1);



        myRef.child("games").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(inviter == null){
                        if(d.getKey().equals("secondresult")){
                            int value = d.getValue(Integer.class);
                            if(value != -1){
                                int firstresult = Integer.parseInt(s);
                                TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                                correct2.setText(value + "");
                                incorrect2.setText(Math.abs(value-gameResult.getQuestions().size())+"");
                                correct2.setVisibility(View.VISIBLE);
                                incorrect2.setVisibility(View.VISIBLE);


                                TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                                pb.setVisibility(View.GONE);
                                if(firstresult > value){

                                    //Toast.makeText(ResultActivity.this, "You Won!", Toast.LENGTH_LONG).show();
                                    winLose.setText("You Won");
                                    waiting.setVisibility(View.GONE);
                                    winLose.setVisibility(View.VISIBLE);

                                    myRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            int var = mutableData.child("players").child(gameResult.getMyUsername()).child("wins").getValue(Integer.class);
                                            mutableData.child("players").child(gameResult.getMyUsername()).child("wins").setValue(var + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                        }
                                    });

                                }else if(firstresult == value){

                                    //Toast.makeText(ResultActivity.this, "You Draw!", Toast.LENGTH_LONG).show();
                                    winLose.setText("Draw");
                                    waiting.setVisibility(View.GONE);
                                    winLose.setVisibility(View.VISIBLE);

                                }else if(firstresult < value){

                                    //Toast.makeText(ResultActivity.this, "You Lost!", Toast.LENGTH_LONG).show();
                                    winLose.setText("You Lost");
                                    waiting.setVisibility(View.GONE);
                                    winLose.setVisibility(View.VISIBLE);
                                }

                                listenForResult();
                                showResults.setVisibility(View.VISIBLE);
                                myRef.removeEventListener(this);
                            }
                        }
                    }else{
                        if(d.getKey().equals("firstresult")){
                            int value = d.getValue(Integer.class);
                            if(value != -1){
                                int firstresult = Integer.parseInt(s);
                                TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                                correct2.setText(value + "");
                                incorrect2.setText(Math.abs(value-gameResult.getQuestions().size())+"");
                                correct2.setVisibility(View.VISIBLE);
                                incorrect2.setVisibility(View.VISIBLE);
                                //listenForResult();

                                TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                                pb.setVisibility(View.GONE);
                                if(firstresult > value){

                                    //Toast.makeText(ResultActivity.this, "You Won!", Toast.LENGTH_LONG).show();
                                    winLose.setText("You Won");
                                    waiting.setVisibility(View.GONE);
                                    winLose.setVisibility(View.VISIBLE);

                                    myRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            int var = mutableData.child("players").child(gameResult.getMyUsername()).child("wins").getValue(Integer.class);
                                            mutableData.child("players").child(gameResult.getMyUsername()).child("wins").setValue(var + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                        }
                                    });
                                    //myRef.child("players").child(gameResult.getMyUsername()).child("wins")

                                }else if(firstresult == value){

                                    //Toast.makeText(ResultActivity.this, "You Draw!", Toast.LENGTH_LONG).show();
                                    winLose.setText("Draw");
                                    waiting.setVisibility(View.GONE);
                                    winLose.setVisibility(View.VISIBLE);
                                    myRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            int var = mutableData.child("players").child(gameResult.getMyUsername()).child("draws").getValue(Integer.class);
                                            mutableData.child("players").child(gameResult.getMyUsername()).child("draws").setValue(var + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                        }
                                    });

                                }else if(firstresult < value){

                                    //Toast.makeText(ResultActivity.this, "You Lost!", Toast.LENGTH_LONG).show();
                                    winLose.setText("You Lost");
                                    waiting.setVisibility(View.GONE);
                                    winLose.setVisibility(View.VISIBLE);
                                    myRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            int var = mutableData.child("players").child(gameResult.getMyUsername()).child("losses").getValue(Integer.class);
                                            mutableData.child("players").child(gameResult.getMyUsername()).child("losses").setValue(var + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                        }
                                    });
                                }

                                listenForResult();
                                showResults.setVisibility(View.VISIBLE);
                                myRef.removeEventListener(this);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showResults.setVisibility(View.INVISIBLE);
        showResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pg.cancel();
                startActivity(new Intent(ResultActivity.this, Table_Result.class));
            }
        });

    }

    public void show() {
        // Set the dialog to not focusable.
        pg.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


        // Show the dialog with NavBar hidden.
        pg.show();

        // Set the dialog to focusable again.
        pg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        pg.getWindow().getDecorView().setSystemUiVisibility(
                this.getWindow().getDecorView().getSystemUiVisibility());
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, DashBoardActivity.class));
    }

    private void listenForResult(){

        myRef.child("games").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(inviter != null){
                    GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {};

                    gameResult.setOponentArray(dataSnapshot.child("creatorResult").getValue(t));
                }else{
                    GenericTypeIndicator<List<Integer>> t = new GenericTypeIndicator<List<Integer>>() {};

                    gameResult.setOponentArray(dataSnapshot.child("joinerResult").getValue(t));

                }

                System.out.println("inviter : " + inviter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
