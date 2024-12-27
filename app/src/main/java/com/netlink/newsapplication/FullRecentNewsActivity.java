package com.netlink.newsapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlink.newsapplication.adapters.NewRecommendedNewsAdapter;
import com.netlink.newsapplication.models.NewsModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FullRecentNewsActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "favoritesPrefs";
    private static final String FAVORITES_KEY = "favoritesKey";
    TextView tvTitle;
    private String title;
    private String img_url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ImageView imgFullNews;
        TextView tvContent,tvAuthor,tvDate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_recent_full);
        imgFullNews = findViewById(R.id.news_image);
        LinearLayout contentLayout = findViewById(R.id.news_content_layout);
        tvTitle = findViewById(R.id.news_title);
        tvAuthor=findViewById(R.id.tvAuthor);
        tvDate=findViewById(R.id.tvDate);
        tvContent = findViewById(R.id.news_content);
        Intent i = getIntent();
         title = i.getStringExtra("title");
        String content = i.getStringExtra("content");
        img_url = i.getStringExtra("image");
        String link = i.getStringExtra("link");
        String author = i.getStringExtra("author");
        String date = i.getStringExtra("date");
        int categoryNumb = i.getIntExtra("categoryNumb",0);
        setNewsColor(categoryNumb);
        Glide.with(this)
                .load(img_url)
                .into(imgFullNews);
        tvTitle.setText(title);

        tvAuthor.setText(author);

        tvDate.setText(date);
        tvContent.setText(cleanHtmlContent(content));
        // Parse and display HTML content
        renderHtmlContent(content, contentLayout);
        //whatsapp
        LinearLayout btnWhatsapp=findViewById(R.id.btnShareWhatsApp);
                btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareText = tvTitle.getText().toString() + "\n" + link;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                try {
                    view.getContext().startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btnSaveToFavorites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFavorites(title, img_url);
            }
        });
        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();
        AdView mAdView = findViewById(R.id.adView);
        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    public String cleanHtmlContent(String html) {
        return Jsoup.parse(html).text(); // This will extract the plain text
    }
    private void renderHtmlContent(String html, LinearLayout contentLayout) {
        // Parse the HTML content
        Document document = Jsoup.parse(html);

        // Process each <h> and <p> tag
        Elements elements = document.body().children();
        for (Element element : elements) {
            if (element.tagName().startsWith("h")) {
                // Create a TextView for headings
                TextView headingView = new TextView(this);
                headingView.setText(element.text());
                headingView.setTextSize(20);
                headingView.setTypeface(null, Typeface.BOLD);
                headingView.setTextColor(getResources().getColor(android.R.color.black));
                headingView.setPadding(0, 16, 0, 8);
                contentLayout.addView(headingView);
            } else if (element.tagName().equals("p")) {
                // Create a TextView for paragraphs
                TextView paragraphView = new TextView(this);
                paragraphView.setText(element.text());
                paragraphView.setTextSize(16);
                paragraphView.setTextColor(getResources().getColor(android.R.color.black));
                paragraphView.setPadding(0, 8, 0, 8);
                contentLayout.addView(paragraphView);
            }
            else if (element.tagName().equals("ul")) {
                // Process list items
                Elements listItems = element.getElementsByTag("li");
                for (Element listItem : listItems) {
                    TextView bulletPointView = new TextView(this);
                    bulletPointView.setText("• " + listItem.text());
                    bulletPointView.setTextSize(16);
                    bulletPointView.setTextColor(getResources().getColor(android.R.color.black));
                    bulletPointView.setPadding(16, 4, 0, 4);
                    contentLayout.addView(bulletPointView);

                }
            }

        }
    }
    private void setNewsColor(int categoryNumb)
    {
        if (categoryNumb==8823) {
            tvTitle.setTextColor(Color.parseColor("#FF6F00")); // oRANGE color
        }
        else if (categoryNumb==8827) {
            tvTitle.setTextColor(Color.parseColor("#FF00FF")); // oRANGE color
        }
        else if (categoryNumb==1) {
            tvTitle.setTextColor(Color.parseColor("#2E7D32")); // oRANGE color
        }
        else if (categoryNumb==40) {
            tvTitle.setTextColor(Color.parseColor("#D32F2F")); // oRANGE color
        }
        else if (categoryNumb==4) {
            tvTitle.setTextColor(Color.parseColor("#F57C00")); // oRANGE color
        }
        else if (categoryNumb==13528) {
            tvTitle.setTextColor(Color.parseColor("#9575CD")); // oRANGE color
        }
        else if (categoryNumb==9746) {
            tvTitle.setTextColor(Color.parseColor("#424242")); // oRANGE color
        }
        else if (categoryNumb==7) {
            tvTitle.setTextColor(Color.parseColor("#B71C1C")); // oRANGE color
        }
        else if (categoryNumb==10) {
            tvTitle.setTextColor(Color.parseColor("#880E4F")); // oRANGE color
        }
        else if (categoryNumb==8) {
            tvTitle.setTextColor(Color.parseColor("#FF1744")); // oRANGE color
        }

    }


    //Add to Faveroite Logic might changein future

    private void saveToFavorites(String title, String imgUrl) {
        // Get existing favorites
        List<FavoriteItem> favorites = getFavorites();
        FavoriteItem newItem = new FavoriteItem(title, imgUrl);

        // Check if the item already exists
        if (favorites.contains(newItem)) {
            // Show Alert Dialog if item already exists
            showAlertDialog("Already Added", "This news is already in your favorites.");
            return;
        }

        // Add new item to the list
        favorites.add(newItem);

        // Save back to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favorites);
        editor.putString(FAVORITES_KEY, json);
        editor.apply();

        Toast.makeText(this, "Saved to Favorites", Toast.LENGTH_SHORT).show();
    }
    // Method to show an Alert Dialog
    private void showAlertDialog(String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private List<FavoriteItem> getFavorites() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String json = sharedPreferences.getString(FAVORITES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FavoriteItem>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Model for Favorite Item
    public static class FavoriteItem {
        private String title;
        private String imgUrl;

        public FavoriteItem(String title, String imgUrl) {
            this.title = title;
            this.imgUrl = imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getImgUrl() {
            return imgUrl;
        }
    }
}
