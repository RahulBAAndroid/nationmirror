package com.netlink.newsapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.NewsModelOld;

public class RecomendedCardAdapter extends FirebaseRecyclerAdapter<NewsModelOld, RecomendedCardAdapter.RecommendedNewsViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecomendedCardAdapter(@NonNull FirebaseRecyclerOptions<NewsModelOld> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecommendedNewsViewHolder holder, int position, @NonNull NewsModelOld model) {
        holder.tvRecommended.setText(model.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(model.getImageUrl())
                .into(holder.imgRecomended);
    }

    @NonNull
    @Override
    public RecommendedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_recomended_card, parent, false);
        return new RecommendedNewsViewHolder(view);
    }


    public class RecommendedNewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecommended;
        ImageView imgRecomended;
        public RecommendedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecommended = itemView.findViewById(R.id.tvRecomendedNews);
            imgRecomended = itemView.findViewById(R.id.imgRecomended);

        }
    }
}
