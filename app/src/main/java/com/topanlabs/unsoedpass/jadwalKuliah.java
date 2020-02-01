package com.topanlabs.unsoedpass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;

import java.util.List;
import java.util.Map;
import java.lang.String;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;

import java.util.ArrayList;

import static android.widget.GridLayout.HORIZONTAL;

public class jadwalKuliah extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private List<matkuldb> mahasiswaArrayList;
    private  matkulViewModel matkulViewModel;
    GetMatkul getmatkullist;
    ProgressDialog dialog;
    String nim, pass;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kuliah);
        mSettings = getSharedPreferences("Settings",0);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addData();
        adapter = new MahasiswaAdapter(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build()).build();
        mAdView.loadAd(adRequest);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(jadwalKuliah.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Jadwal Kuliah");
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation);
        recyclerView.setLayoutAnimation(controller);
        nim = mSettings.getString("nim", "nim");
        pass = mSettings.getString("pass", "pass");
        matkulViewModel = ViewModelProviders.of(this).get(matkulViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        matkulViewModel.getAll().observe(this, new Observer<List<matkuldb>>() {
            @Override
            public void onChanged(@Nullable final List<matkuldb> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
                recyclerView.scheduleLayoutAnimation();
            }
        });


        //getmatkullist =new GetMatkul();
        //getmatkullist.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
    }
    void addData() {
        mahasiswaArrayList = new ArrayList<>();
        //mahasiswaArrayList.add(new matkul("Dimas Maulana", "1414370309", "123456789", "BLABLA"));
        //mahasiswaArrayList.add(new matkul("Fadly Yonk", "1214234560", "987654321","winny imut"));
        //mahasiswaArrayList.add(new matkul("Ariyandi Nugraha", "1214230345", "987648765", "winny seksi"));
        //mahasiswaArrayList.add(new matkul("Aham Siswana", "1214378098", "098758124", "devan kaya"));
    }

    private class GetMatkul extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{

                Connection.Response initial = Jsoup
                        .connect(params[0])
                        .method(Method.GET).execute();
                Connection.Response document = Jsoup.connect(params[0])

                        .data("LoginForm[username]", nim)
                        .data("LoginForm[password]", pass)
                        //.cookies(document.cookies())
                        .method(Method.POST)
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
                    matkulViewModel.insert(new matkuldb(winnyku2, hari4, eldosen2, title4));

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
