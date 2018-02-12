package com.example.noli.sphinx;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ScrollingResults extends AppCompatActivity {
    private ConstraintLayout cl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cl = (ConstraintLayout) findViewById(R.id.constrainLayot);
        ConstraintSet set = new ConstraintSet();

        ConstraintLayout.LayoutParams params1 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);
        ConstraintLayout.LayoutParams params2 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);



        for(int i = 0; i < gameResult.getQuestions().size(); i++){
            TextView pytja = new TextView(this);
            TextView ans1 = new TextView(this);
            TextView ans2 = new TextView(this);
            pytja.setId(i);
            ans1.setId(i);
            ans2.setId(i);

            pytja.setText(gameResult.getQuestions().get(i).getPyetja().toString());
            ans1.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getMyArray().get(i)-1).toString());
            //ans2.setText(gameResult.getQuestions().get(i).getPergjigjet().get(gameResult.getOponentArray().get(i)-1).toString());
            ans2.setText("hello");

            pytja.setLayoutParams(params1);
            ans1.setLayoutParams(params2);
            ans2.setLayoutParams(params2);

            cl.addView(pytja);
            cl.addView(ans1);
            cl.addView(ans2);
        }
    }
}
