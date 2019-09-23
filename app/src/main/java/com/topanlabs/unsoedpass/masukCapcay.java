package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class masukCapcay extends AppCompatActivity {
    Button btnRefresh, btnLanjut;
    EditText edtCapcay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk_capcay);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnLanjut = findViewById(R.id.btnLanjut);
        edtCapcay = findViewById(R.id.edtCapcay);
    }
}
