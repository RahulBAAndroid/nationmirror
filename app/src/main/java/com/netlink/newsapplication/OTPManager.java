package com.netlink.newsapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class OTPManager {


        private String phoneNumber;
        private String otp;
        private CountDownTimer timer;
    private Context context;

    public OTPManager(Context context, String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
    }

        public void sendOTP() {
            // Generate a random 4-digit OTP
            otp = String.valueOf((int) (Math.random() * 9000) + 1000);
            // Create the PendingIntent
            PendingIntent sentIntent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_UPDATE_CURRENT);
            }
            // Send the OTP to the phone number using SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "Your OTP is: " + otp, sentIntent, null);
            Log.d("OTP Manager", "sendOTP: Your OTP is:"+otp + "and mobile number is "+phoneNumber);
            // Start the timer for OTP expiration
            startTimer(60 * 1000*2); // 2 minute
        }

        public void verifyOTP(String enteredOTP) {
            if (otp.equals(enteredOTP)) {
                System.out.println("OTP verified successfully!");
            } else {
                System.out.println("Invalid OTP. Please try again.");
            }
        }

        public void resendOTP() {
            sendOTP();
        }

        private void startTimer(long duration) {
            timer = new CountDownTimer(duration, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("OTP expires in " + millisUntilFinished / 1000 + " seconds");
                }

                @Override
                public void onFinish() {
                    System.out.println("OTP expired. Please resend.");
                }
            }.start();
        }

}
