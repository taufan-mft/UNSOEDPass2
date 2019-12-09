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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.topanlabs.unsoedpass.broadcast.AlarmReceiver;

import java.text.SimpleDateFormat;
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
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    List<beritaModel> beritaModelk;
    private beritaAdapter beritaAdapter;
    String nim;
    String pass;
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
    ConstraintLayout jadwalK, aha2,aha4, aha3, exam;
    RecyclerView recyclerView;
    TextView todayMat, todayJam, todayRuangan, txtSalam;
    String tokenkita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences("Settings",0);
        todayMat = findViewById(R.id.txtNamatkul);
        todayJam = findViewById(R.id.textJamkul);
        todayRuangan=findViewById(R.id.textRuangan);
        txtnama = findViewById(R.id.txtNamaK);
        txtSalam = findViewById(R.id.textView15);
        updateGreetings();
        editor = mSettings.edit();
        String yourLocked = mSettings.getString("logged", "ya");
        String firstTime = mSettings.getString("pertama", "ya");
        String tglSync = mSettings.getString("tglSync","01 01 2000");
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
        }
        if (days > intervalSync) {
            Intent i = new Intent(this, masukCapcay.class);
            startActivity(i);
            finish();
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

            }
        });

        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                showaDialog("Belum masa ujian","Cek jadwal ujian tidak tersedia karena bukan masa ujian.","yup");
            }
        });

        aha4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w = new Intent(MainActivity.this, kelasSelector.class);
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
                Intent w = new Intent(MainActivity.this, fiturBelumTersedia.class);
                startActivity(w);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.beritarec);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        beritaAdapter = new beritaAdapter(beritaModelk, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(beritaAdapter);
        getBerita();
        mRepository = new matkulRepository(getApplication());
        updJadwal();
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
        String namaku = mSettings.getString("nama", "waduh");
        String[] arr = namaku.split("\\s+");
        txtnama.setText(arr[0]+" "+arr[1]+"!");



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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        updJadwal();
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

    private void absenLaunch(int days) {
        String trialwarn = mSettings.getString("trialwarnabsen", "belum");

        if ( (days < 30) && trialwarn.equals("belum")) {
            showaDialog("Ini merupakan fitur premium","Cek absen merupakan salah satu fitur premium. Kamu dapat mencoba fitur ini selama 30 hari.", "cekabsen");
        } else if (ifpremium.equals("ya")) {

            Intent w = new Intent(MainActivity.this, cekAbsen.class);
            startActivity(w);
        } else if (days < 30) {
            Intent w = new Intent(MainActivity.this, cekAbsen.class);
            startActivity(w);
        } else if (days > 30){
            Intent w = new Intent(MainActivity.this, checkoutPay.class);
            startActivity(w);
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
        final String BASE_URL = "http://10.10.10.8:8000";
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
        final String BASE_URL = "http://10.10.10.8:8000";
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

                Context context = getApplicationContext();
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
        if(timeOfDay >= 0 && timeOfDay < 12){
            String salam= "Selamat pagi, mau ngapain hari ini?";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            String salam= "Selamat siang, mau ngapain hari ini?";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            String salam= "Selamat malam, mau ngapain hari ini?";
            txtSalam.setText(salam);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            String salam= "Ayo istirahat. Jangan begadang, ya.";
            txtSalam.setText(salam);
        }
    }

    private void updJadwal() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
                Date date = new Date(System.currentTimeMillis());
                String date2 = formatter.format(date);
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
                // Log.d("winal", win.get(2).getNamakul());
                /**for (matkuldb wins : win) {
                 Log.d("winal", wins.getNamakul());
                 }**/
                long[] milisec = new long[win.size()];
                if (!win.isEmpty()) {
                    for (int i = 0; i < win.size(); i++) {
                        Log.d("winal", win.get(i).getJam().substring(0, 4));
                        String date3 = win.get(i).getJam().substring(0, 4);
                        Date waktu2 = new Date();
                        try {
                            waktu2 = formatter.parse(date3);
                        } catch (Exception e) {
                            //
                        }
                        long difference = waktu2.getTime() - waktu1.getTime();
                        milisec[i] = difference;
                        Log.d("winal", Long.toString(difference));

                    }
                    long min = Long.MAX_VALUE;
                    int index = 0;
                    String minus = "ya";

                    for (int i = 0; i < milisec.length; i++) {
                        Log.d("winal", "ini " + i);
                        if (min > milisec[i] && milisec[i] > 0) {
                            min = milisec[i];
                            Log.d("winal", "Ini value min & max" + min + "&" + milisec[i]);
                            index = i;
                            minus = "ga";
                        }
                    }

                    if (minus.equals("ga")) {
                        final int index2 = index;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                todayMat.setText(win.get(index2).getNamakul());
                            }
                        });

                        String jamentah = win.get(index).getJam();
                        if (jamentah.length() == 8 ) {
                            String jamateng = win.get(index).getJam().substring(0,2) +":" +win.get(index).getJam().substring(2,4) + " - " + win.get(index).getJam().substring(4,6) +":"+win.get(index).getJam().substring(6,8);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    todayJam.setText(jamateng);
                                }
                            });

                        } else {
                            String jamateng = win.get(index).getJam().substring(0,2) +":" +win.get(index).getJam().substring(2,4);
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
            }

            ;
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
                            "Winny notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
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
