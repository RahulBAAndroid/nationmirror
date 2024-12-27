package com.netlink.newsapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.YouTubeVideo;

import java.util.List;

public class YoutubeReelsAdapter extends RecyclerView.Adapter<YoutubeReelsAdapter.YoutubeReelsViewHolder> {
    private List<YouTubeVideo> videoList;
    public YoutubeReelsAdapter(List<YouTubeVideo> videoList) {
        this.videoList = videoList;
    }
    @NonNull
    @Override
    public YoutubeReelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reels_youtube, parent, false);
        return new YoutubeReelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeReelsViewHolder holder, int position) {
        YouTubeVideo video = videoList.get(position);

        // Load the video in WebView
        String embedUrl = "https://www.youtube.com/embed/" + video.getVideoId() + "?list=PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv&autoplay=1&playsinline=1&loop=1";
        holder.webView.loadUrl(embedUrl);

        WebSettings webSettings = holder.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        holder.webView.setWebViewClient(new WebViewClient());
        holder.webView.getSettings().setDomStorageEnabled(true);
        holder.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
    public void playVideo(String videoId, int position) {
        notifyItemChanged(position); // Ensure the correct video plays
    }

    public static class YoutubeReelsViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        public YoutubeReelsViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
        }
    }
}
