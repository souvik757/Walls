package com.example.saikouwalls.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saikouwalls.Models.SavedWallpaperRVModel;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Views.WallpaperViews.SavedWallpaperActivity;

import java.util.ArrayList;

public class SavedWallpaperRVAdapter extends RecyclerView.Adapter<SavedWallpaperRVAdapter.ViewHolder>{
    private ArrayList<SavedWallpaperRVModel> wallpaperRVList ;
    private Context context ;

    class ViewHolder extends RecyclerView.ViewHolder{

        private CardView imageCV ;
        private ImageView wallpaperIV ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCV = itemView.findViewById(R.id.idCVWallpaper) ;
            wallpaperIV = itemView.findViewById(R.id.idIVWallpaper) ;
        }

    }
    public SavedWallpaperRVAdapter(ArrayList<SavedWallpaperRVModel> wallpaperRVList, Context context) {
        this.wallpaperRVList = wallpaperRVList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_rv_item , parent , false) ;
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(wallpaperRVList.get(position).getURL()).into(holder.wallpaperIV) ;
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context , android.R.anim.slide_in_left));
        holder.itemView.setOnClickListener(view -> {
            SavedWallpaperRVModel model = wallpaperRVList.get(position) ;
            Intent i = new Intent(context , SavedWallpaperActivity.class) ;
            i.putExtra("imgURL" , model.getURL()) ;
            i.putExtra("userID" , model.getID()) ;
            context.startActivity(i) ;
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperRVList.size() ;
    }
}
