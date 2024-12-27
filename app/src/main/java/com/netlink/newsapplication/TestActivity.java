package com.netlink.newsapplication;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netlink.newsapplication.adapters.YouTubeAdapter;
import com.netlink.newsapplication.databinding.ActivityTestBinding;
import com.netlink.newsapplication.models.YouTubeVideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTestBinding binding;
    WebView webView;
    private RecyclerView recyclerView;
    private YouTubeAdapter adapter;
    private List<YouTubeVideo> videoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setSupportActionBar(binding.toolbar);
        //webView = findViewById(R.id.web);

        recyclerView = findViewById(R.id.recycler_view);

        // Set the custom LinearLayoutManager for smooth one-item scrolling
        recyclerView.setLayoutManager(new OneByOneLinearLayoutManager(this));

        videoList = new ArrayList<>();
        adapter = new YouTubeAdapter(this, videoList);

        recyclerView.setAdapter(adapter);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                    int currentPlayingPosition = -1; // Initialize with no video playing

                    for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                        View view = layoutManager.findViewByPosition(i);
                        if (view != null) {
                            int height = view.getHeight();
                            int visibleHeight = Math.min(height, recyclerView.getHeight() - view.getTop());
                            visibleHeight = Math.max(0, visibleHeight); // Ensure it's non-negative

                            if (visibleHeight > height / 2) {
                                // Item is more than 50% visible
                                currentPlayingPosition = i;
                                break;
                            }
                        }
                    }

                    if (currentPlayingPosition != -1) {
                        adapter.playVideoAtPosition(currentPlayingPosition);
                    }
                }
            }
        });

// Automatically play the first video when the RecyclerView is initially loaded
        recyclerView.post(() -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisiblePosition != RecyclerView.NO_POSITION) {
                    adapter.playVideoAtPosition(firstVisiblePosition);
                }
            }
        });



         /*// loading https://www.geeksforgeeks.org url in the WebView.
        webView.loadUrl("https://www.youtube.com/playlist?list=PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv");*/
     /*   WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        String videoId = "PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv";  // Replace with your YouTube Shorts video ID
        loadYouTubeVideo(videoId);*/
        fetchYouTubePlaylist();
    }

   /* private void loadYouTubeVideo(String playlistId) {
        String embedUrl = "https://www.youtube.com/embed/videoseries?list=" + playlistId + "&autoplay=1&playsinline=1&loop=1";
        webView.loadUrl(embedUrl);
    }*/

    private void fetchYouTubePlaylist() {
        String apiUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLdKPLmGtcGrm9Nsrn0NVai5JRTXsE9Ctv&key=AIzaSyC981N3vljVWQcWn8424LgDEJ7w6qwoo5s";

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

        runOnUiThread(() -> adapter.notifyDataSetChanged());
    }
}