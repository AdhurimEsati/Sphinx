package com.example.noli.sphinx;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class QuizActivity extends AppCompatActivity {

    //private TextView test;
    private final int interval = 1000; // 1 Second
    private Handler handler;
    private Runnable runnable;
    private int time = 30;
    private int sakt = 0;
    private int paskat = 0;
    private int nrPytjeve = 0;
    private Timer t;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private TextView tv;
    private String quiz;
    private String key;
    private String oponent;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Quiz q;
    private List<Question> questions;
    private int playing = 0;
    private ProgressDialog progressDialog;
    private Runnable run;
    private Runnable run2;
    private int timer = 2;
    private int secondTimer = 2;
    private int stop = 0;
    private int pauza = 0;
    private Game game;
    private Animation animeFadeIn;
    private Animation animeFadeOut;
    private List<Integer> array;
    private TextView playerOne;
    private TextView playerTwo;
    private List<Integer> answers;
    private ViewGroup transitionsContainer;
    private String inviter;

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
        setContentView(R.layout.activity_quiz);


         b1 = (Button) findViewById(R.id.btnA);
         b2 = (Button) findViewById(R.id.btnB);
         b3 = (Button) findViewById(R.id.btnC);
        b4 = (Button) findViewById(R.id.btnD);
         tv = (TextView) findViewById(R.id.pytjaTxt);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        quiz = getIntent().getExtras().getString("quiz");
        System.out.println(quiz);
        key = getIntent().getExtras().getString("key");
        oponent = getIntent().getExtras().getString("oponent");
        inviter=getIntent().getExtras().getString("inviter");
        progressDialog = new ProgressDialog(this);
        playerOne = (TextView) findViewById(R.id.playerOne);
        playerTwo = (TextView) findViewById(R.id.playerTwo);
        handler = new Handler();
        transitionsContainer = (ViewGroup) findViewById(R.id.conLayout);
        answers = new ArrayList<>();
        answers.add(0);
        answers.add(1);
        answers.add(2);
        answers.add(3);


        myRef.child("kuizet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {

                        if(d.getKey().equals(quiz)){

                            q = d.getValue(Quiz.class);
                            questions = q.getPyetjet();
                            array = new ArrayList<Integer>();
                            gameResult.setQuestions(questions);
                            //System.out.println(gameResult.getQuestions() + " FOR TESTINGGGGGGGG!!!!!!!!!!!");
                            playerOne.setText(gameResult.getMyUsername());
                            playerTwo.setText(gameResult.getOponentUsername());

                            startQuiz();
                        }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("games").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(playing == 0) {
                    playing = 1;
                    game = dataSnapshot.getValue(Game.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        activateButton1();
        activateButton2();
        activateButton3();
        activateButton4();

        myRef.child("games").child(key).child("connect1").onDisconnect().setValue(-999);
        onDisconnectListener();
        System.out.println("inviter#2 " + inviter);

    }

    private void onDisconnectListener(){
        myRef.child("games").child(key).child("connect2").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Listening+++++++++++++++++++++++++++++++++++++++++");
                if(dataSnapshot.getValue() != null && dataSnapshot.getValue().toString().equals("-999")){
                    createAlertDialog();
                    System.out.println("Listening..............................@@@@@@@@@@@@@@@@@@@@@@");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            //System.out.println("Listening..............................@@@@@@@@@@@@@@@@@@@@@@");
        });
    }

    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);

        builder.setMessage("Opponent Left The Game!")
                .setTitle("Opponent Disconnected");


        builder.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                startActivity(new Intent(QuizActivity.this, SelectOpponentActivity.class));
                finishAffinity();
                //finish();
            }
        });

        builder.create();
        handler.removeCallbacks(runnable);
        builder.show();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    //@Override
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

    public void startQuiz(){

        tv.setText(questions.get(nrPytjeve).getPyetja());
        hideAnswers();
        startHandler();
    }

    public void startHandler(){

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                final TextView test = (TextView) findViewById(R.id.timerTest);
                test.setTextColor(Color.GREEN);

                if (time == 0) {
                    array.add(0);
                    paskat++;
                    nrPytjeve++;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (nrPytjeve < questions.size()) {
                                hideButtons();

                                //handler.removeCallbacks(this);
                                timer = 2;
                                showRound();

                                //handler.removeCallbacks(runnable);
                                tv.setText(questions.get(nrPytjeve).getPyetja());

                                secondTimer = 2;
                                pauza = 1;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                                        b1.setVisibility(View.VISIBLE);
                                        b2.setVisibility(View.VISIBLE);
                                        b3.setVisibility(View.VISIBLE);
                                        b4.setVisibility(View.VISIBLE);
                                        //Collections.shuffle(answers);
                                        b1.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(0)));
                                        b2.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(1)));
                                        b3.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(2)));
                                        b4.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(3)));
                                        pauza = 0;
                                        time = 29;
                                        test.setTextColor(Color.GREEN);

                                    }
                                }, 2000);


                                //this.run();



                            } else {

                                gameResult.setMyArray(array);
                                gameResult.setOponentUsername(oponent);

                                Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                                stop = 1;
                                intent.putExtra("sakt", Integer.toString(sakt));
                                intent.putExtra("key",key);
                                intent.putExtra("pasakt", Integer.toString(paskat));
                                intent.putExtra("inviter", inviter);
                                if(inviter != null){
                                    myRef.child("games").child(key).child("secondresult").setValue(sakt);
                                    myRef.child("games").child(key).child("joinerResult").setValue(array);
                                }else{
                                    myRef.child("games").child(key).child("firstresult").setValue(sakt);
                                    myRef.child("games").child(key).child("creatorResult").setValue(array);
                                }
                                startActivity(intent);
                                finish();

                            }
                        }
                    }, 1200);


                }
                if (time < 10) {
                    test.setTextColor(Color.RED);
                }
                if(pauza == 1){
                    test.setText(String.valueOf(30));
                }else{
                    test.setText(String.valueOf(time));
                    time--;
                }

                System.out.println(time);

                handler.postDelayed(this, 1000);
                if(stop == 1){
                    handler.removeCallbacks(runnable);
                }
            }
        };
        runnable.run();
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, DashBoardActivity.class));
    }

    public void activateButton1(){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array.add(1);

                nrPytjeve++;
                int x = questions.get(nrPytjeve-1).getNr();
                String s = questions.get(nrPytjeve-1).getPergjigjet().get(x-1);
                TransitionManager.beginDelayedTransition(transitionsContainer, new Recolor());
                if(b1.getText().equals(s)){
                    sakt++;
                    b1.setBackgroundResource(R.drawable.round_green);
                    //b1.setBackgroundDrawable();
                }else{
                    paskat++;
                    b1.setBackgroundResource(R.drawable.round_red);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(nrPytjeve < questions.size()){
                            hideButtons();

                            timer =2;
                            showRound();
                            tv.setText(questions.get(nrPytjeve).getPyetja());
                            secondTimer = 2;
                            //wait2sec();

                            hideAnswers();

                            //System.out.println(nrPytjeve++);
                        }

                        else{
                            System.out.println("inside else");
                            handler.removeCallbacks(runnable);
                            //game.setFirstresult(sakt);
                            if(inviter != null){
                                myRef.child("games").child(key).child("secondresult").setValue(sakt);
                                myRef.child("games").child(key).child("joinerResult").setValue(array);
                            }else{
                                myRef.child("games").child(key).child("firstresult").setValue(sakt);
                                myRef.child("games").child(key).child("creatorResult").setValue(array);
                            }

                            gameResult.setMyArray(array);
                            gameResult.setOponentUsername(oponent);
                            Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("sakt", Integer.toString(sakt));
                            intent.putExtra("pasakt", Integer.toString(paskat));
                            intent.putExtra("inviter", inviter);


                            startActivity(intent);

                            myRef = null;
                            finish();

                        }
                    }
                }, 1200);


                //System.out.println(nrPytjeve + "pyetjet");



            }
        });
    }

    public void activateButton2(){
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array.add(2);
                nrPytjeve++;
                int x = questions.get(nrPytjeve - 1).getNr();
                String s = questions.get(nrPytjeve - 1).getPergjigjet().get(x-1);
                if (b2.getText().equals(s)) {
                    ++sakt;
                    b2.setBackgroundResource(R.drawable.round_green);
                } else {
                    paskat++;
                    b2.setBackgroundResource(R.drawable.round_red);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (nrPytjeve < questions.size()) {
                            hideButtons();

                            timer = 2;
                            showRound();

                            tv.setText(questions.get(nrPytjeve).getPyetja());
                            secondTimer = 2;
                            //wait2sec();
                            hideAnswers();


                        } else {
                            System.out.println("inside else");
                            handler.removeCallbacks(runnable);

                            gameResult.setMyArray(array);
                            gameResult.setOponentUsername(oponent);
                            //game.setFirstresult(sakt);
                            if(inviter != null){
                                myRef.child("games").child(key).child("secondresult").setValue(sakt);
                                myRef.child("games").child(key).child("joinerResult").setValue(array);
                            }else{
                                myRef.child("games").child(key).child("firstresult").setValue(sakt);
                                myRef.child("games").child(key).child("creatorResult").setValue(array);
                            }

                            Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("sakt", Integer.toString(sakt));
                            intent.putExtra("pasakt", Integer.toString(paskat));
                            intent.putExtra("inviter", inviter);
                            startActivity(intent);
                            myRef = null;
                            finish();
                        }
                    }
                }, 1200);

            }
            });

    }

    public void activateButton3(){
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array.add(3);
                nrPytjeve++;

                int x = questions.get(nrPytjeve - 1).getNr();
                String s = questions.get(nrPytjeve - 1).getPergjigjet().get(x-1);
                if (b3.getText().equals(s)) {
                    ++sakt;
                    b3.setBackgroundResource(R.drawable.round_green);
                } else {
                    paskat++;
                    b3.setBackgroundResource(R.drawable.round_red);
                }


                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (nrPytjeve < questions.size()) {

                            hideButtons();


                            timer = 2;
                            showRound();
                            tv.setText(questions.get(nrPytjeve).getPyetja());
                            secondTimer = 2;
                            hideAnswers();



                        } else {
                            System.out.println("inside else");
                            handler.removeCallbacks(runnable);

                            gameResult.setMyArray(array);
                            gameResult.setOponentUsername(oponent);
                            //game.setFirstresult(sakt);
                            if(inviter != null){
                                myRef.child("games").child(key).child("secondresult").setValue(sakt);
                                myRef.child("games").child(key).child("joinerResult").setValue(array);
                            }else{
                                myRef.child("games").child(key).child("firstresult").setValue(sakt);
                                myRef.child("games").child(key).child("creatorResult").setValue(array);
                            }
                            Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("sakt", Integer.toString(sakt));
                            intent.putExtra("pasakt", Integer.toString(paskat));
                            intent.putExtra("inviter", inviter);
                            startActivity(intent);
                            myRef = null;
                            finish();
                        }
                    }
                }, 1200);


            }

        });
    }

    public void activateButton4(){
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                array.add(4);
                nrPytjeve++;
                int x = questions.get(nrPytjeve - 1).getNr();
                String s = questions.get(nrPytjeve - 1).getPergjigjet().get(x-1);
                if (b4.getText().equals(s)) {
                    ++sakt;
                    b4.setBackgroundResource(R.drawable.round_green);
                } else {
                    paskat++;
                    b4.setBackgroundResource(R.drawable.round_red);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (nrPytjeve < questions.size()) {
                            hideButtons();

                            timer =2;
                            showRound();
                            tv.setText(questions.get(nrPytjeve).getPyetja());
                            secondTimer = 2;
                            //wait2sec();
                            hideAnswers();



                        } else {
                            System.out.println("inside else");
                            handler.removeCallbacks(runnable);
                            gameResult.setMyArray(array);
                            gameResult.setOponentUsername(oponent);
                            //game.setFirstresult(sakt);
                            if(inviter != null){
                                myRef.child("games").child(key).child("secondresult").setValue(sakt);
                                myRef.child("games").child(key).child("joinerResult").setValue(array);
                            }else{
                                myRef.child("games").child(key).child("firstresult").setValue(sakt);
                                myRef.child("games").child(key).child("creatorResult").setValue(array);
                            }
                            Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("sakt", Integer.toString(sakt));
                            intent.putExtra("pasakt", Integer.toString(paskat));
                            intent.putExtra("inviter", inviter);
                            startActivity(intent);
                            myRef = null;
                            finish();
                        }
                    }
                }, 1200);



            }
        });

    }

    private void hideButtons(){

        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        b4.setVisibility(View.GONE);
        b1.setBackgroundResource(R.drawable.round_corners);
        b2.setBackgroundResource(R.drawable.round_corners);
        b3.setBackgroundResource(R.drawable.round_corners);
        b4.setBackgroundResource(R.drawable.round_corners);
    }

    private void hideAnswers(){
        pauza = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                //Collections.shuffle(answers);
                b1.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(0)));
                b2.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(1)));
                b3.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(2)));
                b4.setText(questions.get(nrPytjeve).getPergjigjet().get(answers.get(3)));
                time = 29;
                pauza = 0;
            }
        }, 2000);
    }


    private void showRound(){
        progressDialog.setMessage("Round " + (nrPytjeve+1) +"/" + questions.size());
       show();
        run = new Runnable() {
            @Override
            public void run() {
                if(timer <= 0){
                    progressDialog.cancel();
                    handler.removeCallbacks(run);
                }
                timer--;
                handler.postDelayed(this, 900);
            }
        };
        run.run();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
            handler.removeCallbacks(runnable);
        }
        return super.onKeyDown(keyCode, event);
    }

    }

