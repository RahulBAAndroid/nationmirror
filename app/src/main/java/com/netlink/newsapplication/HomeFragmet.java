package com.netlink.newsapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.netlink.newsapplication.adapters.NewRecommendedNewsAdapter;
import com.netlink.newsapplication.adapters.RecomendedCardAdapter;
import com.netlink.newsapplication.models.NewsModel;
import com.netlink.newsapplication.models.NewsModelOld;
import com.netlink.newsapplication.models.YoutubeModel;
import com.netlink.newsapplication.models.YoutubePlaylistResponse;
import com.netlink.newsapplication.sqllites.NewsDatabaseHelper;
import com.netlink.newsapplication.utiles.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.internal.schedulers.NewThreadWorker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragmet extends Fragment {
    RecyclerView recyclerView;
    ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list
    public static final String NEWS_PREF = "newsPrefs";
    private NewsDatabaseHelper dbHelper;
    private boolean isNewsFetched = false; // Flag to prevent multiple fetches
    private CircularProgressIndicator progressIndicator;
    ImageSlider imageSlider;
    boolean isLogin=false;
    public static final String SHARED_PREF = "sharedPrefs";
    public static HomeFragmet newInstance() {
        HomeFragmet fragment = new HomeFragmet();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageSlider = view.findViewById(R.id.image_slider);
        recyclerView = view.findViewById(R.id.recyclerViewRecommended);
        // Fetch YouTube playlist videos
        fetchYouTubePlaylistVideos("PLdKPLmGtcGrnVHcdAPqMdZ39JhpP2eP6h");
        dbHelper = new NewsDatabaseHelper(getContext());
        checkNewsPref();
        if (checkNewsPref()==false) {
            /*// Fetch data from the API and save to SQLite
            imageList.add(new SlideModel(R.drawable.sliderimg, ScaleTypes.CENTER_CROP));
            imageList.add(new SlideModel("https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2024/09/file-image-2024-09-02t141553-1725266765.jpg", ScaleTypes.CENTER_CROP));
            imageList.add(new SlideModel("https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2023/09/trending-sports-5-1694578083.jpg", ScaleTypes.CENTER_CROP));
            fetchNews("general");*/
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(NEWS_PREF, getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isNewsFetched","false");
            editor.apply();
            isNewsFetched = true;
        }else{
            // Load data from SQLite
            loadNewsFromDatabase();
        }
        view.findViewById(R.id.btnBusiness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Finance clicked", Toast.LENGTH_SHORT).show();
               fetchNewsViaCategory(1);
            }
        });
        view.findViewById(R.id.btnSports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              fetchNewsViaCategory(8823);
            }
        });
        view.findViewById(R.id.btnAstro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              fetchNewsViaCategory(4);
            }
        });
        view.findViewById(R.id.btnGlobal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewsViaCategory(40);
            }
        });
        view.findViewById(R.id.btnEntertainment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewsViaCategory(8827);
            }
        });
        view.findViewById(R.id.btnInfotenment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewsViaCategory(13528);
            }
        });
        view.findViewById(R.id.btnCrime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(getContext(),RecentNewsActivity.class);
                i.putExtra("category","Crime");
                startActivity(i);*/
                fetchNewsViaCategory(9746);
            }
        });

        view.findViewById(R.id.btnCG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewsViaCategory(10);
            }
        });

        view.findViewById(R.id.btnMP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewsViaCategory(7);
            }
        });
        view.findViewById(R.id.btnUP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewsViaCategory(8);
            }
        });


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragmet_home,container,false);
        // Fetch data from the API and save to SQLite
        /*imageList.add(new SlideModel(R.drawable.sliderimg, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2024/09/file-image-2024-09-02t141553-1725266765.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2023/09/trending-sports-5-1694578083.jpg", ScaleTypes.CENTER_CROP));*/
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            Toast.makeText(getContext(), "No internet connection. Loading offline data.", Toast.LENGTH_SHORT).show();
            showProgress();
                showConnectToInternetDialog();
        } else {
            fetchNews("general");
        }
        return view;
    }
    private void fetchYouTubePlaylistVideos(String playlistId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeApiService apiService = retrofit.create(YoutubeApiService.class);
        String apiKey = "AIzaSyC981N3vljVWQcWn8424LgDEJ7w6qwoo5s"; // Replace with your actual API key

        Call<YoutubePlaylistResponse> call = apiService.getPlaylistItems(playlistId, apiKey, "snippet");
        call.enqueue(new Callback<YoutubePlaylistResponse>() {
            @Override
            public void onResponse(Call<YoutubePlaylistResponse> call, Response<YoutubePlaylistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<YoutubeModel> videos = response.body().getItems();
                    if (!videos.isEmpty()) {
                        // Get the first video
                        YoutubeModel firstVideo = videos.get(0);

                        // Extract video details
                        String videoId = firstVideo.getSnippet().getResourceId().getVideoId();
                        String thumbnailUrl = firstVideo.getSnippet().getThumbnails().getHigh().getUrl();

                        // Add the thumbnail to the image slider
                        imageList.add(new SlideModel(thumbnailUrl, "Watch Now", ScaleTypes.CENTER_CROP));

                        // Set click listener to open the video
                        imageSlider.setImageList(imageList);
                        imageSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void doubleClick(int i) {

                            }

                            @Override
                            public void onItemSelected(int position) {
                                if (position == 0) {
                                    // Open YouTube video
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                } else {
                    Log.e("HomeFragment", "Error fetching playlist: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<YoutubePlaylistResponse> call, Throwable t) {
                Log.e("HomeFragment", "Failure: " + t.getMessage());
            }
        });
    }
    private void fetchNews(String category) {
        dismissProgress();
            RetrofitInstance.getInstance().getApiInterface().getNews(10, 1).enqueue(new Callback<List<NewsModel>>() {
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
                        dbHelper.insertNews(newsList, category);

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
    private void fetchNewsViaCategory(int catNumb) {
        dismissProgress();
        RetrofitInstance.getInstance().getApiInterface().getNewsViaCategory(50, 1,catNumb).enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel> newsList = response.body();
                    NewRecommendedNewsAdapter adapter = new NewRecommendedNewsAdapter(newsList,catNumb);
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
    boolean checkNewsPref(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(NEWS_PREF, getActivity().MODE_PRIVATE);
        String check = sharedPreferences.getString("isNewsFetched","");
        if(check.equals("true"))
        {
           return true;
        }
        else
        {
//            Toast.makeText(this, " No t Loged in", Toast.LENGTH_SHORT).show();
           return false;
        }
    }
    void showRecommended()
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child("9826700264");
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("recentNews");

        // Get user's favorite categories
        userRef.child("feveroite").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> favoriteCategories = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    favoriteCategories.add(categorySnapshot.getKey());
                    Log.d("favCat", "onDataChange: "+favoriteCategories);
                }

                // Query news based on favorite categories
                Query recommendedNewsQuery = newsRef.orderByChild("category")
                        .startAt(favoriteCategories.get(0))
                        .endAt(favoriteCategories.get(favoriteCategories.size() - 1) + "\uf8ff");
                Log.d("favCat", "onDataChange: "+recommendedNewsQuery);
                // Set up FirebaseRecyclerAdapter with this query
                FirebaseRecyclerOptions<NewsModelOld> options = new FirebaseRecyclerOptions.Builder<NewsModelOld>()
                        .setQuery(recommendedNewsQuery, NewsModelOld.class)
                        .build();

                RecomendedCardAdapter adapter = new RecomendedCardAdapter(options);
                RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewRecommended);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadNewsFromDatabase() {
        List<NewsModel> newsList = dbHelper.getAllNews();
        // Check if the list is empty
        if (newsList.isEmpty()) {
            // Show a message or a placeholder for no data
            Toast.makeText(getContext(), "No news available offline", Toast.LENGTH_SHORT).show();
            return; // Exit the method if no news is available
        }
        NewRecommendedNewsAdapter adapter = new NewRecommendedNewsAdapter(newsList);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewRecommended);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    private void showConnectToInternetDialog() {
        dismissProgress();
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
    //For the Future Use Made By Rahul Ramchandani with Jr.Dev Priyanshu and Backend Dev Pranjal
    void getFaveroits(String email)
    {if(email==null){email="R@R.com";}
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        Query q = databaseReference.orderByChild("email").equalTo(email);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();  // Get the first user snapshot
                Map<String, String> faveroits = (Map<String, String>) userSnapshot.child("feveroite").getValue();
                Log.d("Firbase Faveroits", faveroits.toString());
                if (faveroits != null) {
                    // Log or use the favorite categories
                    for (String key : faveroits.keySet()) {
                        Log.d("Firebase Faveroits", "Category: " + key + ", Value: " + faveroits.get(key));
                        fetchNews(faveroits.get(key));
                        Toast.makeText(getContext(), faveroits.get(key), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Firebase Faveroits", "No faveroits data available.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Faveroits", "No faveroits data available Cancle.");
            }
        });
    }
    void checkLigin()
    {
        Intent i = getActivity().getIntent();
        String login = i.getStringExtra("login");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, getActivity().MODE_PRIVATE);
        String check = sharedPreferences.getString("login","");
        if(check.equals("true"))
        {
            isLogin= true;
        }
        else
        {
//            Toast.makeText(this, " No t Loged in", Toast.LENGTH_SHORT).show();
            isLogin=false;
        }
    }
     /*  WebView wvYoutube = view.findViewById(R.id.wvYoutubeVideo);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/1P9ZULF_hug?si=dPcZAflw6yQ8KjXj\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        wvYoutube.loadData(video,"text/html","utf-8");
        wvYoutube.getSettings().setJavaScriptEnabled(true);
        wvYoutube.setWebChromeClient(new WebChromeClient());*/
}
