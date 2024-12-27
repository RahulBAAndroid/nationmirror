package com.netlink.newsapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.netlink.newsapplication.adapters.RecentNewsdapter;
import com.netlink.newsapplication.models.NewsModel;
import com.netlink.newsapplication.models.NewsModelOld;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentNewsActivity extends Activity {
    private RecentNewsdapter newsAdapter;
    private String category;
    private Dialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_news);
        Intent i = getIntent();
        category = i.getStringExtra("category");
        TextView txtCategory = findViewById(R.id.tvCategory);
        LinearLayout globalTabs = findViewById(R.id.global_tabs);
        // Show tabs only for the "Global" category
        if ("Global".equals(category)) {
            globalTabs.setVisibility(View.VISIBLE);
        } else {
            globalTabs.setVisibility(View.GONE);
        }
        txtCategory.setText(category);
        showProgress();
        if(category.equals("search")){String searchText = i.getStringExtra("SEARCH_QUERY"); fetchNewsViaSearch(searchText); return;}
        else{int categoryNumber = getCategoryNumber(category); fetchNews(categoryNumber);}
        dismissProgress();
        setupTabListeners();



    }
    private int getCategoryNumber(String category) {
        switch (category) {
            case "Sports":
                return 8823;
            case "Entertainment":
                return 8827;
            case "Business":
                return 1;
            case "Global":
                return 40;
            case "Astro":
                return 4;
            case "Infotenment":
                return 13528;
            case "Crime":
                return 9746;
            case "देश विदेश":
                return 40;
            case "मध्यप्रदेश":
                return 7;
            case "छत्तीसगढ़":
                return 10;
            case "अन्य":
                return 8;
            default:
                return 0; // Default category
        }
    }
    void fetchNews(int categoryNumber) {
        RetrofitInstance.getInstance().getApiInterface().getNewsViaCategory(100, 1, categoryNumber).enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel> newsList = response.body();
                    RecentNewsdapter newsdapter = new RecentNewsdapter(newsList,categoryNumber);
                    RecyclerView rvRecentNews = findViewById(R.id.rvRecentNews);
                    rvRecentNews.setAdapter(newsdapter);
                    rvRecentNews.setLayoutManager(new LinearLayoutManager(RecentNewsActivity.this));

                } else {
                    Toast.makeText(getBaseContext(), "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Ram Ram", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
    void fetchNewsViaSearch(String search) {
        RetrofitInstance.getInstance().getApiInterface().getNewsViaSearch(10, 1, search).enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel> newsList = response.body();
                    RecentNewsdapter newsdapter = new RecentNewsdapter(newsList);
                    RecyclerView rvRecentNews = findViewById(R.id.rvRecentNews);
                    rvRecentNews.setAdapter(newsdapter);
                    rvRecentNews.setLayoutManager(new LinearLayoutManager(RecentNewsActivity.this));

                } else {
                    Toast.makeText(getBaseContext(), "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Ram Ram", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
    private void setupTabListeners() {
        findViewById(R.id.btnDeshVidesh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                fetchNews(40);
                dismissProgress();
                // "देश विदेश"
            }
        });
        findViewById(R.id.btnMadhyaPradesh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                fetchNews(7);
                dismissProgress();
            }
        });
        findViewById(R.id.btnChhattisgarh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                fetchNews(10);
                dismissProgress();
            }
        });
        findViewById(R.id.btnAnya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                fetchNews(8);
                dismissProgress();
            }
        });
    }
    public void showProgress() {
        try {
            if (isProgressShowing())
                dismissProgress();
            mProgressDialog = new Dialog(this);
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
