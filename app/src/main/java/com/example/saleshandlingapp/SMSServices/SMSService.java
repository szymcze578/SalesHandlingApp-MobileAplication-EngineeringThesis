package com.example.saleshandlingapp.SMSServices;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.saleshandlingapp.SMSServices.SMSReceiver;

public class SMSService extends Service{

    private SMSReceiver receiver = new SMSReceiver();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(receiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
