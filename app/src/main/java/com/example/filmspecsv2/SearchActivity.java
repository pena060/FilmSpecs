package com.example.filmspecsv2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageSwipeAdapter adapter;
    private ImageButton scanner;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//start of oncreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewPager = (ViewPager)findViewById(R.id.image_swiper);
        adapter = new ImageSwipeAdapter(this);
        viewPager.setAdapter(adapter);

        scanner = (ImageButton)findViewById(R.id.scan_barcode);
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannerActivity();
            }
        });

        //initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //assign icon option to this activity
        bottomNavigationView.setSelectedItemId(R.id.search);

        //listen to item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile_library:
                        startActivity(new Intent(getApplicationContext(),ProfileLibraryActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.search:
                        return true;
                }
                return false;
            }
        });


    }//end of oncreate


    public void openScannerActivity(){
        startActivity(new Intent(SearchActivity.this,ScannerActivity.class));
        overridePendingTransition(0,0);
    }


    @Override
    public void onBackPressed() { //go back to home on back press
        Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);

    }//end of back pressed over ride

}//end of main function
