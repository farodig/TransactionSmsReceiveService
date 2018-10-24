package com.farodig.transactionsmsreceiveservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.util.Log;

import com.farodig.transactionsmsreceiveservice.Model.SmsItem;
import com.farodig.transactionsmsreceiveservice.View.CSV;

import java.util.Calendar;
import java.util.Date;


public class SmsReceiverBroadcast extends BroadcastReceiver
{
    final String LOG_TAG = "SmsReceiveService";
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String format = bundle.getString("format");

        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null && pdus.length > 0) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            String message = "";
            Date messageDate = null;

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage partOfMessage;
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    partOfMessage = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    partOfMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                // sberbank phone
                if (partOfMessage.getOriginatingAddress().equals("900"))
                {
                    // get time of receive message
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(partOfMessage.getTimestampMillis());
                    messageDate = calendar.getTime();

                    // get part of message text
                    message += partOfMessage.getMessageBody();
                }
            }

            // Check if sms is transaction
            if (SmsItem.IsValid(message.toLowerCase()) && messageDate != null)
            {
                // parse message
                SmsItem msg = new SmsItem(message, messageDate);
                CSV csv = new CSV();
                csv.SaveCSV(msg);
            }
        }
    }
}
