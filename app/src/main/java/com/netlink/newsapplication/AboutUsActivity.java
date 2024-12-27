package com.netlink.newsapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        // Initialize buttons
        Button btnInstagram = findViewById(R.id.btnInstagram);
        Button btnX = findViewById(R.id.btnX);
        Button btnTelegram = findViewById(R.id.btnTelegram);
        Button btnWhatsapp = findViewById(R.id.btnWhatsapp);

        // Set click listeners for each button
        btnInstagram.setOnClickListener(v -> openLink("https://www.instagram.com/nationmirror/"));
        btnX.setOnClickListener(v -> openLink("https://x.com/intent/post?text=About+Us&url=https%3A%2F%2Fnationmirror.com%2Fabout-us%2F"));
        btnTelegram.setOnClickListener(v -> openLink("https://telegram.me/share/url?url=https%3A%2F%2Fnationmirror.com%2Fabout-us%2F&text=About%20Us"));
        btnWhatsapp.setOnClickListener(v -> openLink("https://api.whatsapp.com/send?text=About%20Us%20https%3A%2F%2Fnationmirror.com%2Fabout-us%2F"));
    }
    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
