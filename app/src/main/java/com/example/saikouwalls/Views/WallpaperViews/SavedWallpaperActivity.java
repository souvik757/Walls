package com.example.saikouwalls.Views.WallpaperViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Services.DatabaseKeys;
import com.example.saikouwalls.Services.ExtractPhotoURLNumber;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class SavedWallpaperActivity extends AppCompatActivity {
    private WallpaperManager wallpaperManager ;
    private ImageView image ;
    private Button setWallpaper , removeWallpaper ;
    private String url ;
    private ProgressBar loadingPB ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_wallpaper);
        init() ;
        sync() ;
    }
    private void init(){
        url = getIntent().getStringExtra("imgURL") ;

        image = findViewById(R.id.idImgWallpaper) ;
        setWallpaper = findViewById(R.id.idBtnSetWallpaper) ;
        removeWallpaper = findViewById(R.id.idBtnUnSaveWallpaper) ;
        loadingPB = findViewById(R.id.idPBLoading) ;

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext()) ;
    }
    private void sync(){
        setViews() ;
        setOnClick() ;
    }
    private void setViews(){
        loadingPB.setVisibility(View.VISIBLE) ;
        Glide.with(SavedWallpaperActivity.this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                loadingPB.setVisibility(View.GONE) ;
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                loadingPB.setVisibility(View.GONE) ;
                return false;
            }
        }).into(image) ;
    }
    private void setOnClick(){
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(SavedWallpaperActivity.this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        showCustomToast("Something went wrong");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            wallpaperManager.setBitmap(resource);
                            showCustomToast("wallpaper has been set");
                        } catch (IOException e) {
                            showCustomToast("fail to set wallpaper");
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                }).submit() ;
                showCustomToast("wallpaper has been set");
            }
        });

        removeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedID = getIntent().getStringExtra("userID") ;
                removeFromDatabase(savedID);
            }
        });
    }
    private void removeFromDatabase(String UID){
        String imgID = ExtractPhotoURLNumber.getNumber(url) ;
        DatabaseReference mRealtime = FirebaseDatabase.getInstance().getReference();
        mRealtime.child(DatabaseKeys.users).child(UID).child(DatabaseKeys.savedImg).child(imgID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showCustomToast("Wallpaper has been removed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showCustomToast("Something went wrong");
            }
        }) ;
    }

    private void showCustomToast(String message){
        LayoutInflater inflater = getLayoutInflater() ;
        View layout = inflater.inflate(R.layout.custom_toast_layout , (ViewGroup) findViewById(R.id.containerToast)) ;
        ImageView img = layout.findViewById(R.id.imageViewToast) ;
        img.setImageResource(R.drawable.baseline_notifications_active_24);
        TextView txt = layout.findViewById(R.id.textViewToast) ;
        txt.setText(message);
        Toast toast = new Toast(getApplicationContext()) ;
        toast.setDuration(Toast.LENGTH_SHORT) ;
        toast.setView(layout);
        toast.show() ;
    }
}