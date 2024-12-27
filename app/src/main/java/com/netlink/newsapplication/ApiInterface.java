package com.netlink.newsapplication;

import com.netlink.newsapplication.models.NewsModel;
import com.netlink.newsapplication.models.YoutubePlaylistResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

  @GET("wp-json/wp/v2/posts")
  Call<List<NewsModel>> getNews(
          @Query("per_page") int perPage,
          @Query("page") int page
  );
  @GET("wp-json/wp/v2/posts")
  Call<List<NewsModel>> getNewsViaCategory(
          @Query("per_page") int perPage,
          @Query("page") int page,
          @Query("categories") int categories
  );
  @GET("wp-json/wp/v2/posts")
  Call<List<NewsModel>> getNewsViaSearch(
          @Query("per_page") int perPage,
          @Query("page") int page,
          @Query("search") String search
  );

}
