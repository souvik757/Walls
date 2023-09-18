package com.example.saikouwalls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.saikouwalls.Views.MainMenu;

public class MainActivity extends AppCompatActivity {
    // widgets
    private TextView txtView ;
    private ProgressBar loadingBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init() ;
        sync() ;

    }
    private void sync(){
        new Handler().postDelayed(()->{
            nav() ;
            finish() ;
        } , 1200) ;
    }
    private void init(){
        txtView = findViewById(R.id.txtview) ;
        loadingBar = findViewById(R.id.progressBar) ;
    }
    private void nav(){
        Intent i = new Intent(MainActivity.this , MainMenu.class) ;
        startActivity(i);
    }
}