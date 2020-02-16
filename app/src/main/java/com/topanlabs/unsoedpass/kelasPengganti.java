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
import com.topanlabs.unsoedpass.kelaspenggantidb.kelaspengganti;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    String tokenkita, kodekelas, nim;
    mahaint mahaint;
    List<kelaspengganti>  kelaspenggantis;
    kelasRepository repo;
    Boolean ketuakelas;
    Integer oldCount, newCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas_pengganti);
        repo= new kelasRepository(getApplication());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(kelasPengganti.this);
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
        getSupportActionBar().setTitle("Kelas Pengganti");
        getSupportActionBar().setSubtitle(kodekelas);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        kelasService =
                retrofit.create(kelasInt.class);
        mahaint = retrofit.create(mahaint.class);
        Call<List<kelasModel>> call = kelasService.getKelas(tokenkita,kodekelas);
        call.enqueue(new Callback<List<kelasModel>>() {
            @Override
            public void onResponse(Call<List<kelasModel>> call, Response<List<kelasModel>> response) {
                if (response.code() == 200) {
                    List<kelasModel> matkul = response.body();
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

                                    repo.insert(new kelaspengganti(0, matkul.get(i).getNamatkul(), matkul.get(i).getJam(), matkul.get(i).getRuangan(), matkul.get(i).getTanggal()));
                                }
                                kelaspenggantis = repo.getKelas();
                                newCount = repo.getCount();
                                addAlarm(false);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(kelasPengganti.this);
                    alertDialogBuilder.setTitle("Kelas sudah dihapus.");
                    alertDialogBuilder
                            //.setMessage(pesan)
                            //.setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    editor.putString("kodekelas", "0");
                                    editor.putBoolean("isKetuaKelas",false);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (ketuakelas) {
            inflater.inflate(R.menu.menuketua, menu);
        } else{
            inflater.inflate(R.menu.menu2, menu);
    }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:

                updateKelas();
                return true;
            case R.id.newMemo:
                Intent i = new Intent(kelasPengganti.this, tambahKelas.class);
                startActivity(i);
                return true;
            case R.id.keluarKelas:
                showCancel();
                return true;
            case R.id.hapusKelas:
                hapuskelas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
void hapuskelas(){
    Call<Void> call = kelasService.delKelas(tokenkita,kodekelas);
    call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

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
    editor.putString("kodekelas", "0");
    editor.apply();
    mahasis mahasiw = new mahasis(null,null,null,null, null, null, null, "0", null, null, null);
    call = mahaint.gantiKodekel(nim,mahasiw,tokenkita );
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

    void updateKelas() {
        Call<List<kelasModel>> call = kelasService.getKelas(tokenkita,kodekelas);
        call.enqueue(new Callback<List<kelasModel>>() {
            @Override
            public void onResponse(Call<List<kelasModel>> call, Response<List<kelasModel>> response) {
                if (response.code() == 200) {
                    List<kelasModel> matkul = response.body();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            oldCount = repo.getCount();
                            if (oldCount!= 0){
                                addAlarm(true);
                            }
                            repo.nukeTable();
                            if (!matkul.isEmpty()) {
                                for (int i = 0; i < matkul.size(); i++) {

                                    repo.insert(new kelaspengganti(0, matkul.get(i).getNamatkul(), matkul.get(i).getJam(), matkul.get(i).getRuangan(), matkul.get(i).getTanggal()));
                                }
                                kelaspenggantis = repo.getKelas();
                                newCount = repo.getCount();
                                addAlarm(false);
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
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(kelasPengganti.this);
                    alertDialogBuilder.setTitle("Kelas sudah dihapus.");
                    alertDialogBuilder
                            //.setMessage(pesan)
                            //.setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editor.putString("kodekelas", "0");
                                    editor.apply();
                                    mahasis mahasiw = new mahasis(null, null, null, null, null, null, null, "0", null, null,null);
                                    Call<Void> call = mahaint.gantiKodekel(nim, mahasiw, tokenkita);
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
            }
                @Override
                public void onFailure (Call < List < kelasModel >> call, Throwable t){

                    Context context = getApplicationContext();
                    CharSequence text = "Error TL12";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }

                //showDialog();
            });
    };

    private void showCancel() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(kelasPengganti.this);
        alertDialogBuilder.setTitle("Keluar kelas?");
        alertDialogBuilder
                //.setMessage(pesan)
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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

    private void addAlarm(Boolean cancel){
        int requestCode = 60;
    if (!cancel) {
        for (int i = 0; i <= newCount - 1; i++) {
            Log.d("zhafarin", "ini i nya " + i);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            String namatkul = kelaspenggantis.get(i).getNamakul() +" "+ new String(Character.toChars(0x1F500));
            Log.d("zhafarin", "ini namatkul nya " + namatkul);
            String jam = kelaspenggantis.get(i).getJam().substring(0, 2);
            String menit = kelaspenggantis.get(i).getJam().substring(3, 5);
            String ruangan = kelaspenggantis.get(i).getRuangan();
            String tanggal = kelaspenggantis.get(i).getTanggal();
            String brigita = "Kuliah dimulai jam " + jam + "." + menit + ", di " + ruangan;
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
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(jam)-1);
            calendar.set(Calendar.MINUTE, Integer.parseInt(menit));
            calendar.set(Calendar.DAY_OF_WEEK, day);
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
            myIntent.putExtra("afterAction", "main");


            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            requestCode++;

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            //Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);

        }
    }else {
        requestCode = 60;
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
