package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.topanlabs.unsoedpass.broadcast.AlarmReceiver;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelasRepository;
import com.topanlabs.unsoedpass.memo.memoModel;
import com.topanlabs.unsoedpass.memo.memoent;
import com.topanlabs.unsoedpass.memo.memorepo;
import com.topanlabs.unsoedpass.memo.memoAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    Context cox;
    List<memoent>  kelaspenggantis;
    memorepo repo;
    Boolean ketuakelas;
    Boolean jalanga = false;
    Integer oldCount, newCount;
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
        kelaspenggantis = new ArrayList<>();
        tokenkita = mSettings.getString("token","0");
        cox = getApplicationContext();
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
                            oldCount = repo.getCount();
                            if (oldCount!= 0){
                                addAlarm(true);
                            }
                            repo.nukeTable();
                            if (!matkul.isEmpty()) {
                                Log.d("zhafarin", "matkul size: "+matkul.size());
                                for (int i = 0; i < matkul.size(); i++) {

                                    repo.insert(new memoent(0, matkul.get(i).getNamatkul(), matkul.get(i).getJam(), matkul.get(i).getRuangan(), matkul.get(i).getTanggal(), matkul.get(i).getCatatan(),matkul.get(i).getJenis(), matkul.get(i).getIdmemo()));

                                }
                                if (kelaspenggantis!=null) {
                                    Log.d("zhafarin","dihapus");
                                kelaspenggantis.clear();}
                                kelaspenggantis = repo.getKelas();
                                newCount = repo.getCount();
                                addAlarm(false);
                                Log.d("zhafarin","lagi "+ kelaspenggantis.size());
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        memoAdapter adapter = new memoAdapter(kelaspenggantis, memoList.this);
                                        recyclerView.setAdapter(adapter);

                                        adapter.notifyDataSetChanged();
                                        recyclerView.scheduleLayoutAnimation();
                                        jalanga = true;
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
                                    Intent i = new Intent(memoList.this, MainActivity.class);
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
            public void onFailure(Call<List<memoModel>> call, Throwable t) {

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
                                memoAdapter adapter = new memoAdapter(kelaspenggantis,memoList.this);
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
    @Override
    public void onResume(){
        super.onResume();
        //updateKelas();
        if(jalanga){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("zhafarin", "onresume");
               updateKelas();
            }
        }, 300);

    }}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (ketuakelas) {
            inflater.inflate(R.menu.menuketua_memo, menu);
        } else{
            inflater.inflate(R.menu.menumemo, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
Log.d("zhafarin", "aku dipencet");
                updateKelas();
                return true;
            case R.id.newMemo:
                Intent i = new Intent(memoList.this, memoAdd.class);
                startActivityForResult(i, 12);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void updateKelas() {
        Call<List<memoModel>> call = kelasService.getMemo(tokenkita,kodekelas);
        call.enqueue(new Callback<List<memoModel>>() {
            @Override
            public void onResponse(Call<List<memoModel>> call, Response<List<memoModel>> response) {
                if (response.code() == 200) {
                    Log.d("zhafarin", "200 disini cuy");
                    List<memoModel> matkul = response.body();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            oldCount = repo.getCount();
                            if (oldCount!= 0){
                                addAlarm(true);
                            }
                            repo.nukeTable();
                            if (!matkul.isEmpty()) {
                                Log.d("zhafarin", "2matkul size: "+matkul.size());
                                for (int i = 0; i < matkul.size(); i++) {

                                    repo.insert(new memoent(0, matkul.get(i).getNamatkul(),
                                            matkul.get(i).getJam(), matkul.get(i).getRuangan(), matkul.get(i).getTanggal(),
                                            matkul.get(i).getCatatan(),matkul.get(i).getJenis(), matkul.get(i).getIdmemo()));

                                }
                                if(kelaspenggantis!=null){
                                    Log.d("zhafarin","2dihapus");
                                    kelaspenggantis.clear();}
                                kelaspenggantis = repo.getKelas();
                                newCount = repo.getCount();
                                addAlarm(false);
                                Log.d("zhafarin","2lagi "+ kelaspenggantis.size());
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        memoAdapter adapter = new memoAdapter(kelaspenggantis, memoList.this);
                                        recyclerView.setAdapter(adapter);

                                        adapter.notifyDataSetChanged();
                                        recyclerView.scheduleLayoutAnimation();
                                    }
                                });
                                //Log.d("raisan", mahasiswaArrayList.toString());

                            } else {
                                kelaspenggantis = repo.getKelas();
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        memoAdapter adapter = new memoAdapter(kelaspenggantis, memoList.this);
                                        recyclerView.setAdapter(adapter);

                                        adapter.notifyDataSetChanged();
                                        recyclerView.scheduleLayoutAnimation();
                                    }
                                });
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
                                    Intent i = new Intent(memoList.this, MainActivity.class);
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
            public void onFailure(Call<List<memoModel>> call, Throwable t) {

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
                                memoAdapter adapter = new memoAdapter(kelaspenggantis,memoList.this);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == 12) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateKelas();
            }
        }
    }
    private void addAlarm(Boolean cancel){
        int requestCode = 150;
        if (!cancel) {
            for (int i = 0; i <= newCount - 1; i++) {
                Log.d("zhafarin", "ini i nya " + i);
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                String jenis= kelaspenggantis.get(i).getJenis()+", ";
                String namatkul = jenis+kelaspenggantis.get(i).getNamakul();
                Log.d("zhafarin", "ini namatkul nya " + namatkul);
                String jam = kelaspenggantis.get(i).getJam().substring(0, 2);
                String menit = kelaspenggantis.get(i).getJam().substring(3, 5);
                String ruangan = kelaspenggantis.get(i).getRuangan();
                String tanggal = kelaspenggantis.get(i).getTanggal();
                String brigita = "Besok jam " + jam + "." + menit + ", di " + ruangan;
                SimpleDateFormat formatterTanggal = new SimpleDateFormat("dd-MM-yyyy");
                Date waktu1 = new Date();
                try {
                    waktu1 = formatterTanggal.parse(tanggal);
                } catch (Exception e) {
                    //
                }
                Calendar c = Calendar.getInstance();
                c.setTime(waktu1);
                int day = c.get(Calendar.DAY_OF_WEEK);
                Calendar calNow = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(jam));
                calendar.set(Calendar.MINUTE, Integer.parseInt(menit));
                calendar.set(Calendar.DAY_OF_WEEK, day-1);
                calendar.set(Calendar.SECOND, 0);
                if (calendar.before(calNow)) {
                    // If it's in the past increment by one week.
                    continue;
                }
                Intent myIntent = new Intent(this, AlarmReceiver.class);
                myIntent.putExtra("Judul", namatkul);
                myIntent.putExtra("konten", brigita);
                myIntent.putExtra("notifID", requestCode);
                myIntent.putExtra("hour", jam);
                myIntent.putExtra("minute", menit);
                myIntent.putExtra("dayofweek", day);
                myIntent.putExtra("repeat", false);
                myIntent.putExtra("afterAction", "memo");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                requestCode++;

                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);

            }
        }else {
            requestCode = 150;
            for (int i = 0; i <=oldCount - 1; i++) {
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                manager.cancel(pendingIntent);
                requestCode++;

            }
        }

    }

}
