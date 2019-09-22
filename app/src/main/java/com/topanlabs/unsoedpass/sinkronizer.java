package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Map;

public class sinkronizer extends AppCompatActivity {
    Map<String, String> kukis;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkronizer);
        kukis = (Map) getIntent().getSerializableExtra("kukis");
    }
}
