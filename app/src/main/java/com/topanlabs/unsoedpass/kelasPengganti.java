package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.topanlabs.unsoedpass.kelaspenggantidb.kelasRepository;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelaspengganti;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class kelasPengganti extends AppCompatActivity {
    RecyclerView recyclerView;
    List<kelasModel> matkul;
    kelasInt kelasService;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String tokenkita;
    List<kelaspengganti>  kelaspenggantis;
    kelasRepository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas_pengganti);
        repo= new kelasRepository(getApplication());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(kelasPengganti.this);
        recyclerView.setLayoutManager(layoutManager);
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        tokenkita = mSettings.getString("token","0");
        final String BASE_URL = "http://10.10.10.8:8000";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        kelasService =
                retrofit.create(kelasInt.class);

        Call<List<kelasModel>> call = kelasService.getKelas(tokenkita,"DIANIS");
        call.enqueue(new Callback<List<kelasModel>>() {
            @Override
            public void onResponse(Call<List<kelasModel>> call, Response<List<kelasModel>> response) {
                List<kelasModel> matkul= response.body();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        repo.nukeTable();
                        for (int i = 0; i < matkul.size(); i++) {

                            repo.insert(new kelaspengganti(0, matkul.get(i).getNamatkul(), matkul.get(i).getJam(), matkul.get(i).getRuangan(), matkul.get(i).getTanggal()));
                        }
                            kelaspenggantis = repo.getKelas();

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    kelasAdapter adapter = new kelasAdapter(kelaspenggantis,getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            //Log.d("raisan", mahasiswaArrayList.toString());

                        }

                });



                //Log.d("raisan", mahasiswaArrayList.toString());
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<kelasModel>> call, Throwable t) {

                Context context = getApplicationContext();
                CharSequence text = "Error TL12";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });
    }
}
