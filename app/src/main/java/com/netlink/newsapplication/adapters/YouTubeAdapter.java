package com.netlink.newsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.YouTubeVideo;

import java.util.List;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.VideoViewHolder> {

    private Context context;
    private List<YouTubeVideo> videoList;
    private int currentPlayingPosition = 0; // Track the current playing position

    public YouTubeAdapter(Context context, List<YouTubeVideo> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        YouTubeVideo video = videoList.get(position);
        holder.title.setText(video.getTitle());
        holder.description.setText(video.getDescription());

        // Configure WebView settings
        holder.thumbnail.getSettings().setJavaScriptEnabled(true);
        holder.thumbnail.setWebViewClient(new WebViewClient());
        holder.thumbnail.getSettings().setDomStorageEnabled(true);
        holder.thumbnail.getSettings().setMediaPlaybackRequiresUserGesture(false);

        // Check if this is the current playing position
        if (currentPlayingPosition == position) {
            holder.playVideo(video.getVideoId());
        } else {
            holder.stopVideo();
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void playVideoAtPosition(int position) {
        int previousPlayingPosition = currentPlayingPosition;
        currentPlayingPosition = position;

        // Stop the previous video
        if (previousPlayingPosition != -1 && previousPlayingPosition != currentPlayingPosition) {
            notifyItemChanged(previousPlayingPosition);
        }

        // Start the new video
        notifyItemChanged(currentPlayingPosition);
    }



    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        WebView thumbnail;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            description = itemView.findViewById(R.id.video_description);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
        }
        public void playVideo(String videoId) {
            String embedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&playsinline=1&loop=1";
            thumbnail.loadUrl(embedUrl);
        }

        public void stopVideo() {
            thumbnail.loadUrl("about:blank"); // Stops the WebView content
        }
    }
}
