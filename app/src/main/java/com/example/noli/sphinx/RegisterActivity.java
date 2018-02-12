package com.example.noli.sphinx;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {
    private Button signUp;
    private EditText email;
    private String emailStr;
    private EditText password;
    private EditText confirmPass;
    private EditText user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    boolean emailCorrect = false;
    boolean passwordBigger8 = false;
    boolean passwordsMatch = false;



    //private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        signUp = (Button) findViewById(R.id.signUpBtn);
        email = (EditText) findViewById(R.id.emailTF2);
        password = (EditText) findViewById(R.id.passTF);
        confirmPass = (EditText) findViewById(R.id.confirmPassTF);
        user = (EditText) findViewById(R.id.usernameTF);
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        progressDialog = new ProgressDialog(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    emailCorrect = true;
                }else {
                    password.setText("");
                    confirmPass.setText("");
                    email.setText("");
                    user.setText("");
                    email.setHintTextColor(Color.GRAY);
                    email.setHint("Enter a valid e-mail address!");
                }
                if(password.getText().toString().length() > 8) {
                    passwordBigger8 = true;
                }else {
                    password.setText("");
                    confirmPass.setText("");
                    email.setText("");
                    user.setText("");
                    password.setHintTextColor(Color.GRAY);
                    password.setHint("Password must contain 8 or more characters");
                }
                if(password.getText().toString().equals(confirmPass.getText().toString())) {
                    passwordsMatch = true;
                }else {
                    password.setText("");
                    confirmPass.setText("");
                    email.setText("");
                    user.setText("");
                    confirmPass.setHintTextColor(Color.GRAY);
                    if(passwordBigger8) {
                        password.setHint("Both Passwords must match");
                        confirmPass.setHint("Both passwords must match");
                    }
                    else if(!passwordBigger8) {
                        confirmPass.setHint("Both passwords must match");
                    }
                }

                if(emailCorrect && passwordBigger8 && passwordsMatch) {
                    register();
                }

            }
        });
    }

    private void register(){
        String emailAdd = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmP = confirmPass.getText().toString().trim();
        final String username = user.getText().toString().trim();

        if(TextUtils.isEmpty(emailAdd) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmP) || TextUtils.isEmpty(username)){
            //Snackbar.make(RegisterActivity.this, "Please fill all fields!", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.equals(pass, confirmP)){
            Toast.makeText(this, "Password's didn't match!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailAdd, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Registered Successifully", Toast.LENGTH_SHORT).show();
                            String email = mAuth.getCurrentUser().getEmail();
                            String key = mAuth.getCurrentUser().getUid();
                            User u = new User(username,email);
                            //mDatabase.child("users").child(userId).child("username").setValue(name);
                            myRef.child("users").child(key).setValue(u);
                            List<Invitation> invitations = new ArrayList<Invitation>();
                            Player p = new Player(username,0,0,0,0,invitations);
                            myRef.child("players").child(username).setValue(p);

                            //database
                        }else{
                            Toast.makeText(RegisterActivity.this, "Registered Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
