package com.netlink.newsapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class LoginAcctivity extends Activity {
    Dialog mProgressDialog;
    public static final String SHARED_PREF = "sharedPrefs";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_login);
        checkLogin();
        Button btnLogin = findViewById(R.id.btnLogin);
        EditText etEmail = findViewById(R.id.etEmail);
        String email = etEmail.getText().toString();
        EditText etPassword = findViewById(R.id.etPassword);
        String password = etPassword.getText().toString();
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginAcctivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
              DatabaseReference reference = database.getReference("users");
                Query q = reference.orderByChild("email").equalTo(etEmail.getText().toString());
                 q.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if (snapshot.exists()) {
                             for (DataSnapshot d : snapshot.getChildren()) {
                                 String retrievedEmail = d.child("email").getValue(String.class);
                                 Log.d("DEBUG", "Retrieved Email: " + retrievedEmail);
                                 String spassword = d.child("password").getValue(String.class);
                                 Log.d("DEBUG", "Stored Password: " + spassword);
                                 if (spassword != null && spassword.equals(etPassword.getText().toString())) {
                                     SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                                     SharedPreferences.Editor editor = sharedPreferences.edit();
                                     editor.putString("login","true");
                                     editor.apply();
                                     dismissProgress();
                                     Intent i = new Intent(LoginAcctivity.this, DashBoardActivity.class);
                                     i.putExtra("login","true");
                                     i.putExtra("email",retrievedEmail);
                                     startActivity(i);
                                     finish(); // Optionally finish the login activity
                                 } else {
                                     dismissProgress();
                                     Toast.makeText(LoginAcctivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         }
                         else {
                             dismissProgress();
                             Toast.makeText(LoginAcctivity.this, "User not found", Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {
                         dismissProgress();
                         Toast.makeText(LoginAcctivity.this, "Someting Went Wrong", Toast.LENGTH_SHORT).show();
                     }
                 });

            }
        });
        findViewById(R.id.btnLoginMobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginAcctivity.this,LoginViaPhoneActivity.class);
                startActivity(i);
            }
        });
    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        String check = sharedPreferences.getString("login","");
        if(check.equals("true"))
        {
//          Toast.makeText(this, "Already Login  ", Toast.LENGTH_LONG).show();
//            Log.d("loginCheck", "checkLogin: "+name + age +gender+dist+cast+labourType+mobile_number+registrationStatus);
            Intent i = new Intent(getBaseContext(),DashBoardActivity.class);
            startActivity(i);
        }
    }
    public void showProgress() {
        try {
            if (isProgressShowing())
                dismissProgress();
            mProgressDialog = new Dialog(LoginAcctivity.this);
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
