package com.farodig.transactionsmsreceiveservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    final String APP_TAG = "SmsReceiveService";
    final int SMS_RECEIVER_SERVICE_REQUEST_RECEIVE_SMS = 1;
    final int SMS_RECEIVER_SERVICE_REQUEST_READ_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestPermissions(Manifest.permission.RECEIVE_SMS, SMS_RECEIVER_SERVICE_REQUEST_RECEIVE_SMS);
        RequestPermissions(Manifest.permission.READ_SMS, SMS_RECEIVER_SERVICE_REQUEST_READ_SMS);
    }


    private void RequestPermissions(String receiveSms, int code) {


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, receiveSms ) != PackageManager.PERMISSION_GRANTED)
        {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, code);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }


    public void onClickStart(View view)
    {
        this.startService(new Intent(this, SmsReceiveService.class));
        //this.startService(new Intent( this, SmsReceiverBroadcast.class));
    }

    public void onClickStop(View view)
    {
        this.stopService(new Intent(this, SmsReceiveService.class));
        //this.stopService(new Intent(this, SmsReceiverBroadcast.class));
    }
}
