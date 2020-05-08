package com.example.filmspecsv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity {
    private ZXingScannerView zXingScannerView;//for scanner

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Turn on camera and begin scanning
        zXingScannerView = new ZXingScannerView(this);
        zXingScannerView.setResultHandler(new ZxingScannerResultHandler());
        setContentView(zXingScannerView);
        zXingScannerView.startCamera();
    }

    @Override
    public void onPause() { //when activity is left or put on the background, pause camera
        super.onPause();
        zXingScannerView.stopCamera();
    }

    class ZxingScannerResultHandler implements ZXingScannerView.ResultHandler {//class handles what happens to result
        @Override
        public void handleResult(Result result) {//result from scan
            String resultCode = result.getText();
            String scanYes = "true_t";
            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);//vibrate phone
            vibrator.vibrate(500);//vibrate phone
            Intent i = new Intent(ScannerActivity.this, MovieDisplayActivity.class);

            i.putExtra("movie_key", resultCode);
            i.putExtra("general_key", scanYes);
            startActivity(i);
            zXingScannerView.stopCamera();




        }
    }//end of scanner



}
