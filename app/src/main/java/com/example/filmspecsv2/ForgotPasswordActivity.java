package com.example.filmspecsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText userEmail;
    Button userPass;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = findViewById(R.id.input_email_pass_reset);//store inputted email
        userPass = findViewById(R.id.pass_reset);//button to send reset link


        firebaseAuth = FirebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {//when button is clicked
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString();
                if (email.isEmpty()) {
                    userEmail.setError("Please enter email id");
                    userEmail.requestFocus();
                }else {

                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent to provided e-mail", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }
}
