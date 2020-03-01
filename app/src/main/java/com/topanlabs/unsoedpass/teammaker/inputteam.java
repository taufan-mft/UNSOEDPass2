package com.topanlabs.unsoedpass.teammaker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.topanlabs.unsoedpass.R;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class inputteam extends AppCompatActivity {
MaterialButton btnTambah, btnReset, btnBuat;
EditText edtAng, edtJum;
TextView txtv;
ListView lview;
    ArrayList<String> namaorang;
    String placeholder = "";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Gson gson;
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
        getSupportActionBar().setTitle("Team Maker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
gson = new Gson();
        mSettings = getSharedPreferences("Settings",0);
        editor= mSettings.edit();
        String data = mSettings.getString("timMember", "no");
        Boolean firstTeam = true;
        firstTeam = mSettings.getBoolean("firstTeam", true);
        if (firstTeam){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(inputteam.this, R.style.AlertDialogTheme2);
            alertDialogBuilder.setTitle("Selamat datang di Team Maker!");
            alertDialogBuilder
                    .setMessage("Di sini, kamu bisa membuat tim secara acak dari anggota yang ada. Berguna untuk membuat tim presentasi, makalah, tugas, dan sebagainya. Mulai dengan menambahkan daftar anggota, kemudian masukkan jumlah grup, lalu tap Buat Team. Jika ingin menghapus salah satu anggota, tap nama anggota tersebut. Tap Reset untuk menghapus semua anggota. Daftar anggota akan otomatis disimpan sehingga kamu gaperlu menulis lagi dikemudian hari.")
                    //.setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Aku Mengerti", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            editor.putBoolean("firstTeam", false);
            editor.apply();
        }
        if (!data.equals("no")) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            namaorang  = gson.fromJson(data, type);
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.openheart, R.id.textView2,
                    namaorang);
            lview.setAdapter(adapter);
            txtv.setText("Jumlah: "+ Integer.toString(namaorang.size()));
        }

btnReset.setOnClickListener((View v) -> {
    namaorang = new ArrayList<String>();
    ArrayAdapter adapter = new ArrayAdapter<String>(this,
            R.layout.openheart, R.id.textView2,
            namaorang);
    lview.setAdapter(adapter);
    txtv.setText("Jumlah: "+ Integer.toString(namaorang.size()));
    editor.putString("timMember", "no");
    editor.apply();

        });
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
    String json = gson.toJson(namaorang);
    editor.putString("timMember", json);
    editor.apply();
        });


btnBuat.setOnClickListener((View v) -> {
    if (edtJum.getText().toString().equals("") || namaorang.size() == 0) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(inputteam.this, R.style.AlertDialogTheme2);
        alertDialogBuilder.setTitle("Ada yang belum diisi");
        alertDialogBuilder
                .setMessage("Pastiin anggota sama jumlah grup ngga kosong.")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return;

    }
    Intent i = new Intent(this, teamResult.class);
    i.putExtra("anggota", (Serializable) namaorang);
    Log.d("NABILAA",String.valueOf((Serializable) namaorang));
    String json = gson.toJson(namaorang);
    editor.putString("timMember", json);
    editor.apply();
    i.putExtra("jumlahgrup", edtJum.getText().toString());
    startActivity(i);
});

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(inputteam.this, R.style.AlertDialogTheme2);
                alertDialogBuilder.setTitle("Hapus "+namaorang.get(position)+"?");
                alertDialogBuilder
                        //.setMessage()
                        //.setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                namaorang.remove(position);
                                ArrayAdapter adapter = new ArrayAdapter<String>(inputteam.this,
                                        R.layout.openheart, R.id.textView2,
                                        namaorang);
                                lview.setAdapter(adapter);
                                dialog.cancel();
                                txtv.setText("Jumlah: "+ Integer.toString(namaorang.size()));
                                String json = gson.toJson(namaorang);
                                editor.putString("timMember", json);
                                editor.apply();
                            }
                        })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    }
                })
                ;
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });
            }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }


