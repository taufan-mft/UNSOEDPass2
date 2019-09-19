package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class checkoutPay extends AppCompatActivity {
ImageButton bni;
ImageButton bca;
ProgressBar pbar;
Button cout, copynom, copyrek;
checkoutint apiService;
mahaint orstatus;
tokenint authenticator;
String token;
String nim;
TextView nominaltxt, orderber, awaltrf, rektopan, atasnama, nominalku, txtdet1, txtdet2 ;
SharedPreferences mSettings;
SharedPreferences.Editor editor;
String bank, jamString, tanggalString, nominal;

SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_checkout_pay);
        bni =(ImageButton)findViewById(R.id.bni);
        bca =findViewById(R.id.bca);
        cout = findViewById(R.id.loginbutton);
        cout.setEnabled(false);
        pbar = findViewById(R.id.progressBar);
        Date todayDate = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("HH:mm");
        jamString = formatter.format(todayDate);
        formatter = new SimpleDateFormat("dd MM yyyy");
        tanggalString = formatter.format(todayDate);
        mSettings = getSharedPreferences("Settings", 0);
        nim = mSettings.getString("nim", "nim");
        String lagibeli = mSettings.getString("lagibeli", "tidak");
        if (lagibeli.equals("ya")) {
            Intent intent = new Intent(checkoutPay.this, waitTransfer.class);


            startActivity(intent);
            finish();
        }
        nominaltxt = findViewById(R.id.nominal);
        pbar = findViewById(R.id.progressBar);
        txtdet1 = findViewById(R.id.txtdet1);
        txtdet2 = findViewById(R.id.txtdet2);
        String concatenator = "UNSOED Pass Premium - Rp. 10.000 \nUntuk NIM: " +nim;
        txtdet1.setText(concatenator);
        String concatenator2 = "Pembelian akan dikaitkan dengan NIM: "+nim +" dan aktif selamanya.";
        txtdet2.setText(concatenator2);
       bni.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               bca.setBackgroundResource(R.drawable.gelapbgt);
               bni.setBackgroundResource(R.drawable.buttonselector);
               bank = "bni";
               cout.setEnabled(true);
           }
           //    public void onClick(View v) {
        //       bni.setBackground(getDrawable(R.drawable.buttonselector));
           });
        bca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bca.setBackgroundResource(R.drawable.buttonselector);
                bni.setBackgroundResource(R.drawable.gelapbgt);
                bank = "bca";
                cout.setEnabled(true);
            }
            //    public void onClick(View v) {
            //       bni.setBackground(getDrawable(R.drawable.buttonselector));
        });
        cout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initRetro();
            }
            //    public void onClick(View v) {
            //       bni.setBackground(getDrawable(R.drawable.buttonselector));
        });
       }

    private void initRetro() {
        final String BASE_URL = "https://geni.topanlabs.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        apiService =
                retrofit.create(checkoutint.class);
        orstatus = retrofit.create(mahaint.class);
        authenticator = retrofit.create(tokenint.class);
        getToken();
        pbar.setVisibility(View.VISIBLE);

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
                cekAdaNIM();

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
    private void buatEntry() {
        checkoutmodel checkoutmodel = new checkoutmodel(nim, tanggalString, bank, "0", "nope", jamString);
        Call<checkoutmodel> call = apiService.createOrder(checkoutmodel, token);
        call.enqueue(new Callback<checkoutmodel>() {
            @Override
            public void onResponse(Call<checkoutmodel> call, Response<checkoutmodel> response) {
                checkoutmodel checkoutmodel = response.body();
                nominal = checkoutmodel.getJumlah();
                nominaltxt.setText(nominal);
                String jam = checkoutmodel.getJam();
                pbar.setVisibility(View.GONE);
                Intent intent = new Intent(checkoutPay.this, waitTransfer.class);


                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<checkoutmodel> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    private void cekAdaNIM() {


        Call<checkoutmodel> call = apiService.getOrder(nim, token);
        call.enqueue(new Callback<checkoutmodel>() {
            @Override
            public void onResponse(Call<checkoutmodel> call, Response<checkoutmodel> response) {
                Context context = getApplicationContext();

                int statusCode = response.code();
                if (statusCode == 404) {
                    buatEntry();
                } else {
                    checkoutmodel checkoutmodel = response.body();
                    nominal = checkoutmodel.getJumlah();
                    String jam = checkoutmodel.getJam();
                    nominaltxt.setText(nominal);
                    Intent intent = new Intent(checkoutPay.this, waitTransfer.class);
                    intent.putExtra("nimku", nim);
                    intent.putExtra("nominal", nominal);
                    intent.putExtra("bank", bank);
                    intent.putExtra("jam", jam);

                    startActivity(intent);
                    finish();
                    pbar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<checkoutmodel> call, Throwable t) {

                Context context = getApplicationContext();
                CharSequence text = "Error DVN12. Mohon klik bantuan jika berlanjut.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            //showDialog();
        });

    }
}

