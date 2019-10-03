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
import android.view.Window;
import android.view.WindowManager;
//import android.widget.GridLayout;
import androidx.core.app.NotificationCompat;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.String;

import android.view.View;
import android.widget.Toast;

import com.topanlabs.unsoedpass.broadcast.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
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

public class MainActivity extends AppCompatActivity {
TextView txtnama;
ProgressDialog dialog;
String nama;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
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
    String ifpremium;
    ImageView imgpremium;
    String token;
    Boolean warntrial;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gridLayout=(GridLayout)findViewById(R.id.mainGrid);
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
        }
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        deliverNotification();
}
    @Override
    public void onResume(){
        super.onResume();
        String yourLocked = mSettings.getString("logged", "ya");
        if (yourLocked.equals("ya")) {
            Intent i = new Intent(this, login.class);
            startActivity(i);
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
        final String BASE_URL = "https://geni.topanlabs.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        statint = retrofit.create(statint.class);
        authenticator = retrofit.create(tokenint.class);
        getToken();

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
            // SET TIME HERE
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 42);


            myIntent = new Intent(this, AlarmReceiver.class);
            myIntent.putExtra("Judul", winnyk[i]);
            myIntent.putExtra("konten", kongten[i]);
            myIntent.putExtra("notifID", i);
            pendingIntent = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("aurel", "uda");
        }
    }
}
