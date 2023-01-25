package com.serafaleo.shutdownsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SMSBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file),
                                                                     Context.MODE_PRIVATE);

        if(preferences.getBoolean(context.getString(R.string.toggle_btn_preference), false))
        {
            String sms_to_recognize = preferences.getString(context.getString(R.string.sms_text_preference),
                                                            context.getString(R.string.undefined_sms_text));

            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            {
                Bundle bundle = intent.getExtras();
                if(bundle != null)
                {
                    Object[] pdus = (Object[])bundle.get("pdus");
                    String format = bundle.getString("format");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for(int i = 0; i < pdus.length; i++)
                    {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            messages[i] = SmsMessage.createFromPdu((byte[])pdus[i], format);
                        }
                        else
                        {
                            messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        }

                        if(messages[0].getMessageBody().equals(sms_to_recognize))
                        {
                            try
                            {
                                Process exec_process = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot", "-p"});
                                BufferedReader error_stream_reader = new BufferedReader(new InputStreamReader(exec_process.getErrorStream()));

                                StringBuilder str_builder = new StringBuilder();
                                String line;
                                while((line = error_stream_reader.readLine()) != null)
                                {
                                    str_builder.append(line);
                                }

                                if(str_builder.toString().equals(context.getString(R.string.permission_denied_text)))
                                {
                                    setToggleButtonPreferenceFalse(preferences, context);
                                }
                            }
                            catch(IOException e)
                            {
                                setToggleButtonPreferenceFalse(preferences, context);
                            }
                            catch(SecurityException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setToggleButtonPreferenceFalse(SharedPreferences preferences, Context context)
    {
        SharedPreferences.Editor preferences_editor = preferences.edit();
        preferences_editor.putBoolean(context.getString(R.string.toggle_btn_preference), false);
        preferences_editor.apply();
    }
}
