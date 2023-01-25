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
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file),
                                                                     Context.MODE_PRIVATE);

        if(preferences.getBoolean(context.getString(R.string.toggle_btn_preference), false))
        {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            {
                Toast.makeText(context, "SMS Received!", Toast.LENGTH_SHORT).show();

                // TODO: If we get here and "su" fails, it means that it got uninstalled after the last
                // time the app was open. We need to set the preference to false.
            }
        }
    }
}
