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


public class SmsReceiverBroadcast extends BroadcastReceiver {

    private static final String TAG = SmsReceiverBroadcast.class.getSimpleName();
    public static final String pdu_type = "pdus";

    public SmsReceiverBroadcast()
    {
        //This log will display in the logcat
        Log.d("SmsReceiverBroadcast", "SmsReceiverBroadcast constructor called.");
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive method started !!!!!!!!!!!!!!!!!");

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");

        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] =
                            SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                // sberbank phone
                if (msgs[i].getOriginatingAddress() == "900") {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(msgs[i].getTimestampMillis());

                    String message = msgs[i].getMessageBody();
                    Date time = calendar.getTime();
                    if (IsValid(message)) {
                        SmsItem msg = new SmsItem(message, time);
                        CSV csv = new CSV();
                        csv.SaveCSV(msg);
                        Log.d("SmsReceiverBroadcast", "Message is saved.");
                        //Parse(message, time);
                    } else {
                        Log.d("SmsReceiverBroadcast", "Message is not valid.");
                    }
                }
            }
        }
    }

    /*
    Check if sms is transaction
     */
    private boolean IsValid(String message)
    {
        if (message.contains("кредит")) return false;
        if (message.contains("отказ покупка")) return false;
        if (message.contains("карта")) return false;
        if (message.contains("карту")) return false;
        if (message.contains("картой")) return false;
        if (message.contains("карте")) return false;
        if (message.contains("перевод")) return false;
        if (message.contains("откройте")) return false;
        if (message.contains("оформите")) return false;
        if (message.contains("отправьте")) return false;
        if (message.contains("обратитесь")) return false;
        if (message.contains("не сообщайте")) return false;
        if (message.contains("встречайте")) return false;
        if (message.contains("можете")) return false;
        if (message.contains("с днем рождения")) return false;
        if (message.contains("запрос на регистрацию")) return false;
        if (message.contains("вход")) return false;

        return true;
    }

    private void Parse(String message, Date DateTime)
    {
        // Parsed SMS
        SmsItem msg = new SmsItem(message, DateTime);
        //mess
    }
}
