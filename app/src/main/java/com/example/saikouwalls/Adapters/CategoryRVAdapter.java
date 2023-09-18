package com.example.saikouwalls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.* ;

import com.bumptech.glide.Glide;
import com.example.saikouwalls.Models.CategoryRVModel;
import com.example.saikouwalls.R ;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder>{
    private ArrayList<CategoryRVModel> categoryRVModels ;
    private Context context ;
    private CategoryClickInterface categoryClickInterface ;

    public CategoryRVAdapter(ArrayList<CategoryRVModel> categoryRVModels, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryRVModels = categoryRVModels;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTV ;
        private ImageView categoryIV ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.idTVCategory) ;
            categoryIV = itemView.findViewById(R.id.idIVCategory) ;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item , parent , false) ;
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
        holder.itemView.setOnClickListener(view -> categoryClickInterface.onCategoryClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return categoryRVModels.size() ;
    }
}
