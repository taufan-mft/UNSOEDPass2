package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class sinkronisasi extends AppCompatActivity {
    Button btnSimpan, btnMin, btnPlus;
    TextView txthari;
    Integer hari;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkronisasi);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnMin = findViewById(R.id.btnMin);
        btnPlus = findViewById(R.id.btnPlus);
        txthari = findViewById(R.id.txtHari);
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();

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
                }


        });
    }
}
