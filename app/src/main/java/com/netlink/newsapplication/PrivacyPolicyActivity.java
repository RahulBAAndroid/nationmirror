package com.netlink.newsapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class PrivacyPolicyActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.webViewPrivacyPolicy);
        webView.setWebViewClient(new WebViewClient()); // To open links within the WebView
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.loadUrl("https://sites.google.com/view/nationmirror/home"); // Load the privacy policy URL
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // If there is a previous page, go back
        } else {
            super.onBackPressed(); // Otherwise, exit the activity
        }
    }
}
