package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Executors;

public class logout extends AppCompatActivity {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    matkulRepository matkulRepository;
    absenRepository absenRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        mSettings = getSharedPreferences("Settings",0);
        matkulRepository = new matkulRepository(getApplication());
        absenRepository = new absenRepository(getApplication());
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Log.d("rairai","disinii");
                matkulRepository.nukeTable();
                absenRepository.nukeTable();
            }
        });
        editor = mSettings.edit();
        editor.putString("logged", "ya");
        editor.apply();
        Intent i = new Intent(this, login.class);
        startActivity(i);
        finish();

    }
}
