package com.example.filmspecsv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

public class LoginActivity extends AppCompatActivity {
    //login and verification variables/buttons
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //forgot password button/text
    TextView forgotPass;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //sign in the verification
        mfirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email_sign_in);
        password = findViewById(R.id.pass_sign_in);
        btnSignIn = findViewById(R.id.sign_in_button);
        tvSignUp = findViewById(R.id.make_account);

        //forgot password
        forgotPass = findViewById(R.id.reset_password);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mfirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Error, Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                //Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                //startActivity(intToHome);
                                checkEmailVerification();
                            }
                        }
                    });
                }
                    else{
                        Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            forgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

                }
            });{

        }

            tvSignUp.setOnClickListener(new  View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intSignUp);
                    finish();
                }
            });

        //initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        }

        //make sure user is verified before allowing them to log in
        private void checkEmailVerification(){
            FirebaseUser firebaseUser = mfirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            Boolean emailflag = firebaseUser.isEmailVerified();

            if(emailflag){
                finish();
                Toast.makeText(LoginActivity.this, "You are Logged in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ProfileLibraryActivity.class));
                overridePendingTransition(0,0);

            }else{
                Toast.makeText(LoginActivity.this, "Please verify your email to sign in", Toast.LENGTH_SHORT).show();
                mfirebaseAuth.signOut();
            }

        }



    //go back to home on back press
    public void onBackPressed() {
        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

}
