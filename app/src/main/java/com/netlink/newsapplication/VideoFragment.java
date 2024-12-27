package com.netlink.newsapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import retrofit2.Callback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.netlink.newsapplication.adapters.VideoNewsAdapter;
import com.netlink.newsapplication.adapters.YoutubeVideoAdapter;
import com.netlink.newsapplication.models.VideoModel;
import com.netlink.newsapplication.models.YoutubeModel;
import com.netlink.newsapplication.models.YoutubePlaylistResponse;
import com.netlink.newsapplication.utiles.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoFragment extends Fragment {
    private RecyclerView recyclerViewVideo;
    private YoutubeVideoAdapter videoNewsAdapter;
    private List<YoutubeModel> videoList = new ArrayList<>();
    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fetchYouTubePlaylistVideos("PLMMiR3p8noAvbJgDBtYA8k98r6Bu-is73");
        View view =inflater.inflate(R.layout.fragment_video,container,false);
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            Toast.makeText(getContext(), "No internet connection. Loading offline data.", Toast.LENGTH_SHORT).show();
            showConnectToInternetDialog();
        }
        else{
            // Fetch YouTube videos from the playlist
            recyclerViewVideo = view.findViewById(R.id.recyclerViewVideo);
            recyclerViewVideo.setLayoutManager(new LinearLayoutManager(getContext()));
            videoNewsAdapter = new YoutubeVideoAdapter(videoList);
            recyclerViewVideo.setAdapter(videoNewsAdapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void fetchYouTubePlaylistVideos(String playlistId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeApiService apiService = retrofit.create(YoutubeApiService.class);
        String apiKey = "AIzaSyC981N3vljVWQcWn8424LgDEJ7w6qwoo5s"; // Replace with your actual API key

        Call<YoutubePlaylistResponse> call = apiService.getPlaylistItems(playlistId, apiKey,"snippet");
        call.enqueue(new Callback<YoutubePlaylistResponse>() {
            @Override
            public void onResponse(Call<YoutubePlaylistResponse> call, Response<YoutubePlaylistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoList.clear();
                    videoList.addAll(response.body().getItems());
                    videoNewsAdapter.notifyDataSetChanged();
                } else {
                    Log.e("VideoFragment", "Error fetching playlist: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<YoutubePlaylistResponse> call, Throwable t) {
                Log.e("VideoFragment", "Failure: " + t.getMessage());
            }


        });
    }
Dialog mProgressDialog;
    public void showProgress() {
        try {
            if (isProgressShowing())
                dismissProgress();
            mProgressDialog = new Dialog(getContext());
            mProgressDialog.setContentView(R.layout.dialogue_progress);
            mProgressDialog.setCancelable(false);
            // Make the background transparent
            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            // Get the CircularProgressIndicator from the layout
            CircularProgressIndicator progressIndicator = mProgressDialog.findViewById(R.id.progressIndicator);
            progressIndicator.setIndeterminate(true); // Set the progress indicator to indeterminate
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showConnectToInternetDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("No Internet Connection")
                .setMessage("Please connect to the internet to get the latest news.")
                .setPositiveButton("Turn On Internet", (dialog, which) -> openInternetSettings())
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Open internet settings
    private void openInternetSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }
    /* dismiss progress */
    public void dismissProgress() {
        try {
            if (mProgressDialog != null) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }, 5000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isProgressShowing() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return true;
        else return false;
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        videoNewsAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        // Stop listening when fragment is stopped
        videoNewsAdapter.stopListening();
    }*/
}
