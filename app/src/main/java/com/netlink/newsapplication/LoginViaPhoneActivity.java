package com.netlink.newsapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginViaPhoneActivity extends Activity {
    EditText etMobileNumber;
    ProgressDialog mProgressDialog;
    String mobileNumber;
    Button btnSendOtp;
    private String verificationId;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginvia_mobile);
        init();
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber = etMobileNumber.getText().toString();
                /*OTPManager otpManager = new OTPManager(getApplicationContext(),"7566880933");
                otpManager.sendOTP();*/
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");
                Query q = reference.child(etMobileNumber.getText().toString());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            showProgress();
                            sendOTP();
                            dismissProgress();
                            showOtpDialog();
                        }
                        else
                        { Toast.makeText(LoginViaPhoneActivity.this, "Nt a User Please Register", Toast.LENGTH_SHORT).show();}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginViaPhoneActivity.this, "Nt a User Please Register", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    void init()
    {
        etMobileNumber = findViewById(R.id.etMobile);
        btnSendOtp = findViewById(R.id.btnSendOtp);

    }
    void sendOTP()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+etMobileNumber.getText().toString().trim(),
                60, TimeUnit.SECONDS,
                LoginViaPhoneActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


//                       Toast.makeText(LoginActivity.this, "Sending Message", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
dismissProgress();
                        Toast.makeText(LoginViaPhoneActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Button", "onVerificationFailed: "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                   /*     FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        // set this to remove reCaptcha web
                        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);*/
//                        Toast.makeText(LoginActivity.this, "Sent Sucessfull", Toast.LENGTH_SHORT).show();
                        LoginViaPhoneActivity.this.verificationId=verificationId;
                    }
                }
        );
    }
    void verifyOTP(String otp)
    {

        /*String code = inputCode1.getText().toString() +
                inputCode2.getText().toString() +
                inputCode3.getText().toString() +
                inputCode4.getText().toString() +
                inputCode5.getText().toString() +
                inputCode6.getText().toString() ;*/
        String code = otp;
        if(verificationId!=null)
        {
            PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                    verificationId,
                    code
            );
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                dismissProgress();
                               Intent i = new Intent(getApplicationContext(),DashBoardActivity.class);
                               startActivity(i);
                                alertDialog.dismiss(); // Close the dialog after verification
                            }
                            else {
                                dismissProgress();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginViaPhoneActivity.this);
                                builder.setMessage("Please check the OTP")
                                        .setTitle("Wrong OTP")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                               AlertDialog ad = builder.create();
                               ad.show();
                            }
                        }
                    });
        }

    }
    void showOtpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginViaPhoneActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);

        EditText etOtp = dialogView.findViewById(R.id.etOtp);
        Button btnVerify = dialogView.findViewById(R.id.btnVerify);

        alertDialog = builder.create();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = etOtp.getText().toString().trim();
                showProgress();
                verifyOTP(enteredOtp);
//                alertDialog.dismiss(); // Close the dialog after verification
            }
        });

        alertDialog.show();
    }
    public void showProgress() {
        try {
            if (isProgressShowing())
                dismissProgress();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("PLEASE WAIT USER");
            mProgressDialog.setCancelable(false);
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
