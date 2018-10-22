package com.farodig.transactionsmsreceiveservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.security.Provider;

/**
 * Created by Maxim on 21.10.2018.
 */

public class SmsReceiveService extends Service
{
    SmsReceiverBroadcast receiver;
    final String LOG_TAG = "SmsReceiveServiceLogs";

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");

        SubscribeBroadcast();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();

        // Unregister the SMS receiver
        if (receiver != null)
            this.unregisterReceiver(receiver);
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void SubscribeBroadcast()
    {
        receiver = new SmsReceiverBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(receiver, filter);
    }
}
