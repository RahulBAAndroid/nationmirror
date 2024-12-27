package com.netlink.newsapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.netlink.newsapplication.sqllites.NewsDatabaseHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DashBoardActivity extends AppCompatActivity {
    public static final String NEWS_PREF = "newsPrefs";
    public static final String SHARED_PREF = "sharedPrefs";
    private  InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        checkLigin();
        HomeFragmet hf = new HomeFragmet();
        loadFragment(hf);
        ImageView profileImage = findViewById(R.id.profileImage);
        ImageView imgFullVideo= findViewById(R.id.fullvideo);
        ImageView imgNotification= findViewById(R.id.notification);
        ImageView menuButton = findViewById(R.id.menuButton);
        // Set click listener for menu button
        menuButton.setOnClickListener(this::showPopupMenu);

        // Set OnClickListener to load ProfileFragment when clicked
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileFragment());
            }
        });
        // Set OnClickListener to load Full Video when clicked
        imgFullVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new VideoFragment());
            }
        });
        //Adding Full Size Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Add", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("Ad", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(DashBoardActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                loadFragment(new NotificationFragment());
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        if (bottomNavigationView == null) {
            Toast.makeText(this, "BottomNavigationView not found!", Toast.LENGTH_SHORT).show();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id==R.id.navigation_profile)
                {
                    ProfileFragment pf = new ProfileFragment();
                    loadFragment(pf);
                }
                if(id==R.id.navigation_dashboard)
                {
                    HomeFragmet hf = new HomeFragmet();
                    loadFragment(hf);


                }
                if(id==R.id.navigation_video)
                {
                    YoutubReelsFragment rf = new YoutubReelsFragment();
                    loadFragment(rf);

                }
                if(id==R.id.navigation_news)
                {

                    RecentNewsFragment rf = new RecentNewsFragment();
                    loadFragment(rf);

                }
                return true;
            }
        });


    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(ft.isEmpty()) {
           /*
            1st Iteration
            ft.add(R.id.fragmentContainer, fragment);
            ft.commit();*/
            // Replace the current fragment with the new one
            ft.replace(R.id.fragmentContainer, fragment);
            // Add this transaction to the back stack if you want to enable back navigation between fragments
            ft.addToBackStack(null);
            // Commit the transaction
            ft.commit();
        }
    }
    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    private void showPopupMenu(View view) {
        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_login, popupMenu.getMenu());

        // Set menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.whatsapp)
                {
                    openLink("https://api.whatsapp.com/send?text=About%20Us%20https%3A%2F%2Fnationmirror.com%2Fabout-us%2F");
                    return true;}
                if(menuItem.getItemId()==R.id.twitter)
                {
                    openLink("https://x.com/intent/post?text=About+Us&url=https%3A%2F%2Fnationmirror.com%2Fabout-us%2F");
                    return true;}
                if(menuItem.getItemId()==R.id.telegram)
                {
                    openLink("https://telegram.me/share/url?url=https%3A%2F%2Fnationmirror.com%2Fabout-us%2F&text=About%20Us");
                    return true;}
                if(menuItem.getItemId()==R.id.instagram)
                {
                    openLink("https://www.instagram.com/nationmirror/");
                    return true;}
                else return false;
            }
        });

        // Show the menu
        popupMenu.show();
    }
    void checkLigin()
    {
        Intent i = getIntent();
        String login = i.getStringExtra("login");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String check = sharedPreferences.getString("login","");
        if(check.equals("true"))
        {
//            Toast.makeText(this, "Loged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
//            Toast.makeText(this, " No t Loged in", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        this.finishAffinity();

    }

    @Override
    protected void onStop() {
        super.onStop();
        NewsDatabaseHelper dbHelper = new NewsDatabaseHelper(this);
        dbHelper.deleteAllNews();
        SharedPreferences sharedPreferences = getSharedPreferences(NEWS_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isNewsFetched","false");
        editor.apply();
    }
}
