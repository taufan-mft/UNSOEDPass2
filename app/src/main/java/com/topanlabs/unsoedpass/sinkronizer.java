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

import org.apache.commons.text.WordUtils;
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
    String nim, pass, todayString;
    GetMatkul getmatkullist;
    absenRepository absenRepository;
    Date winny, date2;
    int winny2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkronizer);
        kukis = (Map) getIntent().getSerializableExtra("kukis");
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        absenRepository = new absenRepository(getApplication());
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        todayString = formatter.format(todayDate);
       nim = mSettings.getString("nim", "nim");
       pass = mSettings.getString("pass","pass");
        matkulViewModel = ViewModelProviders.of(this).get(matkulViewModel.class);
        getmatkullist =new GetMatkul();
        getmatkullist.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
        GetAbsen getab = new GetAbsen();
        getab.execute((new String[]{"ya"}));
    }

    private class GetMatkul extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{

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
            final Intent i = new Intent(sinkronizer.this, setReminder.class);
            startActivity(i);
            finish();

        }
    }
}
