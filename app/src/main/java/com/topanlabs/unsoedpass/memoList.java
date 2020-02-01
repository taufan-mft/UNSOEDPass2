package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelasRepository;
import com.topanlabs.unsoedpass.memo.memoModel;
import com.topanlabs.unsoedpass.memo.memoent;
import com.topanlabs.unsoedpass.memo.memorepo;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class memoList extends AppCompatActivity {
    RecyclerView recyclerView;
    List<kelasModel> matkul;
    kelasInt kelasService;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String tokenkita, kodekelas, nim;
    mahaint mahaint;
    List<memoent>  kelaspenggantis;
    memorepo repo;
    Boolean ketuakelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        repo= new memorepo(getApplication());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(memoList.this);
        recyclerView.setLayoutManager(layoutManager);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation);
        recyclerView.setLayoutAnimation(controller);
        mSettings = getSharedPreferences("Settings", 0);
        ketuakelas = mSettings.getBoolean("isKetuaKelas",false);
        kodekelas = mSettings.getString("kodekelas", "0");
        nim = mSettings.getString("nim", "nim");
        editor = mSettings.edit();
        tokenkita = mSettings.getString("token","0");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build()).build();
        mAdView.loadAd(adRequest);
        final String BASE_URL = "https://api1.myunsoed.com";
        getSupportActionBar().setTitle("Memo Kelas");
        getSupportActionBar().setSubtitle(kodekelas);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        kelasService =
                retrofit.create(kelasInt.class);
        Call<List<memoModel>> call = kelasService.getMemo(tokenkita,kodekelas);
        call.enqueue(new Callback<List<memoModel>>() {
            @Override
            public void onResponse(Call<List<memoModel>> call, Response<List<memoModel>> response) {
                if (response.code() == 200) {
                    List<memoModel> matkul = response.body();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            repo.nukeTable();
                            if (!matkul.isEmpty()) {
                                for (int i = 0; i < matkul.size(); i++) {

                                    //repo.insert(new memoent(0, matkul.get(i).getNamatkul(), matkul.get(i).getJam(), matkul.get(i).getRuangan(), matkul.get(i).getTanggal()));
                                }
                                kelaspenggantis = repo.getKelas();

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        kelasAdapter adapter = new kelasAdapter(kelaspenggantis, getApplicationContext());
                                        recyclerView.setAdapter(adapter);

                                        adapter.notifyDataSetChanged();
                                        recyclerView.scheduleLayoutAnimation();
                                    }
                                });
                                //Log.d("raisan", mahasiswaArrayList.toString());

                            }
                        }
                    });
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(memoList.this);
                    alertDialogBuilder.setTitle("Kelas sudah dihapus.");
                    alertDialogBuilder
                            //.setMessage(pesan)
                            //.setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editor.putString("kodekelas", "0");
                                    editor.apply();
                                    mahasis mahasiw = new mahasis(null,null,null,null, null, null, null, "0", null, null, null);
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
                                    });
                                    Intent i = new Intent(kelasPengganti.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            repo.nukeTable();
                                        }
                                    });

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }



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
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        kelaspenggantis = repo.getKelas();

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                kelasAdapter adapter = new kelasAdapter(kelaspenggantis,getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                recyclerView.scheduleLayoutAnimation();
                            }
                        });
                        //Log.d("raisan", mahasiswaArrayList.toString());

                    }

                });


            }

            //showDialog();
        });
    }
}
