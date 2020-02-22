package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.text.WordUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

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


public class login extends AppCompatActivity {
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Button button, bthbantuan;
    EditText txtnim;
    EditText txtpass;
    GetMatkul getmatkullist;
    GetCapcay getcapcuy;
    String nim;
    String pass, capcay;
    String fakultas;
    String jurusan, nohp;
    private List<mahasis> userList =null;
    ProgressDialog dialog;
    mahaint apiService;
    tokenint authenticator;
    kelasInt kelaschecker;
    String ceknam;
    String nama, emailku;
    String token;
    Boolean adanim;
    String todayString;
    SimpleDateFormat formatter;
    Map<String, String> kukis;
    Map<String, String> cookies;
    ByteArrayInputStream winnyaw;
    TextView winnyau;
    Bitmap bmp;
    Boolean tersedia;
    String winul = "WhyIveBeenCryingOverYou123!@#";
    String obfuscated;
    ConstraintLayout clay;
    EditText capcaytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("winal", "btncapcay nih");
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Date todayDate = Calendar.getInstance().getTime();
         formatter = new SimpleDateFormat("dd MM yyyy");
        todayString = formatter.format(todayDate);
        button = findViewById(R.id.loginbutton);
        button.setEnabled(false);
        Button btncapcay = findViewById(R.id.loginbutton2);
        bthbantuan = findViewById(R.id.bthbantuan);
        txtnim = (EditText)findViewById(R.id.txnim);
        txtpass = (EditText)findViewById(R.id.txpass);
        capcaytext = findViewById(R.id.txcapcay);
        txtnim.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("kusuma", "1");
                   if (!txtnim.getText().toString().isEmpty() && !txtpass.getText().toString().isEmpty() && !capcaytext.getText().toString().isEmpty()) {
                       button.setEnabled(true);
                   } else {
                       button.setEnabled(false);
                   }
                } else {
                    Log.d("kusuma", "2");
                    if (!txtnim.getText().toString().isEmpty() && !txtpass.getText().toString().isEmpty() && !capcaytext.getText().toString().isEmpty()) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                }
            }
        });
        txtpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("kusuma", "3");
                    if (!txtnim.getText().toString().isEmpty() && !txtpass.getText().toString().isEmpty() && !capcaytext.getText().toString().isEmpty()) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                } else {
                    Log.d("kusuma", "4");
                    if (!txtnim.getText().toString().isEmpty() && !txtpass.getText().toString().isEmpty() && !capcaytext.getText().toString().isEmpty()) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                }
            }
        });
        capcaytext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("kusuma", "5");
                    if (!txtnim.getText().toString().isEmpty() && !txtpass.getText().toString().isEmpty() && !capcaytext.getText().toString().isEmpty()) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(true);
                    }
                } else {
                    Log.d("kusuma", "6");
                    if (!txtnim.getText().toString().isEmpty() && !txtpass.getText().toString().isEmpty() && !capcaytext.getText().toString().isEmpty()) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                }
            }
        });
        winnyau = findViewById(R.id.textView);
        clay = findViewById(R.id.clay);
        Glide.with(this).load("https://imglogin.myunsoed.com").centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {

                clay.setBackground(resource);

            }
        });
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        String versiapp = mSettings.getString("versiapp","0");
        if(versiapp.equals("0")){
            editor.putString("versiapp","2.0");
            editor.apply();
        }

        getSupportActionBar().hide();
        showaDialog();
        tersedia = true;
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

         apiService =
                retrofit.create(mahaint.class);
         authenticator = retrofit.create(tokenint.class);
         kelaschecker = retrofit.create(kelasInt.class);
        getcapcuy =new GetCapcay();
        getcapcuy.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
        //showaDialog();
        Glide.with(this).load("https://imglogin.myunsoed.com").centerCrop().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {

                    clay.setBackground(resource);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                 nim = txtnim.getText().toString();
                 pass = txtpass.getText().toString();

                Log.d("raisaa","aku disini");
                capcay = capcaytext.getText().toString();
                getmatkullist =new GetMatkul();
                getmatkullist.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
                //getcapcuy =new GetCapcay();
                //getcapcuy.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});

                //Intent i = new Intent(login.this, MainActivity.class);
                //finish();  //Kill the activity from which you will go to next activity/startActivity(i);
            }

        });

        btncapcay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("raisa", "btncapcay nih");
                getcapcuy =new GetCapcay();
                getcapcuy.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
            }

        });

        bthbantuan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
               // String url = "https://wa.me/62895425455070?text=Aku%20butuh%20bantuan%20mengenai%20MyUNSOED";
                //Intent i = new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(url));
                //startActivity(i);
                nim = txtnim.getText().toString();
                pass = txtpass.getText().toString();
                EditText win = findViewById(R.id.txcapcay);
                capcay = win.getText().toString();
                 getmatkullist =new GetMatkul();
                getmatkullist.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});

                //Intent i = new Intent(login.this, MainActivity.class);
                //finish();  //Kill the activity from which you will go to next activity
                //startActivity(i);
            }
        });
    }

    private class GetCapcay extends AsyncTask<String,String,String> {
        //Boolean tersedia;
        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{
                Connection initial = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php?r=site/login")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
                Document d = initial.get();
                kukis = initial.response().cookies();
                Element captcha = d.selectFirst("#yw0");
                String linkURL2 = captcha.absUrl("src");

                Log.d("Winny", new Gson().toJson(kukis));
                Map<String, String> diana = new HashMap<String, String>();
                diana.put("raisa", "dianis");
                diana.put("tetenya","turun");
                Log.d("Winny", new Gson().toJson(diana));

                Connection.Response response = Jsoup //
                        .connect(linkURL2) // Extract image absolute URL
                        .cookies(kukis) // Grab cookies
                        .ignoreContentType(true) // Needed for fetching image
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                        .execute();
                Log.d("raisa",linkURL2);
                Log.d("raisa", "Kukis " + kukis);
                //kukis.clear();
                //kukis = response.cookies();

                //kukis.putAll(response.cookies());
                winnyaw = new ByteArrayInputStream(response.bodyAsBytes());
                 bmp = BitmapFactory.decodeByteArray(response.bodyAsBytes(), 0, response.bodyAsBytes().length);




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

            ImageView image = (ImageView) findViewById(R.id.tempatCapcay2);

            image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                    image.getHeight(), false));
           // winnyau.setText(kukis.toString());



        }
    }
    private class GetMatkul extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {//using params[0]
            try{

                //Connection.Response initial = Jsoup
                 //       .connect(params[0])
                 //       .cookies(kukis)
                  //      .method(Connection.Method.GET).execute();
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
                Log.d("Winny2", kukis.toString());

                Document page = Jsoup
                        .connect("https://akademik.unsoed.ac.id/index.php")
                        .cookies(kukis) //use this with any page you parse. it will log you in
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                        .get();
                Elements eldosen = page.select("#content > div > table > tbody > tr:nth-child(6) > td:nth-child(1)");
                ceknam = eldosen.text();
                Elements ceknim = page.select("#content > div > table > tbody > tr:nth-child(1) > td:nth-child(3)");
                nim = ceknim.text();
                Elements cekjurusan = page.select("#content > div > table > tbody > tr:nth-child(6) > td:nth-child(3)");
                jurusan = cekjurusan.text();
                Elements cekfakultas = page.select("#content > div > table > tbody > tr:nth-child(5) > td:nth-child(3)");
                Elements email = page.select("#content > div > table > tbody > tr:nth-child(4) > td:nth-child(3)");
                emailku = email.text();
                Elements noohp = page.select("#content > div > table > tbody > tr:nth-child(3) > td:nth-child(3)");
                nohp = noohp.text();
                Log.d("nohp", emailku+" " + nohp);
                fakultas = cekfakultas.text();
                Elements namaku = page.select("#content > div > table > tbody > tr:nth-child(2) > td:nth-child(3)");
                nama = namaku.text();
                nama = nama.toLowerCase();
                nama = WordUtils.capitalize(nama);
                Log.d("raisaa", "nih:"+nama);
                //tersedia = false;
               if (nama.isEmpty()) {
                   Log.d("raisaa", nama);
                    tersedia = false;
                } else {
                    tersedia = true;
                   Log.d("raisaa", nama);

                }





            }catch (Exception excp){
                excp.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(login.this);
            dialog.setProgress(0);
            dialog.setMessage("Logging you in...");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //winnyau.setText(cookies.toString());
            if (tersedia ) {

                //mahasis user = new mahasis(nim, "winny", fakultas, jurusan, nama);
                cekAdaNIM(nim);
                 } else {
                if(dialog.isShowing())
                    dialog.dismiss();
                showDialog();
                txtnim.setText("");
                txtpass.setText("");
                capcaytext.setText("");
                getcapcuy =new GetCapcay();
                getcapcuy.execute(new String[]{"https://akademik.unsoed.ac.id/index.php?r=site/login"});
            }

            Log.d("tersedia",tersedia.toString());
        }
    }

    private void buatEntry(String nim) {
        mahasis mahasis = new mahasis(nim, todayString, fakultas, jurusan, nama, "0", "0","0",pass, emailku, nohp);
        Call<mahasis> call = apiService.createUser(mahasis);
        final String nim2 = nim;
        call.enqueue(new Callback<mahasis>() {
            @Override
            public void onResponse(Call<mahasis> call, Response<mahasis> response) {
                Context context = getApplicationContext();
                CharSequence text = "Berhasil create entry";
                int duration = Toast.LENGTH_SHORT;
                mahasis mahasis2 = response.body();
                Toast toast = Toast.makeText(context, text, duration);
                String kodekelas = mahasis2.getKodekelas();

                //toast.show();
                final Intent i = new Intent(login.this, sinkronisasi.class);
                i.putExtra("kukis", (Serializable) kukis);
                editor.putString("kodekelas", kodekelas);
                editor.putString("nim", nim2);
                editor.putString("pass", pass);
                editor.putString("logged", "tidak");
                editor.putString("fakultas", fakultas);
                editor.putString("jurusan", jurusan);
                editor.putString("nama", nama);
                editor.putString("tawal", todayString);
                editor.apply();

                getToken(false);

            }

            @Override
            public void onFailure(Call<mahasis> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Gagal create entry";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private void cekAdaNIM(String nim) {
        final String nim2 = nim;
        mahasis mahasis = new mahasis("0", "0", "0", "0", "0", "0", "0","0","0","0","0");
        Call<Void> call = apiService.checkUser(nim2);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Context context = getApplicationContext();

                int statusCode = response.code();
                if (statusCode == 404) {
                    adanim = false;
                    buatEntry(nim2);
                } else {
                    getToken(true);
                }
                Log.d("raisani","di ceknim sukses");

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Context context = getApplicationContext();
                CharSequence text = "Error TL12";
                int duration = Toast.LENGTH_SHORT;
                Log.d("raisani","di ceknim gagal");
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });







    }

    private void getUserDetail(String nim) {
        final String nim2 = nim;
        Call<mahasis> call = apiService.getUser(token, nim);
        call.enqueue(new Callback<mahasis>() {
            @Override
            public void onResponse(Call<mahasis> call, Response<mahasis> response) {

               mahasis mahasis = response.body();
                String kodekelas = mahasis.getKodekelas();
                editor.putString("kodekelas", kodekelas);
                editor.apply();
                checkKetua();


            }

            @Override
            public void onFailure(Call<mahasis> call, Throwable t) {

                Context context = getApplicationContext();
                CharSequence text = "Error TL12";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });

    }

    private void getToken(Boolean cekDetailUser) {
        tokenmodel tokenmodel = new tokenmodel(nim,pass);
        Call<tokenmodel> call = authenticator.getToken(tokenmodel);
        final String nim2 = nim;
        call.enqueue(new Callback<tokenmodel>() {
            @Override
            public void onResponse(Call<tokenmodel> call, Response<tokenmodel> response) {
                Context context = getApplicationContext();
                CharSequence text = "Berhasil create entry";
                int duration = Toast.LENGTH_SHORT;
                tokenmodel tokenmodel = response.body();
                String winaw = tokenmodel.getToken();
                token = "Token " + winaw;
                editor.putString("token", token);
                editor.apply();
                final Intent i = new Intent(login.this, sinkronisasi.class);
                i.putExtra("kukis", (Serializable) kukis);
                editor.putString("nim", nim2);
                editor.putString("pass", pass);
                editor.putString("logged", "tidak");
                editor.putString("fakultas", fakultas);
                editor.putString("jurusan", jurusan);
                editor.putString("nama", nama);
                editor.putString("tawal", todayString);
                editor.apply();
                Log.d("rairai","cantikbgt");
                Toast toast = Toast.makeText(context, token, duration);

                if (cekDetailUser) {
                    getUserDetail(nim2);
                } else {
                    OneSignal.setExternalUserId(nim);
                    final Intent d = new Intent(login.this, sinkronisasi.class);
                    d.putExtra("kukis", (Serializable) kukis);
                    startActivity(i);
                    finish();
                }
                //if(dialog.isShowing())
                //    dialog.dismiss();
                //startActivity(i);
               // finish();
               // toast.show();
                //cekAdaNIM(nim2, token);
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

    private void checkKetua() {
        String kelas = mSettings.getString("kodekelas", "kodekelas");
        Call<kelasModel> call = kelaschecker.cariKelas(kelas);
        final String nim2 = nim;
        call.enqueue(new Callback<kelasModel>() {
            @Override
            public void onResponse(Call<kelasModel> call, Response<kelasModel> response) {
                if (response.code() == 200) {
                    kelasModel kel = response.body();
                    String kita = kel.getKetuakelas();
                    if (kita.equals(nim)) {
                        editor.putBoolean("isKetuaKelas", true);
                        editor.apply();
                    }
                }
                if(dialog.isShowing())
                    dialog.dismiss();
                final Intent i = new Intent(login.this, sinkronisasi.class);
                OneSignal.setExternalUserId(nim);
                i.putExtra("kukis", (Serializable) kukis);
                startActivity(i);
                finish();
                // toast.show();
                //cekAdaNIM(nim2, token);
            }

            @Override
            public void onFailure(Call<kelasModel> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private void showaDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(login.this, R.style.AlertDialogTheme2);
        alertDialogBuilder.setTitle("Halo!");
        alertDialogBuilder
                .setMessage("Kepada mahasiswa:\nHalo, selamat datang kembali di MyUNSOED. Saya harap aplikasi ini bermanfaat untuk perkuliahanmu. God bless you, hidup mahasiswa!" +
                        "\n\nKepada orang unsoed (lptsi & WR akademik) jika melihat ini:\nAplikasi ini sangatlah harmless. Saya terbuka untuk membicarakan aplikasi ini lebih lanjut. Anda bisa menemukan nomor saya di app ini.\nJabat erat,\nTaufan M. F. T.")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(login.this, R.style.AlertDialogTheme2);

        // set title dialog
        alertDialogBuilder.setTitle("NIM Tidak Ditemukan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Mohon pastikan NIM serta password sudah benar dan bisa login di akademik.unsoed.ac.id")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke" + ceknam,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        dialog.cancel();
                        txtnim.requestFocus();
                    }
                });


        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}
