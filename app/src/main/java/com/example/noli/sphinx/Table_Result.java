package com.example.noli.sphinx;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.TextView;

import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

public class Table_Result extends AppCompatActivity {
    private TextView pytja;
    private Button pergj1;
    private Button pergj2;
    private TextView username;
    private TextView oUsername;
    private Button prev;
    private Button next;
    private int i = 0;
    private ConstraintLayout cl;
    private TextView questionNr;
    private TextView correctA;
    private TextView txtC;
    private int bothInCorrect = 0;
    private Animation animeFadeIn;
    private Animation animeFadeOut;
    private ViewGroup transitionsContainer;
    private Handler handler;

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
        setContentView(R.layout.activity_table__result);

        pytja = (TextView) findViewById(R.id.pytjaTf);
        pergj1 = (Button) findViewById(R.id.pergj1);
        pergj2 = (Button) findViewById(R.id.pergj2);
        username = (TextView) findViewById(R.id.playerOne);
        oUsername = (TextView) findViewById(R.id.playerTwo);
        next = (Button) findViewById(R.id.nextBtn);
        prev = (Button) findViewById(R.id.prevBtn);
        questionNr = (TextView) findViewById(R.id.questionNr);
        correctA = (TextView) findViewById(R.id.correctAns);
        txtC = (TextView) findViewById(R.id.ca);
        animeFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        animeFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        transitionsContainer = (ViewGroup) findViewById(R.id.constraint);
        handler = new Handler();

        cl = (ConstraintLayout) findViewById(R.id.constraint);

        System.out.println(gameResult.getQuestions() + " FOR TESTINGGGGGGGG!!!!!!!!!!!");
        System.out.println(gameResult.getMyArray()+ " My ARRAYYYYYY!");
        System.out.println(gameResult.getOponentArray());



        //TransitionManager.beginDelayedTransition(transitionsContainer, new com.transitionseverywhere.TransitionSet().addTarget(transitionsContainer).addTransition(new Slide(Gravity.RIGHT)));

