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
import com.example.saikouwalls.Models.WallpaperRVModel;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Views.WallpaperViews.WallpaperActivity;

import java.util.* ;
public class WallpaperRVAdapter extends RecyclerView.Adapter<WallpaperRVAdapter.ViewHolder> {
    private ArrayList<WallpaperRVModel> wallpaperList ;
    private Context context ;

    public WallpaperRVAdapter(ArrayList<WallpaperRVModel> wallpaperList, Context context) {
        this.wallpaperList = wallpaperList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView imageCV ;
        private ImageView wallpaperIV ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCV = itemView.findViewById(R.id.idCVWallpaper) ;
            wallpaperIV = itemView.findViewById(R.id.idIVWallpaper) ;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_rv_item , parent , false) ;
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(wallpaperList.get(position).getURL()).into(holder.wallpaperIV) ;
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context , android.R.anim.slide_in_left));
        holder.itemView.setOnClickListener(view -> {
            WallpaperRVModel model = wallpaperList.get(position) ;
            Intent intent = new Intent(context , WallpaperActivity.class) ;
            intent.putExtra("userID" , model.getID()) ;
            intent.putExtra("imgURL" , model.getURL()) ;
            intent.putExtra("imgWIDTH" , model.getWIDTH()) ;
            intent.putExtra("imgHEIGHT" , model.getHEIGHT()) ;
            intent.putExtra("imgALT" , model.getALT()) ;
            intent.putExtra("imgPHOTOGRAPHER" , model.getPHOTOGRAPHER_NAME()) ;
            intent.putExtra("urlPHOTOGRAPHER" , model.getPHOTOGRAPHER_INFO_URL()) ;
            intent.putExtra("imgCOLOR" , model.getAVG_COLOR()) ;

            context.startActivity(intent) ;
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size() ;
    }
}
