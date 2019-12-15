package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.WordUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class kelasSelector extends AppCompatActivity {
CardView kelasbaru, gabungkelas;
    kelasInt kelasService;
    kelasModel matkul;
    String tokenkita, nim;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Boolean ketuakelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas_selector);
        kelasbaru = findViewById(R.id.cvkelasBaru);
        gabungkelas = findViewById(R.id.cvgabungkelas);

        final String kode = RandomStringUtils.random(6,true,false).toUpperCase();
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        ketuakelas = mSettings.getBoolean("isKetuaKelas",false);
        tokenkita = mSettings.getString("token","token");
        String kodekel = mSettings.getString("kodekelas","0");
        if (!kodekel.equals("0")) {
            Intent i = new Intent(kelasSelector.this, kelasPengganti.class);
            startActivity(i);
            finish();
            return;
        }
        nim = mSettings.getString("nim","nim");
        Log.d("raisani", kode);
        final String BASE_URL = "http://10.10.10.8:8000";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        // matkul = new kelasModel("0","0","0");
        kelasService =
                retrofit.create(kelasInt.class);
        kelasbaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(kelasSelector.this);
                alertDialogBuilder.setTitle("Buat kelas baru?");
                alertDialogBuilder
                        //.setMessage(pesan)
                        //.setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                matkul = new kelasModel(kode,nim,"now");
                                Call<kelasModel> call = kelasService.buatKelas(tokenkita,matkul);
                                call.enqueue(new Callback<kelasModel>() {
                                    @Override
                                    public void onResponse(Call<kelasModel> call, Response<kelasModel> response) {
                                        int statusCode = response.code();
                                        if (statusCode == 201) {
                                            Intent i = new Intent(kelasSelector.this, kelasPengganti.class);
                                            editor.putString("kodekelas", kode);
                                            editor.putBoolean("isKetuaKelas", true);
                                            editor.apply();
                                                    startActivity(i);
                                            finish();

                                        } else {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Gagal membuat kelas", Toast.LENGTH_SHORT);
                                            toast.show();
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

    }
}
