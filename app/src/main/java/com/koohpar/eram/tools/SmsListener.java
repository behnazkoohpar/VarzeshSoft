package com.koohpar.eram.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.koohpar.eram.activities.RecieveSmsCodeActivity;

/**
 * Created by Behnaz on 06/04/2017.
 */
public class SmsListener extends BroadcastReceiver {
    public final static String serverNo2 = "+9810002000002200";

    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        String sender = "";
        StringBuilder text = new StringBuilder();
        // get sender from first PDU
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        String phoneNumber = shortMessage.getDisplayOriginatingAddress();
        String message = shortMessage.getDisplayMessageBody();
        if (phoneNumber.contains(serverNo2)) {
            sender = shortMessage.getOriginatingAddress();
            for (int i = 0; i < pdus.length; i++) {
                shortMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                text.append(shortMessage.getDisplayMessageBody());
                RecieveSmsCodeActivity.codeSmsRecieved = shortMessage.getDisplayMessageBody().substring(shortMessage.getDisplayMessageBody().length() - 6, shortMessage.getDisplayMessageBody().length());
            }
        }
    }
}