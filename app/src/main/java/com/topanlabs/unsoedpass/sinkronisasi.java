package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Map;

public class sinkronisasi extends AppCompatActivity {
    Button btnSimpan, btnMin, btnPlus;
    TextView txthari, info;
    Integer hari;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Map<String, String> kukis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkronisasi);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnMin = findViewById(R.id.btnMin);
        btnPlus = findViewById(R.id.btnPlus);
        txthari = findViewById(R.id.txtHari);
        info = findViewById(R.id.textView10);
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        final Intent intent = getIntent();
        if(intent.hasExtra("kukis")) {
            kukis = (Map) getIntent().getSerializableExtra("kukis");
            info.setText(kukis.toString());
        }
        btnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            hari = Integer.parseInt(txthari.getText().toString());
            hari = hari + 1;
            txthari.setText(hari.toString());
            }

        });

        btnMin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hari = Integer.parseInt(txthari.getText().toString());
                if (hari != 1) {
                    hari = hari - 1;
                    txthari.setText(hari.toString());
                }
            }

        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               editor.putInt("intSinkron", hari);
               editor.apply();
                if(intent.hasExtra("kukis")) {
                    Intent i = new Intent(sinkronisasi.this, sinkronizer.class);
                    i.putExtra("kukis", (Serializable) kukis);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(sinkronisasi.this, masukCapcay.class);
                    startActivity(i);
                    finish();
                }
                }


        });
    }
}
