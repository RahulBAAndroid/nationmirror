package com.netlink.newsapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.VideoModel;

public class VideoNewsAdapter extends FirebaseRecyclerAdapter<VideoModel, VideoNewsAdapter.VideoNewsViewHolder>{
    public VideoNewsAdapter(@NonNull FirebaseRecyclerOptions<VideoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoNewsViewHolder holder, int position, @NonNull VideoModel model) {
//        String video = model.getEmbedded();
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/gNfVmswW9s4?si=6cQCZ-cPAunaGjSF\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        holder.wvYoutube.loadData(model.getEmbedded(), "text/html", "utf-8");
        holder.wvYoutube.getSettings().setJavaScriptEnabled(true);
        holder.wvYoutube.setWebChromeClient(new WebChromeClient());
        holder.tvTitle.setText(model.getTitle());
    }

    @NonNull
    @Override
    public VideoNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_video_card, parent, false);
        return new VideoNewsViewHolder(view);
    }



    public class VideoNewsViewHolder  extends RecyclerView.ViewHolder {
        WebView wvYoutube ;
        TextView tvTitle;
        public VideoNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            wvYoutube = itemView.findViewById(R.id.navYoutubeVideo);
            tvTitle = itemView.findViewById(R.id.tvVideoTitl);
        }
    }
}
