package com.example.filmspecsv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    Button btnLogout; //logout button
    //FirebaseAuth mFirebaseAuth;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //code needed to logout
        btnLogout = findViewById(R.id.log_out);

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(SettingsActivity.this, "Sign out Successful", Toast.LENGTH_SHORT).show();
                Intent intToLogin = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                finish();
            }
        });




    }

    //go back to Library on back press
    public void onBackPressed() {
        Intent intent=new Intent(SettingsActivity.this,ProfileLibraryActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}
