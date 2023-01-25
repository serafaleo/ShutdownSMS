package com.serafaleo.shutdownsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        if(preferences.getBoolean("toggle_btn", false))
        {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            {
                Toast.makeText(context, "SMS Received!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