        pytja.setText(gameResult.getQuestions().get(i).getPyetja().toString());
        username.setText(gameResult.getMyUsername());
        oUsername.setText(gameResult.getOponentUsername());
        questionNr.setText("Question " + (i+1) + " : " + gameResult.getQuestions().size());
        if(gameResult.getMyArray().get(i) > 0){
            if(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString().equals(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1))){
                pergj1.setBackgroundResource(R.drawable.round_green);
                pergj1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
            }else{
                pergj1.setBackgroundResource(R.drawable.round_red);
                pergj1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
                bothInCorrect++;
            }
        }else{
            pergj1.setBackgroundResource(R.drawable.round_red);
            pergj1.setText("No Answer");
            bothInCorrect++;
        }


        if(gameResult.getOponentArray().get(i) > 0){
            if(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString().equals(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1))){
                pergj2.setBackgroundResource(R.drawable.round_green);
                pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
            }else{
                pergj2.setBackgroundResource(R.drawable.round_red);
                pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
                bothInCorrect++;
            }
        }else{
            pergj2.setBackgroundResource(R.drawable.round_red);
            pergj2.setText("No Answer");
            bothInCorrect++;
        }
        //pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
        //pergj2.setText("XXXXXXXXX");
        System.out.println(gameResult.getOponentArray().get(i).getClass().getName());
        System.out.println(gameResult.getOponentArray().get(i));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(transitionsContainer, new Fade().setDuration(900));
                pergj1.setVisibility(View.VISIBLE);
                pergj2.setVisibility(View.VISIBLE);
            }
        }, 1000);

        if(bothInCorrect == 2){
            correctA.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1).toString());
            TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.BOTTOM));

            correctA.setVisibility(View.VISIBLE);
            txtC.setVisibility(View.VISIBLE);
        }
        bothInCorrect = 0;




        cl.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                if(i+1 < gameResult.getQuestions().size()){
                    //animeFadeIn.start();
                    showRight(++i);
                }
            }

            public void onSwipeRight(){
                if(i-1 >= 0){
                    showRight(--i);
                }
            }
        });
        /*
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i+1 < gameResult.getQuestions().size()){
                    showRight(++i);
                }
            }
        });



        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i-1 >= 0){
                    showLeft(--i);
                }
            }
        });
*/
    }

    private void showRight(int i){
        //pergj1.setVisibility(View.INVISIBLE);
        //pergj2.setVisibility(View.INVISIBLE);
        TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN).excludeTarget(pergj1, true).excludeTarget(correctA, true).excludeTarget(pergj2, true));
        questionNr.setText("Question " + (i+1) + " : " + gameResult.getQuestions().size());
        pytja.setText(gameResult.getQuestions().get(i).getPyetja().toString());
        correctA.setVisibility(View.INVISIBLE);
        txtC.setVisibility(View.INVISIBLE);

        if(gameResult.getMyArray().get(i) > 0){
            if(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString().equals(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1))){
                pergj1.setBackgroundResource(R.drawable.round_green);
                pergj1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
            }else{
                pergj1.setBackgroundResource(R.drawable.round_red);
                pergj1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
                bothInCorrect++;
            }
        }else{
            pergj1.setBackgroundResource(R.drawable.round_red);
            pergj1.setText("No Answer");
            bothInCorrect++;
        }

        if(gameResult.getOponentArray().get(i) > 0){
            if(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString().equals(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1))){
                pergj2.setBackgroundResource(R.drawable.round_green);
                pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
            }else{
                pergj2.setBackgroundResource(R.drawable.round_red);
                pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
                bothInCorrect++;
            }
        }else{
            pergj2.setBackgroundResource(R.drawable.round_red);
            pergj2.setText("No Answer");
            bothInCorrect++;
        }
        //pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
        //pergj2.setText("XXXXXXXXX");
        //handler.postDelayed(new Runnable() {
           // @Override
            //public void run() {
                
        TransitionManager.beginDelayedTransition(transitionsContainer, new Fade(Fade.IN).setDuration(600));
        pergj1.setVisibility(View.VISIBLE);
        pergj2.setVisibility(View.VISIBLE);


            //}
        //}, 600);
        if(bothInCorrect == 2){
            correctA.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1).toString());
            TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.BOTTOM));
            correctA.setVisibility(View.VISIBLE);
            txtC.setVisibility(View.VISIBLE);
        }
        bothInCorrect = 0;
    }
    /*private void showLeft(int i){
        pergj1.setVisibility(View.INVISIBLE);
        pergj2.setVisibility(View.INVISIBLE);
        TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN).excludeTarget(pergj1, true).excludeTarget(correctA, true).excludeTarget(pergj2, true));
        questionNr.setText("Question " + (i+1) + " : " + gameResult.getQuestions().size());
        pytja.setText(gameResult.getQuestions().get(i).getPyetja().toString());
        correctA.setVisibility(View.INVISIBLE);
        txtC.setVisibility(View.INVISIBLE);

        if(gameResult.getMyArray().get(i) > 0){
            if(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString().equals(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1))){
                pergj1.setBackgroundResource(R.drawable.round_green);
                pergj1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
            }else{
                pergj1.setBackgroundResource(R.drawable.round_red);
                pergj1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
                bothInCorrect++;
            }
        }else{
            pergj1.setBackgroundResource(R.drawable.round_red);
            pergj1.setText("No Answer");
            bothInCorrect++;
        }


/*
        if(gameResult.getOponentArray().get(i) > 0){
            if(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString().equals(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1))){
                pergj2.setBackgroundResource(R.drawable.round_green);
                pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
            }else{
                pergj2.setBackgroundResource(R.drawable.round_red);
                pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
                bothInCorrect++;
            }
        }else{
            pergj2.setBackgroundResource(R.drawable.round_red);
            pergj2.setText("No Answer");
            bothInCorrect++;
        }
        //pergj2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
        pergj2.setText("XXXXXXXXX");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(transitionsContainer, new Fade(Fade.IN).setDuration(600));
                pergj1.setVisibility(View.VISIBLE);
                pergj2.setVisibility(View.VISIBLE);
            }
        }, 600);


        if(bothInCorrect == 1){
            correctA.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getQuestions().get(i).getNr()-1).toString());
            TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.BOTTOM));
            correctA.setVisibility(View.VISIBLE);
            txtC.setVisibility(View.VISIBLE);
        }
        bothInCorrect = 0;
    }*/

}
