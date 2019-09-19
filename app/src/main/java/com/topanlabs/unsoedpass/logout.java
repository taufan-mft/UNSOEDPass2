package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class logout extends AppCompatActivity {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
        editor.putString("logged", "ya");
        editor.apply();
        Intent i = new Intent(this, login.class);
        startActivity(i);
        finish();

    }
}
