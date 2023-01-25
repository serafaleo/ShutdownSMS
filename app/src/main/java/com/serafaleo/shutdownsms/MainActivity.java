package com.serafaleo.shutdownsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity
{
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

        SharedPreferences preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        ToggleButton toggle_btn = (ToggleButton)findViewById(R.id.toggle_btn);
        toggle_btn.setOnClickListener(view ->
        {
            SharedPreferences.Editor preferences_editor = preferences.edit();
            preferences_editor.putBoolean("toggle_btn", toggle_btn.isChecked());
            preferences_editor.apply();
        });

        toggle_btn.setChecked(preferences.getBoolean("toggle_btn", false));
    }
}
