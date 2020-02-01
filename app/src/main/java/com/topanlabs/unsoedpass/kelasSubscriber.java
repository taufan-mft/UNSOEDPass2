package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class kelasSubscriber extends AppCompatActivity {
Button btnCari, btnGabung;
EditText txtKodeKelas;
TextView txtkode2, txtketua;
String kode, nim;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    kelasInt kelasService;
    mahaint mahaint;
    String tokenkita;
    kelasModel matkul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas_subscriber);
        btnCari = findViewById(R.id.btnCari);
        btnGabung = findViewById(R.id.btnGabung);
        txtKodeKelas= findViewById(R.id.txtKodekelas);
        txtkode2 = findViewById(R.id.txtKelas);
        txtketua=findViewById(R.id.txtKetua);
        kode = txtKodeKelas.toString();

        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        tokenkita = mSettings.getString("token","0");
        nim = mSettings.getString("nim", "nim");
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
       // matkul = new kelasModel("0","0","0");
        kelasService =
                retrofit.create(kelasInt.class);
        mahaint = retrofit.create(mahaint.class);


        btnGabung.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(kelasSubscriber.this);
                alertDialogBuilder.setTitle("Gabung ke kelas?");
                alertDialogBuilder
                        //.setMessage(pesan)
                        //.setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                            editor.putString("kodekelas", matkul.getKodekelas());
                            editor.apply();
                            mahasis mahasiw = new mahasis(null,null,null,null, null, null, null, matkul.getKodekelas(), null, null,null);
                                Call<Void> call = mahaint.gantiKodekel(nim,mahasiw,tokenkita );
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {



                                        Log.d("raisan", "updatecuy");
                                        //adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                        Context context = getApplicationContext();
                                        CharSequence text = "Error TL12";
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();

                                    }

                                    //showDialog();
                                });
                            Intent i = new Intent(kelasSubscriber.this, kelasPengganti.class);
                            startActivity(i);
                            finish();
                            }
                        })
                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // jika tombol diklik, maka akan menutup activity ini
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        btnCari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               kode = txtKodeKelas.getText().toString();
                Call<kelasModel> call = kelasService.cariKelas(kode);
                call.enqueue(new Callback<kelasModel>() {
                    @Override
                    public void onResponse(Call<kelasModel> call, Response<kelasModel> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {
                             matkul = response.body();
                            txtkode2.setVisibility(v.VISIBLE);
                            txtketua.setVisibility(v.VISIBLE);
                            txtkode2.setText("Kode kelas: "+matkul.getKodekelas());
                            txtketua.setText("Ketua kelas: " +matkul.getKetuakelas());
                            btnGabung.setVisibility(v.VISIBLE);
                        } else {
                            txtkode2.setVisibility(v.VISIBLE);
                            txtkode2.setText("Kelas tidak ditemukan");
                            txtketua.setVisibility(v.INVISIBLE);
                            btnGabung.setVisibility(v.INVISIBLE);
                        }



                        //Log.d("raisan", mahasiswaArrayList.toString());
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<kelasModel> call, Throwable t) {

                        Context context = getApplicationContext();
                        CharSequence text = "Error TL12";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }

                    //showDialog();
                });
            }
        });
    }
}
