package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.lang.String;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;
import java.util.concurrent.Executors;


public class cekAbsen extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;
    private absenAdapter adapter;
    private List<absendb> mahasiswaArrayList;
    //GetMatkul getmatkullist;
    ProgressDialog dialog;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String nim, pass;
    SimpleDateFormat formatter;
    Date winny;
    Date date2;
    String todayString, namakul;
    private absenRepository absenRepository;
    int winny2, pertemuan, kehadiran;
    TextView txtKehadiran, txtPertemuan, txtNamakul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_absen);
        txtKehadiran = findViewById(R.id.txtJumAbsen);
        txtPertemuan = findViewById(R.id.txtJumHadir);
        txtNamakul = findViewById(R.id.txtNamakul);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //addData();
        winny2 = -1;
        pertemuan = 0;
        getSupportActionBar().setTitle("Detail Absen");
        kehadiran = 0;
        Intent intent = getIntent();
        namakul = intent.getStringExtra("Matkul");
        txtNamakul.setText(namakul);
        absenRepository= new absenRepository(getApplication());
        //
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
        nim = mSettings.getString("nim", "nim");
        pass = mSettings.getString("pass", "pass");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(cekAbsen.this);
        //getSupportActionBar().setTitle("Cek Absen");
        recyclerView.setLayoutManager(layoutManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getmatkullist =new GetMatkul();
        Date todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        todayString = formatter.format(todayDate);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mahasiswaArrayList = absenRepository.getDetailAbsen(namakul);
                adapter = new absenAdapter(mahasiswaArrayList);
                recyclerView.setAdapter(adapter);
                //Log.d("raisan", mahasiswaArrayList.toString());
                adapter.notifyDataSetChanged();
                for (int i = 0; i <= mahasiswaArrayList.size() - 1 ; i++) {
                    String hadirga= mahasiswaArrayList.get(i).getKehadiran();
                    Log.d("rairai","ke "+i +" "+hadirga);
                    if (hadirga.equals("Hadir")) {

                        pertemuan += 1;
                    } else {
                        kehadiran += 1;
                        pertemuan +=1;
                    }
                    String p1 = "Jumlah absen: " + kehadiran;
                    String p2 = "Jumlah kehadiran: " + pertemuan;
                    txtKehadiran.setText(p1);
                    txtPertemuan.setText(p2);
                }

            }
        });
            }
        //getmatkullist.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});


    //void addData() {
     //   mahasiswaArrayList = new List<absendb>();
     //  };

    /**private class GetMatkul extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{
                String skeh;
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

                Document awalan = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=jadwal/jadwalpermhs")
                        .cookies(cookies)
                        .get();
                Element awalan2 = awalan
                        .selectFirst("#kodetahunakadkul > option:nth-child(2)");
                String linkawal = awalan2.attr("value");
                String linkfinal = "https://akademik.unsoed.ac.id/index.php?r=jadwal%2Fjadwalpermhs&kodetahunakadkul=" + "201920201" +"&yt0=Proses";

                Document page = Jsoup
                        .connect(linkfinal)
                        .cookies(cookies) //use this with any page you parse. it will log you in
                        .get();

                for (int i = 1; i <= 10; i++) {
                    String urlmatkul = "#jadwal-grid > table > tbody > tr:nth-child("+i+") > td:nth-child(2)";
                    String urltombol = "#jadwal-grid > table > tbody > tr:nth-child("+i+") > td:nth-child(12) > a";
                    Elements namatkul = page.select(urlmatkul);
                    String eldosen2 = namatkul.text();
                    if (eldosen2.contains("Praktikum")) {
                       continue;
                   }
                    if (eldosen2.isEmpty()) {
                        break;
                    }

                    eldosen2 = eldosen2.toLowerCase();
                    eldosen2 = WordUtils.capitalize(eldosen2);
                    Element tombol = page.selectFirst(urltombol);
                    String linkURL = tombol.absUrl("href");
                    Document page2 = Jsoup //buka detail pertemuan
                            .connect(linkURL)
                            .cookies(cookies) //use this with any page you parse. it will log you in
                            .get();
                    int hadir = 0;
                    int absen = 0;

                    for (int g = 1; g <= 10; g++) { //itung kata2 hadir

                        String urlhadir = "#jadwal-grid > table > tbody > tr:nth-child("+g+") > td:nth-child(8) > span"; //katakata hadir
                        Elements kehadiran = page2.select(urlhadir);
                        String winnyaw = kehadiran.text();
                        String tanggal = "#jadwal-grid > table > tbody > tr:nth-child("+g+") > td:nth-child(3)";
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


                        if (winnyaw.equals("Hadir")) {
                            hadir = hadir + 1;
                        } else {
                            absen = absen + 1;
                            hadir = hadir + 1;
                        }

                    }

                    String urlhadir2 = "#yw0 > li:nth-child(4) > a";
                    Element tombol2 = page2.selectFirst(urlhadir2);

                    if (tombol2 != null) {
                        String linkURL2 = tombol2.absUrl("href");
                        Document page3 = Jsoup
                                .connect(linkURL2)
                                .cookies(cookies) //use this with any page you parse. it will log you in
                                .get();
                        for (int h = 1; h<= 4; h++) {
                            String urlhadir = "#jadwal-grid > table > tbody > tr:nth-child(" + h + ") > td:nth-child(8) > span";
                            Elements kehadiran = page3.select(urlhadir);
                            String winnyaw = kehadiran.text();
                            if (winnyaw.equals("Hadir")) {
                                hadir = hadir + 1;
                            } else {
                                absen = absen + 1;
                                hadir = hadir + 1;
                            }

                        }

                }

                    String hadirr = "Jumlah pertemuan: " + hadir;
                    String absenn = "Jumlah absen: " + absen;
                    mahasiswaArrayList.add(new absendb(eldosen2, absenn, hadirr));

                }


            }catch (Exception excp){
                excp.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(cekAbsen.this);
            dialog.setProgress(0);
            dialog.setMessage("Boleh bolos, asal jangan kelebihan...");
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
            adapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(cekAbsen.this, "OYO", 5);
            //toast.show();

        }
    }**/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                //Intent intent = new Intent(this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                //finish();
                //return true;
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
