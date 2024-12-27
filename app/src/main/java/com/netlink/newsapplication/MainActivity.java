package com.netlink.newsapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        // Initialize the VideoView
        VideoView videoView = findViewById(R.id.videoView);

        // Set the video URI
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
        videoView.setVideoURI(videoUri);

        // Start the video
        videoView.start();

        // Set a listener to transition to the next activity after the video finishes
        videoView.setOnCompletionListener(mp -> {
            Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        });
       /* if(firstRun==false)//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
            Intent i=new Intent(MainActivity.this, DisclaimerActivity.class);
            new Handler().postDelayed(new Runnable()
            {@Override public void run() {
                startActivity(i);
            }},5000);
        }
        else
        {*/


        //Image View Code
        /*Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
        new Handler().postDelayed(new Runnable()
        {@Override public void run() {
            startActivity(i);
        }},5000);*/
    }
}
