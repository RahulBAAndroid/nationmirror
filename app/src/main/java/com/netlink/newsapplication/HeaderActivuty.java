package com.netlink.newsapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HeaderActivuty extends Activity {
   static ImageView imgProfile,imgFullVideo,imgNotification,menuButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
         imgProfile= findViewById(R.id.profileImage);
         imgFullVideo= findViewById(R.id.fullvideo);
         imgNotification= findViewById(R.id.notification);
        menuButton = findViewById(R.id.menuButton);
        // Set click listener for menu button
        menuButton.setOnClickListener(this::showPopupMenu);
    }
    public static Fragment getProfileFragment()
    {

       return new ProfileFragment();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
       if(item.getItemId()==R.id.whatsapp)
       {
           openLink("https://api.whatsapp.com/send?text=About%20Us%20https%3A%2F%2Fnationmirror.com%2Fabout-us%2F");
           return true;}
        if(item.getItemId()==R.id.twitter)
        {
            openLink("https://x.com/intent/post?text=About+Us&url=https%3A%2F%2Fnationmirror.com%2Fabout-us%2F");
            return true;}
        if(item.getItemId()==R.id.telegram)
        {
            openLink("https://telegram.me/share/url?url=https%3A%2F%2Fnationmirror.com%2Fabout-us%2F&text=About%20Us");
            return true;}
        if(item.getItemId()==R.id.instagram)
        {
            openLink("https://www.instagram.com/nationmirror/");
            return true;}
       else
           return  false;
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
}
