package com.example.saikouwalls.Views.HomePageActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.saikouwalls.Views.HomePageActivities.HomePageFragments.CategoryWalls.CategoryWalls;
import com.example.saikouwalls.Views.HomePageActivities.HomePageFragments.PopularWalls;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Views.HomePageActivities.HomePageFragments.SavedWalls;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    // widgets
    private BottomNavigationView bottomNavigationView ;
    public String UniqueID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initializeWidgets() ;
        setBottomNavigationView() ;
        if(checkForUser().equals(" ")){
            createAndSaveUser() ;
        }
        else
            UniqueID = checkForUser() ;
    }
    // 1 .
    private void initializeWidgets(){
        bottomNavigationView = findViewById(R.id.bottomNavView) ;
    }
    // 2 .
    private void setBottomNavigationView(){
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.idPopular);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId() ;
        if(itemID == R.id.idPopular) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, new PopularWalls()).commit();
            return true;
        }
        else if(itemID == R.id.idCategory) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, new CategoryWalls()).commit();
            return true ;
        }
        else if(itemID == R.id.idSaved) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, new SavedWalls()).commit();
            return true;
        }

        return false;
    }
    private String checkForUser(){
        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_USER" , MODE_PRIVATE) ;
        return sharedPreferences.getString("unique_user" , " ") ;
    }
    private void createAndSaveUser(){
        UniqueID = UUID.randomUUID().toString() ;
        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_USER" , MODE_PRIVATE) ;
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.putString("unique_user" , UniqueID) ;
        editor.commit() ;
    }
}