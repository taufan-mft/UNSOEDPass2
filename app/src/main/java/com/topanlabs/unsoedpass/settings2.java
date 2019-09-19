package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class settings2 extends AppCompatActivity {
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Button logout, btncall, btnkebijakan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logout = findViewById(R.id.btnlogout);
        btncall = findViewById(R.id.btncall);
        btnkebijakan = findViewById(R.id.btnkebijakan);
        btncall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String url = "https://wa.me/62895425455070?text=Aku%20butuh%20bantuan%20mengenai%20MyUNSOED:";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                //Intent i = new Intent(login.this, MainActivity.class);
                //finish();  //Kill the activity from which you will go to next activity
                //startActivity(i);
            }

        });
        btnkebijakan.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                // Code here executes on main thread after user presses button
                                                String url = "https://www.topanlabs.com/unsoed-pass/kebijakan-privasi-dan-keamanan-data/";
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(url));
                                                startActivity(i);

                                                //Intent i = new Intent(login.this, MainActivity.class);
                                                //finish();  //Kill the activity from which you will go to next activity
                                                //startActivity(i);
                                            }
                                        });

        logout.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
            showaDialog("Yakin mau logout?", "Serius ga?");
          }
         });

        getSupportActionBar().setTitle("Settings");
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showaDialog(String title, String pesan){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(settings2.this);

        // set title dialog
        alertDialogBuilder.setTitle(title);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage(pesan)
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini

                            Intent w = new Intent(settings2.this, MainActivity.class);

                            editor.putString("trialwarnabsen", "belum");
                            editor.putString("premium", "tidak");
                            editor.putString("logged", "ya");
                            editor.putString("nim","nim");
                            editor.putBoolean("warntrial3", false);
                            editor.apply();
                        startActivity(w);
                        finish();

                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        dialog.cancel();

                    }
                });



        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
