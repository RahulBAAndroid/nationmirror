package com.netlink.newsapplication;
import com.netlink.newsapplication.models.YoutubePlaylistResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface YoutubeApiService {
    @GET("playlistItems")
    Call<YoutubePlaylistResponse> getPlaylistItems(
            @Query("playlistId") String playlistId,
            @Query("key") String apiKey,
            @Query("part") String part// Remove the default value here
    );
}
