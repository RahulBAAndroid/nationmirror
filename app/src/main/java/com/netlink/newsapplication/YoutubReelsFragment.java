package com.netlink.newsapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netlink.newsapplication.adapters.YoutubeReelsAdapter;
import com.netlink.newsapplication.models.YouTubeVideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubReelsFragment extends Fragment {
    private List<YouTubeVideo> videoList = new ArrayList<>();
    private YoutubeReelsAdapter adapter;
    private ViewPager2 viewPager;
    private int currentVideoIndex = 0; // To track the current video index
    WebView webView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_youtubereels,container,false);
        webView = view.findViewById(R.id.webView);
        // Initialize videoList before using it
        videoList = new ArrayList<>(); // This ensures videoList is not null
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL); // Set vertical scrolling
        adapter = new YoutubeReelsAdapter(videoList);
        viewPager.setAdapter(adapter);

        fetchYouTubePlaylist();
        // Set up page change callback to handle video playback
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Play the video for the newly selected page
                if (currentVideoIndex != position) {
                    currentVideoIndex = position;
                    playVideoAtIndex(position);
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void fetchYouTubePlaylist() {
//        String apiUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv&key=AIzaSyC981N3vljVWQcWn8424LgDEJ7w6qwoo5s";
        String apiUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv&maxResults=30&key=AIzaSyC981N3vljVWQcWn8424LgDEJ7w6qwoo5s";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    parseJson(responseBody);
                }
            }
        });
    }

    private void parseJson(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");

            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                JsonObject snippet = item.getAsJsonObject("snippet");

                String title = snippet.get("title").getAsString();
                String description = snippet.get("description").getAsString();
                String thumbnailUrl = snippet.getAsJsonObject("thumbnails")
                        .getAsJsonObject("medium").get("url").getAsString();
                String videoId = snippet.getAsJsonObject("resourceId")
                        .get("videoId").getAsString();

                videoList.add(new YouTubeVideo(title, description, thumbnailUrl, videoId));
            }

            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        } catch (Exception e) {
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(getActivity(), "Error parsing JSON: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()
            );
        }
    }
    private void loadYouTubeVideo2(String videoId) {
        Toast.makeText(getActivity(), videoId, Toast.LENGTH_SHORT).show();
        String embedUrl = "https://www.youtube.com/embed/"+videoId+"?list=PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv&autoplay=1&playsinline=1&loop=1";
        webView.loadUrl(embedUrl);
    }
    private void showNextVideo() {
        if (videoList != null && !videoList.isEmpty()) {
            // Check if there is a next video
            if (currentVideoIndex < videoList.size() - 1) {
                currentVideoIndex++; // Move to the next video
                String nextVideoId = videoList.get(currentVideoIndex).getVideoId();
                loadYouTubeVideo2(nextVideoId);
                Toast.makeText(getContext(), "Now Playing Video ID: " + nextVideoId, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No more videos in the playlist.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Video list is empty.", Toast.LENGTH_SHORT).show();
        }
    }
    private void playVideoAtIndex(int position) {
        if (position >= 0 && position < videoList.size()) {
            YouTubeVideo video = videoList.get(position);
            adapter.playVideo(video.getVideoId(), position);
        }
    }
}
