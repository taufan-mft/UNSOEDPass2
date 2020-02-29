package com.topanlabs.unsoedpass.teammaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.topanlabs.unsoedpass.R;

import java.io.Serializable;
import java.util.ArrayList;

public class inputteam extends AppCompatActivity {
MaterialButton btnTambah, btnReset, btnBuat;
EditText edtAng, edtJum;
TextView txtv;
ListView lview;
    ArrayList<String> namaorang;
    String placeholder = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputteam);
        namaorang = new ArrayList<String>();
btnTambah = findViewById(R.id.button);
btnReset = findViewById(R.id.button2);
btnBuat = findViewById(R.id.button3);
edtAng = findViewById(R.id.editText);
edtJum = findViewById(R.id.editText2);
lview = findViewById(R.id.lview);
txtv = findViewById(R.id.textViewjum);
btnTambah.setOnClickListener((View v) -> {
            namaorang.add(edtAng.getText().toString());
            for (int i = 0; i < namaorang.size(); i++) {
                placeholder = placeholder + namaorang.get(i) +"\n";
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.openheart, R.id.textView2,
                    namaorang);
            lview.setAdapter(adapter);
            edtAng.setText("");
            txtv.setText("Jumlah: "+ Integer.toString(namaorang.size()));
        });


btnBuat.setOnClickListener((View v) -> {
    Intent i = new Intent(this, teamResult.class);
    i.putExtra("anggota", (Serializable) namaorang);
    Log.d("NABILAA",String.valueOf((Serializable) namaorang));
    i.putExtra("jumlahgrup", edtJum.getText().toString());
    startActivity(i);
});



    }
}
