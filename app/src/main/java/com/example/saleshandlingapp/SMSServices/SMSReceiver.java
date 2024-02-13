package com.example.saleshandlingapp.SMSServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Message;
import com.example.saleshandlingapp.Enum.OrderType;
import com.example.saleshandlingapp.NotificationService.NotificationSender;

import java.time.LocalDateTime;

public class SMSReceiver extends BroadcastReceiver {

    private static SMSReceiverListener smsReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] smsObj = (Object[]) data.get("pdus");

        for(Object obj : smsObj){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            if(checkMessage(smsMessage.getDisplayMessageBody())){
                createMessage(context,smsMessage);
            }
        }
    }

    private boolean checkMessage(String message){
        return message.contains(OrderType.ORDER.getDescription()) || message.contains(OrderType.DELIVERY.getDescription());
    }

    private void createMessage(Context context, SmsMessage smsMessage){
        String number = smsMessage.getDisplayOriginatingAddress();
        String message = smsMessage.getDisplayMessageBody();
        LocalDateTime time = LocalDateTime.now();

        Message messageObject = new Message(message,number,time);
        Database.getInstance(context).messageDao().insertAll(messageObject);
        Toast.makeText(context,"Przechwycono nowe zlecenie!",Toast.LENGTH_LONG).show();
        smsReceiverListener.onSMSReceived();
        NotificationSender.makeNotification(context);
    }

    public static void setSMSReceivedListener(SMSReceiverListener listener) {
         smsReceiverListener = listener;
    }
}
