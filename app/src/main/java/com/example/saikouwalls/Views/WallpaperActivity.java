package com.example.saikouwalls.Views;

import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Services.DatabaseKeys;
import com.example.saikouwalls.Services.ExtractPhotoURLNumber;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;

public class WallpaperActivity extends AppCompatActivity {
    private WallpaperManager wallpaperManager ;
    private ImageView image ;
    private ImageView btnInfo ;
    private Button setWallpaper , saveWallpaper ;
    private String url ;
    private String imgName , imgWidth , imgHeight , imgPhotographer ;
    private ProgressBar loadingPB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        // getting url from parent activity
        url = getIntent().getStringExtra("imgURL") ;
        init() ;
        sync() ;
    }
    private void init(){
        imgName   = getIntent().getStringExtra("imgALT") ;
        imgWidth  = getIntent().getStringExtra("imgWIDTH") ;
        imgHeight = getIntent().getStringExtra("imgHEIGHT") ;
        imgPhotographer = getIntent().getStringExtra("imgPHOTOGRAPHER") ;


        image = findViewById(R.id.image) ;
        btnInfo = findViewById(R.id.idBtnShowImgInfo) ;
        setWallpaper = findViewById(R.id.idBtnSetWallpaper) ;
        saveWallpaper = findViewById(R.id.idBtnDownloadWallpaper) ;
        loadingPB = findViewById(R.id.idPBLoading) ;

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext()) ;
    }
    private void sync(){
        setViews() ;
        setOnClick() ;
    }
    private void setViews(){
        loadingPB.setVisibility(View.VISIBLE) ;
        Glide.with(WallpaperActivity.this).load(url).listener(new RequestListener<Drawable>() {
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
                Glide.with(WallpaperActivity.this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
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

        saveWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedID = getIntent().getStringExtra("userID") ;
                saveToDatabase(savedID) ;
            }
        });

        btnInfo.setOnClickListener(view -> {
            showBottomSheetDialog() ;
        });
    }
    private void saveToDatabase(String UID){
        String imgID = ExtractPhotoURLNumber.getNumber(url) ;
        DatabaseReference mRealtime = FirebaseDatabase.getInstance().getReference();
        mRealtime.child(DatabaseKeys.users).child(UID).child(DatabaseKeys.savedImg).child(imgID).child(DatabaseKeys.imgURL).setValue(url);
        showCustomToast("Wallpaper has been added");
    }

    private void showBottomSheetDialog(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this) ;
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialoge_img_info) ;
        RelativeLayout rl5 = bottomSheetDialog.findViewById(R.id.RL5) ;
        TextView name = bottomSheetDialog.findViewById(R.id.idTVName) ;
        TextView dim  = bottomSheetDialog.findViewById(R.id.idTVDim) ;
        TextView photographerName = bottomSheetDialog.findViewById(R.id.idTVPhotograph) ;

        String dimensions = imgHeight +" X "+imgWidth ;
        // setting resources
        name.setText(imgName);
        dim.setText(dimensions);
        photographerName.setText(imgPhotographer) ;
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage(view);
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        bottomSheetDialog.show() ;
    }

    public void downloadImage(View view) {
        Executors.newSingleThreadExecutor();
        Executors.newSingleThreadExecutor().execute(() -> {
            mSaveMediaToStorage(mLoad(url , view) , view) ;
        });
    }
    private Bitmap mLoad(String string , View view) {
        URL url = mStringToURL(string) ;
        HttpURLConnection connection ;
        try {
            connection = (HttpURLConnection) url.openConnection() ;
            connection.connect() ;
            InputStream inputStream = connection.getInputStream() ;
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream) ;
            return BitmapFactory.decodeStream(bufferedInputStream) ;
        } catch (IOException e) {
            e.printStackTrace() ;
            Snackbar.make(view , "error" , Snackbar.LENGTH_SHORT).show();
        }
        return null;
    }
    private URL mStringToURL(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void mSaveMediaToStorage(Bitmap bitmap , View view) {
        String filename = System.currentTimeMillis() + ".jpg";
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (imageUri != null) {
                try {
                    fos = getContentResolver().openOutputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = new File(imagesDir, filename);
            try {
                fos = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (fos != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Snackbar.make(view , "Downloaded" , Snackbar.LENGTH_LONG).show() ;
        }
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