package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.WordUtils;

public class kelasSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas_selector);

        String kode = RandomStringUtils.random(6,true,false);
        kode = WordUtils.capitalize(kode);
        Log.d("raisani", kode);
    }
}
