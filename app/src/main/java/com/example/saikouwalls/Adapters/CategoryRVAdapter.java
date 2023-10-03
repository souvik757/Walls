package com.example.saikouwalls.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saikouwalls.Models.CategoryRVModel;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Views.FragmentViews.HomePageFragments.CategoryWalls.WallsByCategory;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder>{
    private ArrayList<CategoryRVModel> categoryRVModels ;
    private Context context ;

    public CategoryRVAdapter(ArrayList<CategoryRVModel> categoryRVModels, Context context) {
        this.categoryRVModels = categoryRVModels;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTV ;
        private ImageView categoryIV ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.CategoryTexts) ;
            categoryIV = itemView.findViewById(R.id.CategoryImages) ;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item_l , parent , false) ;
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryRVModel model = categoryRVModels.get(position) ;
        holder.categoryTV.setText(model.getCategory()) ;
        if(!model.getImgUrl().isEmpty())
            Glide.with(context).load(model.getImgUrl()).into(holder.categoryIV) ;
        else
            holder.categoryIV.setImageResource(R.color.lightViolate) ;
        holder.itemView.setOnClickListener(view ->{
            Intent i = new Intent(context , WallsByCategory.class) ;
            i.putExtra("SelectedCategory" , model.getCategory()) ;
            i.putExtra("userID" , model.getID()) ;
            context.startActivity(i) ;
        });
    }

    @Override
    public int getItemCount() {
        return categoryRVModels.size() ;
    }
}
