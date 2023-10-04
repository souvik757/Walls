package com.example.saikouwalls.Views.WallpaperViews;

import static android.content.ContentValues.TAG;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.saikouwalls.Views.WebViews.PhotographInfoWebActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
    private String imgName , imgWidth , imgHeight , imgPhotographer , urlPhotographer , imgColor;
    private ProgressBar loadingPB ;
    // ad mob interstitial
    private AdView mAdView ;
    private InterstitialAd mInterstitialAd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        // admob
        MobileAds.initialize(WallpaperActivity.this) ;
        initializeAdMob();
        loadBannerAd();
        loadInterstitialAd();
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
        urlPhotographer = getIntent().getStringExtra("urlPHOTOGRAPHER") ;
        imgColor = getIntent().getStringExtra("imgCOLOR") ;


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
        adjustTextColor() ;
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

    private void adjustTextColor() {
        if(getBrightness(imgColor)){
            setWallpaper.setTextColor(getColor(R.color.green));
            saveWallpaper.setTextColor(getColor(R.color.green));
        }
        else {
            setWallpaper.setTextColor(getColor(R.color.orange));
            saveWallpaper.setTextColor(getColor(R.color.orange));
        }
    }

    private void setOnClick(){
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAd() ;
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
        RelativeLayout sheet = bottomSheetDialog.findViewById(R.id.bottom_sheet_layout) ;
        RelativeLayout rl5 = bottomSheetDialog.findViewById(R.id.RL5) ;
        LinearLayout ll1 = bottomSheetDialog.findViewById(R.id.LL1) ;

        TextView DetailsTV = bottomSheetDialog.findViewById(R.id.detailsTV) ;
        TextView DimensionsTV = bottomSheetDialog.findViewById(R.id.TV2) ;
        TextView PhotographerTV = bottomSheetDialog.findViewById(R.id.TV3) ;
        TextView NameTV = bottomSheetDialog.findViewById(R.id.TV1) ;
        TextView name = bottomSheetDialog.findViewById(R.id.idTVName) ;
        TextView dim  = bottomSheetDialog.findViewById(R.id.idTVDim) ;
        TextView photographerName = bottomSheetDialog.findViewById(R.id.idTVPhotograph) ;
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
        // setting up on click listener
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage(view);
                showAd() ;
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
                Intent i = new Intent(WallpaperActivity.this , PhotographInfoWebActivity.class) ;
                i.putExtra("PhotographerInfoUrl" , urlPhotographer) ;
                startActivity(i) ;
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
    private void initializeAdMob(){
        MobileAds.initialize(WallpaperActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
    }
    private void loadBannerAd(){
        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build() ;

        mInterstitialAd.load(WallpaperActivity.this, getResources().getString(R.string.admob_interstitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.i("AdMob" , "onAdFailedToLoad" + loadAdError) ;
                        mInterstitialAd = null ;
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.i("AdMob" , "AdLoaded") ;
                        WallpaperActivity.this.mInterstitialAd = interstitialAd ;
                    }
                });
    }
    private void showAd(){
        if(mInterstitialAd != null) {
            mInterstitialAd.show(WallpaperActivity.this);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
        }
        else
        {

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