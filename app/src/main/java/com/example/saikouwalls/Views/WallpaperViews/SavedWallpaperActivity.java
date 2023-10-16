package com.example.saikouwalls.Views.WallpaperViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.example.saikouwalls.Views.WebViews.PhotographInfoWebActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SavedWallpaperActivity extends AppCompatActivity {
    private WallpaperManager wallpaperManager ;
    private ImageView image ;
    private ImageView btnInfo ;
    private Button setWallpaper , removeWallpaper ;
    private String url ;
    private ProgressBar loadingPB ;
    // resources
    private String imgName , imgWidth , imgHeight , imgPhotographer , urlPhotographer , imgColor;
    // bottom dialog layout widgets
    private BottomSheetDialog bottomSheetDialog ;
    private RelativeLayout sheet ;
    private RelativeLayout rl5 ;
    private LinearLayout ll1 ;
    private TextView DetailsTV ;
    private TextView DimensionsTV ;
    private TextView PhotographerTV ;
    private TextView NameTV ;
    private TextView name ;
    private TextView dim ;
    private TextView photographerName ;
    // set as background layout
    private RadioGroup radioGroup ;
    private RadioButton radioButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_wallpaper);
        init() ;
        sync() ;
        // give a exposure of image information for 1.7 s
        bottomSheetDialog.show() ;
        new Handler().postDelayed(()->{
            bottomSheetDialog.hide();
        } , 1700) ;
    }
    private void init() {
        url = getIntent().getStringExtra("imgURL") ;
        imgName   = getIntent().getStringExtra("imgALT") ;
        imgWidth  = getIntent().getStringExtra("imgWIDTH") ;
        imgHeight = getIntent().getStringExtra("imgHEIGHT") ;
        imgPhotographer = getIntent().getStringExtra("imgPHOTOGRAPHER") ;
        urlPhotographer = getIntent().getStringExtra("urlPHOTOGRAPHER") ;
        imgColor = getIntent().getStringExtra("imgCOLOR") ;

        image = findViewById(R.id.idImgWallpaper) ;
        btnInfo = findViewById(R.id.idBtnShowImgInfo) ;
        setWallpaper = findViewById(R.id.idBtnSetWallpaper) ;
        removeWallpaper = findViewById(R.id.idBtnUnSaveWallpaper) ;
        loadingPB = findViewById(R.id.idPBLoading) ;

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext()) ;

        // Initialize BottomDialog Layout
        initializeBottomDialogLayout();
        initializeResourcesForBottomDialogLayout();
        initializeOnCLickForBottomDialogLayout();
    }
    private void initializeBottomDialogLayout() {
        bottomSheetDialog = new BottomSheetDialog(this) ;
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialoge_img_info) ;
        sheet = bottomSheetDialog.findViewById(R.id.bottom_sheet_layout) ;
        rl5 = bottomSheetDialog.findViewById(R.id.RL5) ;
        ll1 = bottomSheetDialog.findViewById(R.id.LL1) ;
        DetailsTV = bottomSheetDialog.findViewById(R.id.detailsTV) ;
        DimensionsTV = bottomSheetDialog.findViewById(R.id.TV2) ;
        PhotographerTV = bottomSheetDialog.findViewById(R.id.TV3) ;
        NameTV = bottomSheetDialog.findViewById(R.id.TV1) ;
        name = bottomSheetDialog.findViewById(R.id.idTVName) ;
        dim  = bottomSheetDialog.findViewById(R.id.idTVDim) ;
        photographerName = bottomSheetDialog.findViewById(R.id.idTVPhotograph) ;
    }
    private void initializeResourcesForBottomDialogLayout(){
        // setting up avg color views
        sheet.setBackgroundColor(Color.parseColor(imgColor)) ;
        // setting up text resources
        String dimensions = imgHeight +" X "+imgWidth ;
        if(getBrightness(imgColor)){
            DetailsTV .setTextColor(getColor(R.color.white));
            DimensionsTV.setTextColor(getColor(R.color.white));
            PhotographerTV.setTextColor(getColor(R.color.white));
            NameTV.setTextColor(getColor(R.color.white));
            name.setTextColor(getColor(R.color.white));
            dim.setTextColor(getColor(R.color.white));
            photographerName.setTextColor(getColor(R.color.white));
        }
        else {
            DetailsTV .setTextColor(getColor(R.color.black));
            DimensionsTV.setTextColor(getColor(R.color.black));
            PhotographerTV.setTextColor(getColor(R.color.black));
            NameTV.setTextColor(getColor(R.color.black));
            name.setTextColor(getColor(R.color.black));
            dim.setTextColor(getColor(R.color.black));
            photographerName.setTextColor(getColor(R.color.black));
        }
        name.setText(imgName);
        dim.setText(dimensions);
        photographerName.setText(imgPhotographer);
    }
    private void initializeOnCLickForBottomDialogLayout(){
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage(view);
            }
        });
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // nothing happens on dismiss event
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SavedWallpaperActivity.this , PhotographInfoWebActivity.class) ;
                i.putExtra("PhotographerInfoUrl" , urlPhotographer) ;
                startActivity(i) ;
            }
        });
    }
    private void sync() {
        setViews() ;
        setOnClick() ;
    }
    private void setViews() {
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
    private void setOnClick() {
        // set wallpaper
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetAsOptions() ;
            }
        });
        removeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedID = getIntent().getStringExtra("userID") ;
                removeFromDatabase(savedID);
            }
        });
        // show information
        btnInfo.setOnClickListener(view -> {
            bottomSheetDialog.show() ;
        });
    }
    private void showSetAsOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SavedWallpaperActivity.this) ;
        final View customLayout = getLayoutInflater().inflate(R.layout.set_as_dialog_layout , null) ;
        builder.setView(customLayout) ;
        builder.setPositiveButton("set" , ((dialog, which) -> {
            radioGroup = customLayout.findViewById(R.id.idSetAsRadioGroup) ;
            int selectedID = radioGroup.getCheckedRadioButtonId() ;
            radioButton = customLayout.findViewById(selectedID) ;
            String buttonText = radioButton.getText().toString() ;
            if(buttonText.equals(getString(R.string.set_as_bg)))
                setAsBackground() ;
            else if(buttonText.equals(getString(R.string.set_as_lck)))
                setAsLockScreen();
            else if(buttonText.equals(getString(R.string.set_as_both)))
                setAsBoth() ;
        })) ;
        AlertDialog dialog = builder.create();
        dialog.setTitle("Set Wallpaper");
        dialog.show();
    }
    private void setAsBackground(){
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
    private void setAsLockScreen(){
        Glide.with(SavedWallpaperActivity.this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                showCustomToast("Something went wrong");
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                try {
                    WallpaperManager.getInstance(SavedWallpaperActivity.this).setBitmap(resource , null , true , WallpaperManager.FLAG_LOCK) ;
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
    private void setAsBoth(){
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
                    WallpaperManager.getInstance(SavedWallpaperActivity.this).setBitmap(resource , null , true , WallpaperManager.FLAG_LOCK) ;
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
    private boolean getBrightness(String value){
        int red = Integer.parseInt(value.substring(1, 1 + 2), 16);
        int green = Integer.parseInt(value.substring(3, 3 + 2), 16);
        int blue = Integer.parseInt(value.substring(5, 5 + 2), 16);
        if (red + green + blue <= 0xff * 3 / 2)
            return true ; // white
        return false ; // black
    }
    private void showCustomToast(String message) {
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