package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.commons.text.WordUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;

public class sinkronizer extends AppCompatActivity {
    Map<String, String> kukis;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String nim, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkronizer);
        kukis = (Map) getIntent().getSerializableExtra("kukis");
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
       nim = mSettings.getString("nim", "nim");
       pass = mSettings.getString("pass","pass");
    }

    private class GetMatkul extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{

                Connection.Response initial = Jsoup
                        .connect(params[0])
                        .method(Connection.Method.GET).execute();
                Connection.Response document = Jsoup.connect(params[0])

                        .data("LoginForm[username]", nim)
                        .data("LoginForm[password]", pass)
                        //.cookies(document.cookies())
                        .method(Connection.Method.POST)
                        .execute();

//This will get you cookies
                Map<String, String> cookies = document.cookies();



                Document page = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=krskhs/entrikrsmhs")
                        .cookies(cookies)
                        .get();

                for (int i = 1; i <= 10; i++) {
                    //int i = 1;
                    String urldosen = "#krskini-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(9)";
                    String urlwinnykul = "#krskini-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)";
                    String urlhari3 = "#krskini-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(7)";
                    String urlelement4 = "#krskini-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(8)";

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
                    String hari4 = hari3.text();
                    Elements element4 = page.select(urlelement4);
                    String title4 = element4.text();
                    mahasiswaArrayList.add(new matkul(winnyku2, hari4, eldosen2, title4));

                }
                /*
                Element links = page
                          .selectFirst("#yw0 > li:nth-child(4) > a");
                    String linkURL = links.absUrl("href");
                Document page2 = Jsoup
                       .connect(linkURL)
                     .cookies(cookies)
                       .get();
                for (int i = 1; i <=4; i++){
                    String urldosen = "#jadwal-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(4)";
                    String urlwinnykul = "#jadwal-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)";
                    String urlhari3 = "#jadwal-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(5)";
                    String urlelement4 = "#jadwal-grid > table > tbody > tr:nth-child(" + i + ") > td:nth-child(6)";
                    Elements eldosen = page2.select(urldosen);
                    String eldosen2 = eldosen.text();
                    if (eldosen2.isEmpty()) {
                        break;
                    }
                    eldosen2 = eldosen2.toLowerCase();
                    eldosen2 = WordUtils.capitalize(eldosen2);
                    Elements winnykul = page2.select(urlwinnykul);
                    String winnyku2 = winnykul.text();
                    Elements hari3 = page2.select(urlhari3);
                    String hari4 = hari3.text();
                    Elements element4 = page2.select(urlelement4);
                    String title4 = element4.text();
                    mahasiswaArrayList.add(new matkul(winnyku2, hari4, eldosen2, title4));

                } */

            }catch (Exception excp){
                excp.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(jadwalKuliah.this);
            dialog.setProgress(0);
            dialog.setMessage("Santai, kuliah bukan cuma nilai...");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(dialog.isShowing())
                dialog.dismiss();
            adapter.notifyDataSetChanged();

        }
    }
}
