package com.netlink.newsapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.netlink.newsapplication.adapters.NewRecommendedNewsAdapter;
import com.netlink.newsapplication.models.NewsModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    RecyclerView recyclerView;
    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_notification,container,false);
        recyclerView = view.findViewById(R.id.recyclerViewNotification);
        showProgress();
        fetchNews("general");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void fetchNews(String category) {
        dismissProgress();
        RetrofitInstance.getInstance().getApiInterface().getNews(50, 1).enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel> newsList = response.body();

                    for (NewsModel newsModel : newsList) {
                        newsModel.getYoastHeadJson().getOgImage().get(0);

                        // Add images with titles

//                            imageList.add(new SlideModel(newsModel.getYoastHeadJson().getOgImage().get(0).getUrl(), ScaleTypes.CENTER_CROP));
//                            imageSlider.setImageList(imageList);
//                        Toast.makeText(getContext(), "Sita Ram"+newsModel.getTitle().getRendered(), Toast.LENGTH_SHORT).show();
                    }

                    NewRecommendedNewsAdapter adapter = new NewRecommendedNewsAdapter(newsList,0);
                    // Insert the fetched news into SQLite database

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                } else {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
//                    Toast.makeText(getContext(), "Ram Ram", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });

        /*
         //For the Future Use Made By Rahul Ramchandani with Jr.Dev Priyanshu and Backend Dev Pranjal
        else {
            int categoryNumber=0;
            if(category.equals("Sports")){categoryNumber=8823;}
            if(category.equals("Entertainment")){categoryNumber=8827;}
            if(category.equals("Business")){categoryNumber=1;}
            if(category.equals("Global")){categoryNumber=40;}
            if(category.equals("Astro")){categoryNumber=4;}
            if(category.equals("Infotenment")){categoryNumber=13528;}
            if(category.equals("Crime")){categoryNumber=9746;}
            RetrofitInstance.getInstance().getApiInterface().getNewsViaCategory(100, 1,categoryNumber).enqueue(new Callback<List<NewsModel>>() {
                @Override
                public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<NewsModel> newsList = response.body();
                        for (NewsModel newsModel : newsList) {
                            newsModel.getYoastHeadJson().getOgImage().get(0);
                            ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list
                            // Add images with titles
                            imageList.add(new SlideModel(newsModel.getYoastHeadJson().getOgImage().get(0).getUrl(), ScaleTypes.CENTER_CROP));
                            imageList.add(new SlideModel("https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2024/09/file-image-2024-09-02t141553-1725266765.jpg", ScaleTypes.CENTER_CROP));
                            imageList.add(new SlideModel("https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2023/09/trending-sports-5-1694578083.jpg", ScaleTypes.CENTER_CROP));
                            imageSlider.setImageList(imageList);
//                        Toast.makeText(getContext(), "Sita Ram"+newsModel.getTitle().getRendered(), Toast.LENGTH_SHORT).show();
                        }
                        NewRecommendedNewsAdapter adapter = new NewRecommendedNewsAdapter(newsList);
                        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewRecommended);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                    } else {
                        Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                    Toast.makeText(getContext(), "Ram Ram", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", t.getMessage(), t);
                }
            });
        }*/
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
}
