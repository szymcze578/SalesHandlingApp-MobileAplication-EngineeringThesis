package com.example.saleshandlingapp.SMSServices;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class SMSSender {

    public static void sendAdd(String phoneNumber, String message){
        SmsManager manager = SmsManager.getDefault();

        ArrayList<String> messageParts = manager.divideMessage(message);
        manager.sendMultipartTextMessage(phoneNumber,
                null,
                messageParts,
                null,
                null);
    }
}
