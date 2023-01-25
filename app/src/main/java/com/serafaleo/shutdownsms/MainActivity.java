package com.serafaleo.shutdownsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private SharedPreferences preferences;
    private ToggleButton toggle_btn;
    private EditText edit_sms_text;
    private Button confirm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
           != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }

        preferences = getSharedPreferences(getString(R.string.preferences_file), MODE_PRIVATE);
        toggle_btn = (ToggleButton)findViewById(R.id.toggle_btn);
        edit_sms_text = (EditText)findViewById(R.id.edit_sms_text);
        confirm_btn = (Button)findViewById(R.id.confirm_edit_btn);

        String sms_text = preferences.getString(getString(R.string.sms_text_preference), getString(R.string.undefined_sms_text));

        if(sms_text.equals(getString(R.string.undefined_sms_text)))
        {
            StringBuilder str_builder = new StringBuilder();
            Random rand = new Random();

            String possible_chars = "abcdefghijklmnopqrstuvwxyz";
            possible_chars = possible_chars.concat(possible_chars.toUpperCase());

            final int str_length = 10;
            for(int i = 0; i < str_length; i++)
            {
                int index = rand.nextInt(possible_chars.length());
                str_builder.append(possible_chars.charAt(index));
            }

            sms_text = str_builder.toString();
            storeSMSTextString(sms_text);
        }

        edit_sms_text.setText(sms_text);

        toggle_btn.setOnClickListener(view ->
        {
            if(checkRoot())
            {
                updateToggleButtonPreference();
            }
            else
            {
                uncheckToggleButton();
            }
        });

        confirm_btn.setOnClickListener(view ->
        {
            final String final_sms_text = preferences.getString(getString(R.string.sms_text_preference),
                                                                getString(R.string.undefined_sms_text));

            String entered_text = edit_sms_text.getText().toString();

            if(entered_text.equals(final_sms_text))
            {
                Toast.makeText(this, "SMS text unchanged", Toast.LENGTH_LONG).show();
            }
            else
            {
                storeSMSTextString(entered_text);
                String toast_msg = "SMS text changed from \"" + final_sms_text + "\" to \"" + entered_text + "\"";
                Toast.makeText(this, toast_msg, Toast.LENGTH_LONG).show();
            }
        });

        toggle_btn.setChecked(preferences.getBoolean(getString(R.string.toggle_btn_preference), false));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkRoot();
    }

    private void updateToggleButtonPreference()
    {
        SharedPreferences.Editor preferences_editor = preferences.edit();
        preferences_editor.putBoolean(getString(R.string.toggle_btn_preference), toggle_btn.isChecked());
        preferences_editor.apply();
    }

    private void uncheckToggleButton()
    {
        toggle_btn.setChecked(false);
        updateToggleButtonPreference();
    }

    private void showRootNotInstalled()
    {
        Toast.makeText(this, getString(R.string.root_not_installed_msg), Toast.LENGTH_LONG).show();
    }

    private void showRootDenied()
    {
        Toast.makeText(this, getString(R.string.root_denied_msg), Toast.LENGTH_LONG).show();
    }

    private boolean checkRoot()
    {
        try
        {
            Process exec_process = Runtime.getRuntime().exec(new String[]{"su", "-c", "exit"});
            BufferedReader error_stream_reader = new BufferedReader(new InputStreamReader(exec_process.getErrorStream()));

            StringBuilder str_builder = new StringBuilder();
            String line;
            while((line = error_stream_reader.readLine()) != null)
            {
                str_builder.append(line);
            }

            if(!str_builder.toString().equals(getString(R.string.permission_denied_text)))
            {
                return true;
            }
            else
            {
                showRootDenied();
                uncheckToggleButton();
            }
        }
        catch(IOException e)
        {
            showRootNotInstalled();
            uncheckToggleButton();
        }
        catch(SecurityException e)
        {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void storeSMSTextString(String to_store)
    {
        SharedPreferences.Editor preferences_editor = preferences.edit();
        preferences_editor.putString(getString(R.string.sms_text_preference), to_store);
        preferences_editor.apply();
    }
}
