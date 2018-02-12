package com.example.noli.sphinx;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.EventListener;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button logout;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private DatabaseReference reference;
    private Button a;
    private Button b;
    private Button c;
    private Button d;
    private String username = "";
    private ImageButton play;
    private ImageButton chat;
    private Button leaderBoard;
    private String gameKey;
    private String oponent;


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
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        reference = mDatabase.getReference().child("players");
        leaderBoard = (Button) findViewById(R.id.leaderBoard);


        logout = (Button) findViewById(R.id.logOutBtn);
        a = (Button) findViewById(R.id.btnA);
        b = (Button) findViewById(R.id.btnB);
        c = (Button) findViewById(R.id.btnC);
        d = (Button) findViewById(R.id.btnD);

        final TextView userNameTV = (TextView) findViewById(R.id.userName);
        final TextView winsNr = (TextView) findViewById(R.id.winsNr);
        final TextView drawsNR = (TextView) findViewById(R.id.drawsNr);
        final TextView lossesNr = (TextView) findViewById(R.id.lossesNr);


        play = (ImageButton) findViewById(R.id.playImg);
        chat = (ImageButton) findViewById(R.id.chatImg);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //Toast.makeText(LogScreenActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth == null){
                    //finish();
                    startActivity(new Intent(DashBoardActivity.this, LogScreenActivity.class));
                }
            }
        };


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View Header = navigationView.getHeaderView(0);
        email = (TextView) Header.findViewById(R.id.emailTF2);


        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.getKey().trim().equals(mAuth.getCurrentUser().getUid().trim())){
                        User user = d.getValue(User.class);
                        email.setText(user.getUsername());


                        username = user.getUsername();
                        myRef.child("players").child(username).child("isAvailable").setValue(1);
                        userNameTV.setText(username);

                        reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot d) {

                                System.out.println("EMRI : " + username + " " + d.getValue());


                                winsNr.setText(d.child("wins").getValue().toString());
                                drawsNR.setText(d.child("draws").getValue().toString());
                                lossesNr.setText(d.child("losses").getValue().toString());



                                GenericTypeIndicator<Integer> t = new GenericTypeIndicator<Integer>() {};
                                //String s = dataSnapshot.child("wins").getValue(t)+"";

                                //drawsNR.setText(dataSnapshot.child("draws").getValue(t));
                                //lossesNr.setText(dataSnapshot.child("losses").getValue(t));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        myRef.child("players").child(username).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("invitations").child("0").child("gamekey").getValue() != null){
                                    gameKey = dataSnapshot.child("invitations").child("0").child("gamekey").getValue().toString();
                                    oponent = dataSnapshot.child("invitations").child("0").child("inviter").getValue().toString();
                                    gameResult.setMyUsername(username);
                                    gameResult.setOponentUsername(oponent);

                                    myRef.child("games").child(gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        String kuizi;
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(Integer.parseInt(dataSnapshot.child("connect1").getValue().toString()) == 1 ) {
                                                if (dataSnapshot.child("quiz").getValue() != null) {
                                                    kuizi = dataSnapshot.child("quiz").getValue().toString();
                                                    createAlertDialog(kuizi);
                                                    //myRef.removeEventListener(this);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("EMRI : " + username);





/*
        myRef.child("players").child("dioni96").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player pl = dataSnapshot.getValue(Player.class);
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.getKey().equals("invitations")) {
                        final List<Invitation> invite = pl.getInvitations();
                        for(Invitation i : invite){
                            System.out.println(i.getGamekey());
                            System.out.println(i.getInviter());
                        }
                        //String key = "";
                        //key = invite.get(0).getGamekey();
                        //myRef.child("players").child(invite.get(0).getGamekey()).setValue();

                        if(invite != null) {

                            myRef.child("games").child(invite.get(0).getGamekey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Game g = dataSnapshot.getValue(Game.class);

                                    //g.setConnect2(1);
                                    //String keyfinal = key;
                                    myRef.child("games").child(invite.get(0).getGamekey()).setValue(g);
                                    //Intent mIntent = new Intent(getApplicationContext(), QuizActivity.class);
                                    //System.out.println("test");
                                    //mIntent.putExtra("quiz", kuiz);
                                    //startActivity(mIntent);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/
        //listener qe lexon kuizet
        /*myRef.child("kuizet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    System.out.println(d.getValue());
                        Quiz q = d.getValue(Quiz.class);
                        System.out.println(q.getEmri());
                        System.out.println(q.getPershkrimi());
                        List<Question> questions = q.getPyetjet();
                        for(Question a : questions){
                            System.out.println(a.getPyetja());
                            for(String x : a.getPergjigjet()){
                                System.out.println(x);
                            }
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(DashBoardActivity.this, SelectOpponentActivity.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(DashBoardActivity.this, SelectChatFriend.class));
            }
        });

        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(DashBoardActivity.this, LeaderBoard.class));
            }
        });

    }

    private void createAlertDialog(final String kuiz){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

        builder.setMessage(oponent + " challenged you in " + kuiz + " category!")
                .setTitle("Game request!");

        final AlertDialog alert = builder.create();
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Intent intent = new Intent(DashBoardActivity.this, QuizActivity.class);
                intent.putExtra("quiz", kuiz);
                intent.putExtra("key", gameKey);
                intent.putExtra("oponent", oponent);
                intent.putExtra("inviter", oponent);
                myRef.child("games").child(gameKey).child("connect2").setValue(1);
                myRef.child("players").child(username).child("invitations").removeValue();
                //finish();
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                //alert.dismiss();
                alert.cancel();
                //Intent intent = new Intent(DashBoardActivity.this, DashBoardActivity.class);

                //startActivity(intent);
                //finish();
            }
        });


        builder.show();
/*
        myRef.child("games").child(gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            String kuizi;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Integer.parseInt(dataSnapshot.child("connect1").getValue().toString()) == 0 ) {
                    alert.cancel();
                    Toast.makeText(DashBoardActivity.this, "Oponent didn't Accept your request! Sorry...", Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        //positiveButtonLL.gravity = Gravity.CENTER;
        //positiveButton.setLayoutParams(positiveButtonLL);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_demo) {
            //return true;
            //startActivity(new Intent(this,CategoryActivity.class));
            System.out.println("inside");
            //startActivity(new Intent(this,CategoryActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_demo) {
            // Handle the camera action
            //finish();
            startActivity(new Intent(this, SelectOpponentActivity.class));


        }

        if (id == R.id.nav_chat) {
            //finish();
            startActivity(new Intent(this, SelectChatFriend.class));

        }

        if (id == R.id.nav_lb) {
            //finish();
            startActivity(new Intent(this, LeaderBoard.class));

        }

        if(id == R.id.logOutBtn){
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        myRef.child("players").child(username).child("isAvailable").setValue(0);
                        mAuth.signOut();
                        //finish();
                        Intent intent = new Intent(getApplicationContext(), LogScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finishAffinity();
                        startActivity(intent);
                        //startActivity(new Intent(DashBoardActivity.this, LogScreenActivity.class));
                    }
                });
    }
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
