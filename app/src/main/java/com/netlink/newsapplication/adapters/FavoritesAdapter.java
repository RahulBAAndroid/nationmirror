package com.netlink.newsapplication.adapters;
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
import com.netlink.newsapplication.FullRecentNewsActivity;
import com.netlink.newsapplication.R;
import com.netlink.newsapplication.FullRecentNewsActivity.FavoriteItem;

import java.util.List;
public class FavoritesAdapter extends  RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private List<FavoriteItem> favoritesList;

    public FavoritesAdapter(List<FavoriteItem> favoritesList, Context context) {
        this.favoritesList = favoritesList;
        this.context = context;
    }

    private Context context;

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        FavoriteItem favorite = favoritesList.get(position);
        holder.tvTitle.setText(favorite.getTitle());

        // Load image using Glide
        Glide.with(context).load(favorite.getImgUrl()).into(holder.imgFavorite);

       /* holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullRecentNewsActivity.class);
            intent.putExtra("title", favorite.getTitle());
            intent.putExtra("image", favorite.getImgUrl());
            context.startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgFavorite;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }
    }
}
