package com.example.noli.sphinx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogScreenActivity extends AppCompatActivity {
    private SignInButton googleBtn;
    private EditText email;
    private EditText password;
    private Button signIn;
    private ProgressDialog progressDialog;
    private TextView register;
    private String emailStr;

    private  static final int RC_SIGN_IN = 10;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LogScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_screen);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LogScreenActivity.this, DashBoardActivity.class));
                    finish();
                }

            }
        };
        //Google Button Text and Color
        googleBtn = (SignInButton) findViewById(R.id.googleBtn);
        //googleBtn.setSize(SIZE_WIDE);
        //googleBtn.setColorScheme(COLOR_DARK);
        TextView textView = (TextView) googleBtn.getChildAt(0);
        textView.setText("Sign in with Google");

        email = (EditText) findViewById(R.id.emailTF2);
        password = (EditText) findViewById(R.id.passTF);
        signIn = (Button) findViewById(R.id.signInBtn);
        register = (TextView) findViewById(R.id.registerTxt);

        progressDialog = new ProgressDialog(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LogScreenActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                emailStr = email.getText().toString().trim();
                int length = emailStr.length();
                int atPos = 0;

                for(int c = 0; c < length; c++) {
                    if(emailStr.charAt(c) == 64) {
                        atPos = c;
                        c = length+1;
                    }
                }

                if(isValidEmail(emailStr) && emailStr.charAt(0) != 46 && emailStr.charAt(atPos-1) != 46) {
                    signInEP();
                    //startActivity(new Intent(LogScreenActivity.this, QuizActivity.class));
                }else {
                    email.setText("");
                    password.setText("");
                    email.setHintTextColor(Color.GRAY);
                    email.setHint("Enter a valid e-mail address");
                    Toast.makeText(LogScreenActivity.this, "Please enter valid Email", Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogScreenActivity.this, RegisterActivity.class));
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInEP(){
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if(TextUtils.isEmpty(e) || TextUtils.isEmpty(p)){
            Toast.makeText(this, "Please enter valid Email and Password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Signing In.....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LogScreenActivity.this, DashBoardActivity.class));
                }else{
                    Toast.makeText(LogScreenActivity.this, "Email or Password Incorrect", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressDialog.setMessage("Signing In.....");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LogScreenActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                });
    }

}
