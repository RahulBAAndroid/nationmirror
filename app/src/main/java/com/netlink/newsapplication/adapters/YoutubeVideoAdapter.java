package com.netlink.newsapplication.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.VideoModel;
import com.netlink.newsapplication.models.YoutubeModel;

import java.util.List;
public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YouTubeVideoViewHolder>{
     List<YoutubeModel> videoList;

    public YoutubeVideoAdapter(List<YoutubeModel> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public YouTubeVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_video_card, parent, false);
        return new YouTubeVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeVideoViewHolder holder, int position) {
        YoutubeModel videoModel = videoList.get(position);
        String videoId = videoModel.getSnippet().getResourceId().getVideoId();
        String videoUrl = "https://www.youtube.com/embed/" + videoId;

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl + "\" frameborder=\"0\" allowfullscreen></iframe>";
        holder.wvYoutube.loadData(videoHtml, "text/html", "utf-8");
        holder.wvYoutube.getSettings().setJavaScriptEnabled(true);
        holder.tvTitle.setText(videoModel.getSnippet().getTitle());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class YouTubeVideoViewHolder extends RecyclerView.ViewHolder {
        WebView wvYoutube;
        TextView tvTitle;

        public YouTubeVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            wvYoutube = itemView.findViewById(R.id.navYoutubeVideo);
            tvTitle = itemView.findViewById(R.id.tvVideoTitl);
        }
    }
}
