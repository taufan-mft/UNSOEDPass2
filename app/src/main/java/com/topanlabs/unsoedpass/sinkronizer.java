package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.text.WordUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class sinkronizer extends AppCompatActivity {
    Map<String, String> kukis;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private matkulViewModel matkulViewModel;
    String nim, pass, todayString, capcay;
    GetMatkul getmatkullist;
    matkulRepository matkulRepository;
    absenRepository absenRepository;
    Date winny, date2;
    int winny2;
    GetAbsen getab;
    boolean firsttime, fromcapcay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkronizer);
        kukis = (Map) getIntent().getSerializableExtra("kukis");
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        absenRepository = new absenRepository(getApplication());
        matkulRepository = new matkulRepository(getApplication());
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        todayString = formatter.format(todayDate);
       nim = mSettings.getString("nim", "nim");
       pass = mSettings.getString("pass","pass");
       fromcapcay = getIntent().getBooleanExtra("fromCapcay", false);
       capcay = getIntent().getStringExtra("capcayku");
       firsttime = mSettings.getBoolean("firstsync", true);
        matkulViewModel = ViewModelProviders.of(this).get(matkulViewModel.class);
        getmatkullist =new GetMatkul();
        getmatkullist.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
         getab = new GetAbsen();

    }

    private class GetMatkul extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{
                absenRepository.nukeTable();
                matkulRepository.nukeTable();
                if (fromcapcay) {
                    Log.d("raisa","dari capcay nih");
                    Connection.Response document = Jsoup.connect(params[0])

                            .data("LoginForm[username]", nim)
                            .data("LoginForm[password]",pass)
                            .data("LoginForm[verifyCode]", capcay)
                            //.cookies(initial.cookies())
                            .cookies(kukis)
                            .method(Connection.Method.POST)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                            .execute();

//This will get you cookies

                    kukis = document.cookies();
                }
                Document page2 = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=jadwal/jadwalpermhs")
                        .cookies(kukis)
                        .get();
                Element kodeadkul = page2.selectFirst("#kodetahunakadkul > option:nth-child(2)");
                String linkURL2 = kodeadkul.attr("value");
                Document page = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=krskhs/detailkrspermhs&kodetahunakadkul="+linkURL2)
                        .cookies(kukis)
                        .get();

                for (int i = 1; i <= 10; i++) {
                    //int i = 1;
                    String urldosen = "#content > table.table.table-striped.well > tbody > tr:nth-child(" + i + ") > td:nth-child(7)";
                    String urlwinnykul = "#content > table.table.table-striped.well > tbody > tr:nth-child(" + i + ") > td:nth-child(3)";
                    String urlhari3 = "#content > table.table.table-striped.well > tbody > tr:nth-child(" + i + ") > td:nth-child(8)";
                    String urlelement4 = "#content > table.table.table-striped.well > tbody > tr:nth-child(" + i + ") > td:nth-child(8)";

                    Elements eldosen = page.select(urldosen);
                    String eldosen2 = eldosen.text();
                    if (eldosen2.isEmpty()) {
                        break;
                    }
                    eldosen2 = eldosen2.toLowerCase();
                    eldosen2 = WordUtils.capitalize(eldosen2);
                    Elements winnykul = page.select(urlwinnykul);
                    String winnyku2 = winnykul.text();
                    Elements hari3 = page.select(urlhari3);
                    String hari4 = hari3.text().replaceAll("[^a-zA-Z].*", "");
                    Elements element4 = page.select(urlelement4);
                    String title4 = element4.text().replaceAll("\\D+","");
                    matkulViewModel.insert(new matkuldb(winnyku2, hari4, eldosen2, title4));

                }
            }catch (Exception excp){
                excp.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getab.execute((new String[]{"ya"}));

        }
    }

    private class GetAbsen extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{

                Document page5 = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=jadwal/jadwalpermhs")
                        .cookies(kukis)
                        .get();
                Element kodeadkul = page5.selectFirst("#kodetahunakadkul > option:nth-child(2)");
                String linkURL2 = kodeadkul.attr("value");

                Document page = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=jadwal/jadwalpermhs&kodetahunakadkul="+linkURL2)
                        .cookies(kukis)
                        .get();

                for (int i = 1; i <= 10; i++) {
                    //int i = 1;
                    Log.d("raisan","disanaa");
                    String urlmatkul = "#jadwal-grid > table > tbody > tr:nth-child("+i+") > td:nth-child(2)";
                    String urltombol = "#jadwal-grid > table > tbody > tr:nth-child("+i+") > td:nth-child(12) > a";
                    Elements namatkul = page.select(urlmatkul);
                    String eldosen2 = namatkul.text();
                    if (eldosen2.contains("Praktikum")) {
                        continue;
                    }
                    Log.d("raisan",String.valueOf(eldosen2.isEmpty()));
                    if (eldosen2.isEmpty()) {
                        break;
                    }
                    eldosen2 = eldosen2.toLowerCase();
                    eldosen2 = WordUtils.capitalize(eldosen2);
                    Element tombol = page.selectFirst(urltombol);
                    String linkURL = tombol.absUrl("href");
                    Document page2 = Jsoup //buka detail pertemuan
                            .connect(linkURL)
                            .cookies(kukis) //use this with any page you parse. it will log you in
                            .get();
                    for (int g = 1; g <= 10; g++) {
                        Log.d("raisan","dfisinii");
                        String urlhadir = "#jadwal-grid > table > tbody > tr:nth-child("+g+") > td:nth-child(8) > span"; //katakata hadir
                        Elements kehadiran = page2.select(urlhadir);
                        String winnyaw = kehadiran.text();
                        String tanggal = "#jadwal-grid > table > tbody > tr:nth-child("+g+") > td:nth-child(3)";
                        String hari = "#jadwal-grid > table > tbody > tr:nth-child("+g+") > td:nth-child(2)";
                        Elements hahrii = page2.select(hari);
                        hari = hahrii.text();
                        Elements tanggalfin = page2.select(tanggal);
                        tanggal = tanggalfin.text();
                        if(tanggal != null) {
                            try {
                                winny = new SimpleDateFormat("dd-MM-yyyy").parse(todayString);
                                date2 = new SimpleDateFormat("dd-MM-yyyy").parse(tanggal);
                                winny2 = date2.compareTo(winny);
                            } catch (java.text.ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if (winnyaw.isEmpty()) {
                            break;
                        }
                        if (winny2 > 0) {
                            break;
                        }
                        Log.d("raisan", String.valueOf(winnyaw.isEmpty()));
                    absenRepository.insert(new absendb(0,eldosen2, hari,tanggal,winnyaw));
                        Log.d("raisan", eldosen2 +hari+tanggal+winnyaw);

                    }
                    String urlhadir2 = "#yw0 > li:nth-child(4) > a";
                    Element tombol2 = page2.selectFirst(urlhadir2);

                    if (tombol2 != null) {
                        linkURL2 = tombol2.absUrl("href");
                        Document page3 = Jsoup
                                .connect(linkURL2)
                                .cookies(kukis) //use this with any page you parse. it will log you in
                                .get();
                        for (int h = 1; h<= 4; h++) {
                            String urlhadir = "#jadwal-grid > table > tbody > tr:nth-child("+h+") > td:nth-child(8) > span"; //katakata hadir
                            Elements kehadiran = page3.select(urlhadir);
                            String winnyaw = kehadiran.text();
                            String tanggal = "#jadwal-grid > table > tbody > tr:nth-child("+h+") > td:nth-child(3)";
                            String hari = "#jadwal-grid > table > tbody > tr:nth-child("+h+") > td:nth-child(2)";
                            Elements hahrii = page3.select(hari);
                            hari = hahrii.text();
                            Elements tanggalfin = page3.select(tanggal);
                            tanggal = tanggalfin.text();
                            if(tanggal != null) {
                                try {
                                    winny = new SimpleDateFormat("dd-MM-yyyy").parse(todayString);
                                    date2 = new SimpleDateFormat("dd-MM-yyyy").parse(tanggal);
                                    winny2 = date2.compareTo(winny);
                                } catch (java.text.ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (winnyaw.isEmpty()) {
                                break;
                            }
                            if (winny2 > 0) {
                                break;
                            }
                            Log.d("raisan", String.valueOf(winnyaw.isEmpty()));
                            absenRepository.insert(new absendb(0,eldosen2, hari,tanggal,winnyaw));

                        }

                    }

                }
            }catch (Exception excp){
                excp.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Date todayDate = Calendar.getInstance().getTime();
            DateTimeFormatter dateStringFormat = DateTimeFormat
                    .forPattern("dd MM yyyy");
            SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
            todayString = formatter.format(todayDate);
            editor.putString("tglSync",todayString);
            editor.apply();
            if (firsttime) {
                final Intent i = new Intent(sinkronizer.this, setReminder.class);
                startActivity(i);
                editor.putBoolean("firstsync", false);
                editor.apply();
                finish();
            } else {

                CharSequence text = "Sinkronisasi selesai.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(sinkronizer.this, text, duration);
                toast.show();
                final Intent i = new Intent(sinkronizer.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
