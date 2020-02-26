package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class memoDetail extends AppCompatActivity {
    SharedPreferences mSettings;
String matkul, jenis, tanggal, jam,  ruangan, catatan, id, tokenkita;
TextView txtMatkul, txtJenis, txtTanggal, txtJam, txtRuangan, txtCatatan;
    kelasInt kelasService;
    Boolean ketuakelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        txtMatkul = findViewById(R.id.txtMatkul);
        txtJenis = findViewById(R.id.txtJenis);
        txtTanggal= findViewById(R.id.txtTanggal);
        txtJam = findViewById(R.id.txtJam);
        txtRuangan = findViewById(R.id.txtRuangan);
        txtCatatan = findViewById(R.id.txtCatatan);
        mSettings = getSharedPreferences("Settings", 0);
        tokenkita = mSettings.getString("token","0");
        ketuakelas = mSettings.getBoolean("isKetuaKelas",false);
        matkul= getIntent().getStringExtra("matkul");
        jenis = getIntent().getStringExtra("jenis");
        tanggal= getIntent().getStringExtra("tanggal");
        jam = getIntent().getStringExtra("jam");
        ruangan = getIntent().getStringExtra("ruangan");
        catatan = getIntent().getStringExtra("catatan");
        id = getIntent().getStringExtra("id");
        getSupportActionBar().setTitle("Detail Memo");
        txtMatkul.setText(matkul);
        txtJenis.setText(jenis);
        txtTanggal.setText("Tanggal: "+tanggal);
        txtJam.setText("Jam: "+jam);
        txtRuangan.setText("Ruangan: "+ruangan);
        txtCatatan.setText("Catatan:\n"+catatan);
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        kelasService = retrofit.create(kelasInt.class);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (ketuakelas) {
            inflater.inflate(R.menu.menumemoview_ketua, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_hapus:
hapusMemo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void hapusMemo() {

        Call<Void> call = kelasService.deleteMemo(tokenkita,id);
        Log.d("zhafarin", id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Context context = getApplicationContext();
                CharSequence text = "Memo sudah dihapus";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Context context = getApplicationContext();
                CharSequence text = "Error TL12";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
    });
}
}
