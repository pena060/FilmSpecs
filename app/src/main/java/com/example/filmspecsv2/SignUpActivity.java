package com.example.filmspecsv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mfirebaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mfirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email_sign_up);
        password = findViewById(R.id.pass_sign_up);
        btnSignUp = findViewById(R.id.sign_up_button);
        tvSignIn = findViewById(R.id.have_account);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mfirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Sign up Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {//user successfully signed up now need to verify their email
                                sendEmailVerification();//send email verification to complete sign up
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

            //send email verification after user signs up
            private void sendEmailVerification(){
                FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Successfully Registered, Verification E-mail sent!", Toast.LENGTH_SHORT).show();
                                mfirebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }else {
                                Toast.makeText(SignUpActivity.this, "Verification email could not be sent, please try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }


        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });





    }

    //go back to Login on back press
    public void onBackPressed() {
        Intent intent=new Intent(SignUpActivity.this,HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}
