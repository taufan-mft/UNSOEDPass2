package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;

import androidx.appcompat.app.AlertDialog;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.GridLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;
import java.lang.String;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;
import com.topanlabs.unsoedpass.broadcast.AlarmReceiver;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelasRepository;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelaspengganti;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.time.LocalDate.now;

public class MainActivity extends AppCompatActivity {
TextView txtnama;
ProgressDialog dialog;
String nama;
    matkulRepository mRepository;
    kelasRepository kelRepo;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    List<beritaModel> beritaModelk, eventModel, pengumumanModel, beasiswaModel;
    private beritaAdapter beritaAdapter;
    private beritaAdapter eventAdapter;
    private beritaAdapter pengumumanAdapter;
    private beritaAdapter beasiswaAdapter;
    String nim;
    String pass, minus;
    GetMatkul getmatkullist;
    String todayString;
    SimpleDateFormat formatter;
    String tgltrial;
    int days;
    GridLayout gridLayout;
    Intent hasyu;
    statint statint;
    tokenint authenticator;
    beritaInt beritaint;
    String ifpremium;
    ImageView imgpremium;
    String token;
    Boolean warntrial;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    ConstraintLayout jadwalK, aha2,aha4, aha3, exam, teammaker;
    RecyclerView recyclerView, eventrec, pengumumanrec, beasiswarec;
    TextView todayMat, todayJam, todayRuangan, txtSalam;
    String tokenkita;
    Boolean dariKelp;
    Integer indexKelp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        minus = "ya";
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences("Settings",0);
        todayMat = findViewById(R.id.txtNamatkul);
        todayJam = findViewById(R.id.textJamkul);
        todayRuangan=findViewById(R.id.textRuangan);
        txtnama = findViewById(R.id.txtNamaK);
        txtSalam = findViewById(R.id.textView15);
        teammaker = findViewById(R.id.teamMaker);
        updateGreetings();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build()).build();
        mAdView.loadAd(adRequest);
        editor = mSettings.edit();
        String yourLocked = mSettings.getString("logged", "ya");
        String firstTime = mSettings.getString("pertama", "ya");
        String tglSync = mSettings.getString("tglSync","01 01 2000");
        String versiapp = mSettings.getString("versiapp","0");
        tokenkita = mSettings.getString("token","token");

        int intervalSync = mSettings.getInt("intervalSync", 7);
        Date todayDate = Calendar.getInstance().getTime();
        DateTimeFormatter dateStringFormat = DateTimeFormat
                .forPattern("dd MM yyyy");
        formatter = new SimpleDateFormat("dd MM yyyy");
        todayString = formatter.format(todayDate);
        DateTime date1 = dateStringFormat.parseDateTime(tglSync);
        DateTime date2 = dateStringFormat.parseDateTime(todayString);
        days = Days.daysBetween(new LocalDate(date1),
                new LocalDate(date2)).getDays();
        if (firstTime == "ya") {
            Intent i = new Intent(this, welcome.class);
            startActivity(i);
            finish();
            return;
        }else if (yourLocked.equals("ya")) {
            Intent i = new Intent(this, login.class);
            startActivity(i);
            finish();
            return;
        } //else if(!versiapp.equals("2.0")){
            //Intent w = new Intent(MainActivity.this, logout.class);

            //editor.putString("trialwarnabsen", "belum");
            //editor.putString("premium", "tidak");
            //editor.putString("logged", "ya");
            //editor.putString("nim","nim");
            //editor.putBoolean("warntrial3", false);
            //editor.apply();
            //startActivity(w);
            ///finish();
            //return;
        //}
        if(!versiapp.equals("2.1.2")){
            String yangbaru;
            yangbaru = "Changelog v2.1.2:\n" +
                    "- Perbaikan FC pada absen";
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme2);
            alertDialogBuilder.setTitle("Terimakasih telah memperbarui MyUNSOED!");
            alertDialogBuilder
                    .setMessage(yangbaru)
                    //.setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Aku Mengerti", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
            ;
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            editor.putString("versiapp", "2.1.2");
            editor.apply();
        }
        if (days > intervalSync && intervalSync != 0) {
            Intent i = new Intent(this, masukCapcay.class);
            startActivity(i);
            finish();
            return;
        }
        jadwalK = findViewById(R.id.aha);
        aha2 = findViewById(R.id.aha2); //absensi
        aha4 = findViewById(R.id.aha4); //lost and found
        aha3 = findViewById(R.id.aha3); //kelas pengganti
        exam = findViewById(R.id.exam); //jadwal ujian

        jadwalK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent w = new Intent(MainActivity.this, jadwalKuliah.class);
                startActivity(w);
                //throw new RuntimeException("Test Crash");
            }
        });

        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                //showaDialog("Belum masa ujian","Cek jadwal ujian tidak tersedia karena bukan masa ujian.","yup");
            Intent a = new Intent(getApplicationContext(), beritaView.class);
            a.putExtra("url", "https://whatever.topanlabs.com/app/");
            a.putExtra("showsub",false);
            a.putExtra("judul","Shop");
            startActivity(a);
            }
        });

        aha4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w = new Intent(MainActivity.this, memoList.class);
                startActivity(w);

            }
        });

        aha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w = new Intent(MainActivity.this, absenSelector.class);
                startActivity(w);

            }
        });
        aha3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w = new Intent(MainActivity.this, kelasSelector.class);
                startActivity(w);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.beritarec);
        eventrec = findViewById(R.id.eventrec);
        pengumumanrec = findViewById(R.id.pengumumanrec);
        beasiswarec = findViewById(R.id.beasiswarec);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        beritaAdapter = new beritaAdapter(beritaModelk, this);
        eventAdapter = new beritaAdapter(eventModel, this);
        pengumumanAdapter = new beritaAdapter(pengumumanModel, this);
        beasiswaAdapter = new beritaAdapter(beasiswaModel, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(beritaAdapter);
        eventrec.setLayoutManager(layoutManager2);
        eventrec.setAdapter(eventAdapter);
        pengumumanrec.setLayoutManager(layoutManager3);
        pengumumanrec.setAdapter(pengumumanAdapter);
        beasiswarec.setLayoutManager(layoutManager4);
        beasiswarec.setAdapter(beasiswaAdapter);
        getBerita();
        mRepository = new matkulRepository(getApplication());
        kelRepo = new kelasRepository(getApplication());
        updJadwal();
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
        String namaku = mSettings.getString("nama", "waduh");
        String[] arr = namaku.split("\\s+");
        if (arr.length > 1) {
            txtnama.setText(arr[0] + " " + arr[1] + "!");
        } else {
            txtnama.setText(arr[0] +  "!");
        }


       /** gridLayout=(GridLayout)findViewById(R.id.mainGrid);
         CardView cardView=(CardView)gridLayout.getChildAt(0);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w = new Intent(MainActivity.this, jadwalKuliah.class);
                startActivity(w);
            }
        });
        CardView cardView2=(CardView)gridLayout.getChildAt(1);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                absenLaunch(days);
            }
        });

        CardView cardView3=(CardView)gridLayout.getChildAt(2);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showaDialog("Belum masa ujian","Cek jadwal ujian tidak tersedia karena bukan masa ujian","yup");
            }
        });

        CardView cardView4=(CardView)gridLayout.getChildAt(3);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w = new Intent(MainActivity.this, settings2.class);
                startActivity(w);

            }
        });
        txtnama = (TextView)findViewById(R.id.txtnama);
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
        String namaku = mSettings.getString("nama", "waduh");
        nim = mSettings.getString("nim", "nim");
        pass = mSettings.getString("pass", "pass");
        tgltrial = mSettings.getString("tawal", "02 02 2000");
       warntrial = mSettings.getBoolean("warntrial3",false);

        txtnama.setText(namaku);
         ifpremium = mSettings.getString("premium", "tidak");
         imgpremium = findViewById(R.id.imgpremium);
        changeStatusBarColor();
        Date todayDate = Calendar.getInstance().getTime();
        DateTimeFormatter dateStringFormat = DateTimeFormat
                .forPattern("dd MM yyyy");
        formatter = new SimpleDateFormat("dd MM yyyy");
        todayString = formatter.format(todayDate);
        DateTime date1 = dateStringFormat.parseDateTime(tgltrial);
        DateTime date2 = dateStringFormat.parseDateTime(todayString);
        days = Days.daysBetween(new LocalDate(date1),
                new LocalDate(date2)).getDays();

        if (warntrial == false && (days < 30) && ifpremium.equals("tidak")) {
            showaDialog("Hai kamu!", "Selamat datang di My UNSOED! Kamu mendapatkan kesempatan untuk mencoba My UNSOED Premium selama 30 hari. Jika cocok, cukup beli seharga 10ribu aktif selamanya. Kalau kamu punya kritik/saran serta menemukan bug, silakan hubungi kami di Settings > Hubungi Kami. Oh, cek jadwal selalu gratis kok, santai aja.", "0");

        }
        if (ifpremium.equals("ya") || days < 30 ){
            imgpremium.setImageResource(R.drawable.premiumbadge);
        } else if (ifpremium.equals("tidak") ) {
            imgpremium.setImageResource(R.drawable.regularbadge);
        }
        String yourLocked = mSettings.getString("logged", "ya");
        String firstTime = mSettings.getString("pertama", "ya");
        if (firstTime == "ya") {
            Intent i = new Intent(this, welcome.class);
            startActivity(i);
            finish();
        }else if (yourLocked == "ya") {
            Intent i = new Intent(this, login.class);
            startActivity(i);
            finish();
        }
        if (nim != "nim") {
            updateStat();
        }**/
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        //deliverNotification();
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                Intent i = new Intent(MainActivity.this, settings2.class);
                startActivity(i);

                return true;
            case R.id.about:
                Intent a = new Intent(MainActivity.this, aboutScreen.class);
                startActivity(a);
                return true;
            case R.id.syncnow:
                Intent s = new Intent(this, masukCapcay.class);
                startActivity(s);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        updJadwal();
        getBerita();
        updateGreetings();
        String yourLocked = mSettings.getString("logged", "ya");
        String firstTime = mSettings.getString("pertama", "ya");
       /** if (firstTime == "ya") {
            Intent i = new Intent(this, welcome.class);
            startActivity(i);
            finish();
            return;
        }else if (yourLocked.equals("ya")) {

            finish();
            return;
        }**/

     //  String yourLocked = mSettings.getString("logged", "ya");
        if (yourLocked.equals("ya")) {

            finish();
        }


    }
        private void showaDialog(String title, String pesan, final String konteks){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(pesan)
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if (konteks.equals("cekabsen")) {
                            Intent w = new Intent(MainActivity.this, cekAbsen.class);
                            startActivity(w);
                            editor.putString("trialwarnabsen", "sudah");
                            editor.apply();
                        } else {
                            dialog.cancel();
                            editor.putBoolean("warntrial3", true);
                            editor.apply();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private class GetMatkul extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{

                Connection.Response initial = Jsoup
                        .connect(params[0])
                        .method(Method.GET).execute();
                Connection.Response document = Jsoup.connect(params[0])

                        .data("LoginForm[username]", "C1B018118")
                        .data("LoginForm[password]","jelek123")

                        .method(Method.POST)
                        .execute();

                Map<String, String> cookies = document.cookies();

                Document page = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php")
                        .cookies(cookies) //use this with any page you parse. it will log you in
                        .get();
                Elements eldosen = page.select("#content > div > table > tbody > tr:nth-child(2) > td:nth-child(3)");
                nama = eldosen.text();
                nama = nama.toLowerCase();
                nama = WordUtils.capitalize(nama);




            }catch (Exception excp){
                excp.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(MainActivity.this);
            dialog.setProgress(0);
            dialog.setMessage("Good things come to those who waits...");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(dialog.isShowing())
                dialog.dismiss();
            //getSupportActionBar().setTitle(blog_title);
            txtnama.setText(nama);


        }
    }
    private void updateStat() {
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        statint = retrofit.create(statint.class);
        authenticator = retrofit.create(tokenint.class);
        getToken();

    }
    private void getBerita() {
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        statint = retrofit.create(statint.class);
        authenticator = retrofit.create(tokenint.class);
        beritaint = retrofit.create(beritaInt.class);
        Call<List<beritaModel>> call = beritaint.getBerita(tokenkita);
        Log.d("rairairai", tokenkita);
        call.enqueue(new Callback<List<beritaModel>>() {
            @Override
            public void onResponse(Call<List<beritaModel>> call, Response<List<beritaModel>> response) {

                List<beritaModel> changesList = response.body();
                beritaAdapter.setBerita(changesList);
            }

            @Override
            public void onFailure(Call<List<beritaModel>> call, Throwable t) {
                beritaModel alsa = new beritaModel("Kamu sedang offline", "Quote favorit Topan: \"Yang patah tumbuh, yang hilang berganti\" - Banda Neira", null, "Offline", "http://aasirai.id");
                List<beritaModel> changesList = new ArrayList<beritaModel>();
                changesList.add(alsa);

                beritaAdapter.setBerita(changesList);
                Context context = getApplicationContext();
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });
        Call<List<beritaModel>> call2 = beritaint.getEvent();
        Log.d("rairairai", tokenkita);
        call2.enqueue(new Callback<List<beritaModel>>() {
            @Override
            public void onResponse(Call<List<beritaModel>> call, Response<List<beritaModel>> response) {

                List<beritaModel> changesList = response.body();
                eventAdapter.setBerita(changesList, "Events");
            }

            @Override
            public void onFailure(Call<List<beritaModel>> call, Throwable t) {
                beritaModel alsa = new beritaModel("Kamu sedang offline", "Quote favorit Topan: \"Yang patah tumbuh, yang hilang berganti\" - Banda Neira", null, "Offline", "http://aasirai.id");
                List<beritaModel> changesList = new ArrayList<beritaModel>();
                changesList.add(alsa);
                Context context = getApplicationContext();
                eventAdapter.setBerita(changesList, "Events");
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });
        call2 = beritaint.getPengumuman();
        call2.enqueue(new Callback<List<beritaModel>>() {
            @Override
            public void onResponse(Call<List<beritaModel>> call, Response<List<beritaModel>> response) {

                List<beritaModel> changesList = response.body();
                pengumumanAdapter.setBerita(changesList, "Pengumuman");
            }

            @Override
            public void onFailure(Call<List<beritaModel>> call, Throwable t) {
                beritaModel alsa = new beritaModel("Kamu sedang offline", "Quote favorit Topan: \"Yang patah tumbuh, yang hilang berganti\" - Banda Neira", null, "Offline", "http://aasirai.id");
                List<beritaModel> changesList = new ArrayList<beritaModel>();
                changesList.add(alsa);
                Context context = getApplicationContext();
                pengumumanAdapter.setBerita(changesList, "Pengumuman");
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });

        call2 = beritaint.getBeasiswa();
        call2.enqueue(new Callback<List<beritaModel>>() {
            @Override
            public void onResponse(Call<List<beritaModel>> call, Response<List<beritaModel>> response) {

                List<beritaModel> changesList = response.body();
                beasiswaAdapter.setBerita(changesList, "Beasiswa");
            }

            @Override
            public void onFailure(Call<List<beritaModel>> call, Throwable t) {
                beritaModel alsa = new beritaModel("Kamu sedang offline", "Quote favorit Topan: \"Yang patah tumbuh, yang hilang berganti\" - Banda Neira", null, "Offline", "http://aasirai.id");
                List<beritaModel> changesList = new ArrayList<beritaModel>();
                changesList.add(alsa);
                Context context = getApplicationContext();
                beasiswaAdapter.setBerita(changesList, "Beasiswa");
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });
    }
    private void updateGreetings() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        Log.d("nabkus", String.valueOf(timeOfDay));
        if(timeOfDay >= 0 && timeOfDay < 3){
            String salam= "Loh kok belom tidur?";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 3 && timeOfDay < 6){
            String salam= "Wah, gasik banget udah bangun.";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 6 && timeOfDay < 12){
            String salam= "Selamat pagi, mau ngapain hari ini? ";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            String salam= "Selamat siang, mau ngapain hari ini?";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 16 && timeOfDay < 18){
            String salam= "Selamat sore, menikmati senja sama siapa nih?";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 18 && timeOfDay < 21){
            String salam= "Selamat malam, jangan lupa makan malam ya.";
            txtSalam.setText(salam);
        }
        else if(timeOfDay >= 21 && timeOfDay < 24){
            String salam= "Ayo istirahat. Jangan begadang, ya.";
            txtSalam.setText(salam);
        }
    }
    Boolean overtime = false;
    private void updJadwal() {

        minus = "ya";
    dariKelp = false;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat formatterTanggal = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date(System.currentTimeMillis());
                String date2 = formatter.format(date);
                String waktu = formatterTanggal.format(date);
                Log.d("rairai", waktu);
                Date waktu1 = new Date();
                try {
                    waktu1 = formatter.parse(date2);
                } catch (Exception e) {
                    //
                }
                Log.d("winal", "devina");
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                String harini = "ha";
                switch (day) {
                    case Calendar.SUNDAY:
                        harini = "MINGGU";
                        break;
                    case Calendar.MONDAY:
                        harini = "SENIN";
                        break;
                    case Calendar.TUESDAY:
                        harini = "SELASA";
                        break;
                    case Calendar.WEDNESDAY:
                        harini = "RABU";
                        break;
                    case Calendar.THURSDAY:
                        harini = "KAMIS";
                        break;
                    case Calendar.FRIDAY:
                        harini = "JUMAT";
                        break;
                    case Calendar.SATURDAY:
                        harini = "SABTU";
                        break;

                }
                Log.d("winal", harini);
                List<matkuldb> win = mRepository.getTodayMat(harini);
                List<kelaspengganti> kelp = kelRepo.getKelasIni(waktu);
                long[] milisec = new long[win.size()];
                long[] milisec2 = new long[kelp.size()];

                if (!kelp.isEmpty()) {
                    for (int i = 0; i < kelp.size(); i++) {
                        String date3 = kelp.get(i).getJam();
                        Log.d("KELPDATE", date3);
                        Date waktu2 = new Date();
                        try {
                            waktu2 = formatter2.parse(date3);
                        } catch (Exception e) {
                            //
                        }
                        long difference = waktu2.getTime() - waktu1.getTime();

                        milisec2[i] = difference;
                    }
                    long min = Long.MAX_VALUE;

                    minus = "ya";
//if (milisec2.length != 0){
                    for (int a = 0; a < milisec2.length; a++) {
                        Log.d("winal", "ini " + a);
                        if (min > milisec2[a] && milisec2[a] > 0) {
                            min = milisec2[a];
                          //  Log.d("winal2", "Ini value milisec2 min & max kelp" + min + "&" + milisec[a]);
                            indexKelp = a;
                            minus = "ga";
                        }
                    }
             //   }
                    }


                if (!win.isEmpty() || !kelp.isEmpty()) {
                    for (int i = 0; i < win.size(); i++) {
                        Log.d("winEMPTY", win.get(i).getJam().substring(0, 4));
                        String date3 = win.get(i).getJam().substring(0, 4);
                        Date waktu2 = new Date();
                        try {
                            waktu2 = formatter.parse(date3);
                        } catch (Exception e) {
                            //
                        }
                        long difference = waktu2.getTime() - waktu1.getTime();

                            difference = difference + 300000;
                            Log.d("nabilaaa", "masuk ga sih"+Long.toString(difference));
                        if (difference > 0 & difference <300000) {
                            overtime=true;
                            Log.d("nabilaaa", "masuk ga sih"+Long.toString(difference));
                        }
                        milisec[i] = difference;
                        Log.d("winal", Long.toString(difference));

                    }
                    long min = Long.MAX_VALUE;
                    int index = 0;


                    for (int i = 0; i < milisec.length; i++) {
                        Log.d("winal", "ini " + i);
                        if (min > milisec[i] && milisec[i] > 0) {
                            min = milisec[i];
                            Log.d("winal", "Ini value min & max" + min + "&" + milisec[i]);
                            index = i;
                            minus = "ga";
                        }
                    }
                    if (milisec.length != 0) {
                        if (milisec[index] < 0) {
                            milisec[index] = 0;
                        }
                    } else {
                        milisec = new long[1];
                        milisec[0]= -19;
                    }
//                    Log.d("apakahiya", String.valueOf(milisec.length) + " " + String.valueOf(milisec2.length) + " " + String.valueOf(index)+" kon "+ milisec2[indexKelp]+" "+milisec[index]);
                    if (!kelp.isEmpty()) {
                        if ((milisec2[indexKelp] > milisec[index]) && (milisec[index] > 0) || (milisec2[indexKelp] <0))  {
                            dariKelp = false;
                            Log.d("mili", milisec2[indexKelp] + "> " + milisec[index]);
                            Log.d("mili", dariKelp.toString());
                        } else {
                            dariKelp = true;
                            Log.d("mili", "oke");
                        }
                    }

                    if (minus.equals("ga")) {
                        final int index2 = index;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dariKelp) {
                                    todayMat.setText(kelp.get(indexKelp).getNamakul() +" "+ new String(Character.toChars(0x1F500)));
                                    todayJam.setText(kelp.get(indexKelp).getJam());
                                    todayRuangan.setText(kelp.get(indexKelp).getRuangan());
                                } else {
                                    if (overtime) {
                                        todayMat.setText(win.get(index2).getNamakul()+" "+new String(Character.toChars(0x23F3)));
                                    }else {
                                        todayMat.setText(win.get(index2).getNamakul());
                                    }
                                    Log.d("uitred", "disini");
                                    String jamentah = win.get(index2).getJam();
                                    if (jamentah.length() == 8) {
                                        String jamateng = win.get(index2).getJam().substring(0, 2) + ":" + win.get(index2).getJam().substring(2, 4) + " - " + win.get(index2).getJam().substring(4, 6) + ":" + win.get(index2).getJam().substring(6, 8);
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                todayJam.setText(jamateng);
                                            }
                                        });

                                    } else {
                                        String jamateng = win.get(index2).getJam().substring(0, 2) + ":" + win.get(index2).getJam().substring(2, 4);
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                todayJam.setText(jamateng);
                                            }
                                        });
                                    }

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            todayRuangan.setText(win.get(index2).getRuangan());
                                        }
                                    });


                                }
                            }


                        });

                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            todayMat.setText("Great job!");
                            todayJam.setText("tidak ada kuliah lagi.");
                            todayRuangan.setText("selamat istirahat,");
                        }
                    });
                }
            } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            todayMat.setText("Great job!");
                            todayJam.setText("tidak ada kuliah lagi.");
                            todayRuangan.setText("selamat istirahat,");
                        }
                    });
                }


        };
    });
    }

    private void getToken() {
        tokenmodel tokenmodel = new tokenmodel("admin","WhyIveBeenCryingOverYou123!@#");
        Call<tokenmodel> call = authenticator.getToken(tokenmodel);
        call.enqueue(new Callback<tokenmodel>() {
            @Override
            public void onResponse(Call<tokenmodel> call, Response<tokenmodel> response) {
                Context context = getApplicationContext();
                CharSequence text = "Berhasil create entry";
                int duration = Toast.LENGTH_SHORT;
                tokenmodel tokenmodel = response.body();
                String winaw = tokenmodel.getToken();
                token = "JWT " + winaw;
                Toast toast = Toast.makeText(context, token, duration);
                //toast.show();
                mulaiUpdate();

            }

            @Override
            public void onFailure(Call<tokenmodel> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    private void mulaiUpdate() {
        Call<statmodel> call = statint.getStat(nim, token);
        call.enqueue(new Callback<statmodel>() {
            @Override
            public void onResponse(Call<statmodel> call, Response<statmodel> response) {
                Context context = getApplicationContext();

                int statusCode = response.code();
                if (statusCode == 404) {
                    createStat();
                } else {
                    updatestat();
                }


            }

            @Override
            public void onFailure(Call<statmodel> call, Throwable t) {

                Context context = getApplicationContext();
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });
    }
    private void createStat(){
        String brand = Build.MANUFACTURER;
        String devicename = Build.MODEL;
        Date todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("dd-MM-yyyy, H:m");
        todayString = formatter.format(todayDate);
        statmodel statmodel = new statmodel(nim,brand, todayString, devicename);
        Call<statmodel> call = statint.createStat(statmodel, token);
        call.enqueue(new Callback<statmodel>() {
            @Override
            public void onResponse(Call<statmodel> call, Response<statmodel> response) {
                Context context = getApplicationContext();
                CharSequence text = "Berhasil create entry";
                int duration = Toast.LENGTH_SHORT;
                statmodel statmodel = response.body();
                token = "berhasil";
                Toast toast = Toast.makeText(context, token, duration);
                toast.show();


            }

            @Override
            public void onFailure(Call<statmodel> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Gagal create entry";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    private void updatestat(){
        String brand = Build.MANUFACTURER;
        String devicename = Build.MODEL;
        Date todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("dd-MM-yyyy, H:m");
        todayString = formatter.format(todayDate);
        statmodel statmodel = new statmodel(nim,brand, todayString, devicename);
        Call<statmodel> call = statint.updateStat(nim, statmodel, token);
        call.enqueue(new Callback<statmodel>() {
            @Override
            public void onResponse(Call<statmodel> call, Response<statmodel> response) {
                Context context = getApplicationContext();
                CharSequence text = "Berhasil create entry";
                int duration = Toast.LENGTH_SHORT;
                statmodel statmodel = response.body();
                token = "berhasil update";
                Toast toast = Toast.makeText(context, token, duration);
                //toast.show();


            }

            @Override
            public void onFailure(Call<statmodel> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Gagal create entry";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Kuliah notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifikasi Utama");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void deliverNotification() {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;
        Log.d("aurel", "memek");
        for (int i = 0; i < 2; i++) {
            String[] winnyk = {"Winny", "Brigita"};
            String[] kongten = {"Anaknya sipit", "Crush nya whimpi"};
            Integer[] gita = {05,07};
            // SET TIME HERE
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            calendar.set(Calendar.MINUTE, gita[i]);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calendar.set(Calendar.SECOND, 0);


            myIntent = new Intent(this, AlarmReceiver.class);
            myIntent.putExtra("Judul", winnyk[i]);
            myIntent.putExtra("konten", kongten[i]);
            myIntent.putExtra("notifID", i);
            pendingIntent = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            //manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("aurel", "uda");
        }
    }
}
