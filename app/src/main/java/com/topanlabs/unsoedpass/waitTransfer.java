package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Thread.sleep;

public class waitTransfer extends AppCompatActivity {
mahaint orstatus;
tokenint authenticator;
checkoutint apiService;
String nim, token, status, nominal, bank, jam;
TextView txtstatus, txtrektopan, txttrfsebelum, txtnominal;
SharedPreferences mSettings;
Button statusbutton, copyrek, copynom, btncall;
SharedPreferences.Editor editor;
NumberFormat formatter;
String number;
String rekeningku;
int nominul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_transfer);
        Intent intent = getIntent();
        copyrek = findViewById(R.id.copyrek);
        copynom = findViewById(R.id.copynom);
        statusbutton = findViewById(R.id.statusbutton);
        btncall = findViewById(R.id.helpbutton);
        btncall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String url = "https://wa.me/62895425455070?text=Aku%20butuh%20bantuan%20mengenai%20MyUNSOED:";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }

        });
        statusbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initRetro();
            }
            //    public void onClick(View v) {
            //       bni.setBackground(getDrawable(R.drawable.buttonselector));
        });
        copyrek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("rekening", rekeningku);
                clipboard.setPrimaryClip(clip);
                CharSequence text = "Rekening tersalin";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(waitTransfer.this, text, duration);
                toast.show();
            }
        });
        copynom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("nominal", nominal);
                clipboard.setPrimaryClip(clip);
                CharSequence text = "Nominal tersalin";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(waitTransfer.this, text, duration);
                toast.show();
            }
        });
        txtstatus = findViewById(R.id.status);
        txtrektopan = findViewById(R.id.rektopan);
        txttrfsebelum = findViewById(R.id.trfsebelum);
        txtnominal = findViewById(R.id.nominal);
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        nim = mSettings.getString("nim", "nim");
        editor.putString("lagibeli", "ya");
        editor.apply();
        initRetro();

    }
    private void refreshStatus() {
        Call<mahasis> call = orstatus.getUser(nim, token);
        call.enqueue(new Callback<mahasis>() {
            @Override
            public void onResponse(Call<mahasis> call, Response<mahasis> response) {

                    mahasis mahasis = response.body();
                    String stator = mahasis.getBeli();

                    if (bank.equals("bni")) {
                        txtrektopan.setText("BNI - 043 1366 227");
                    } else if (bank.equals("bca")) {
                        txtrektopan.setText("BCA - ");
                    }
                    if (stator.equals("0")) {
                       status = "Status: Menunggu verifikasi transfer";
                } else if (stator.equals("1")) {
                        status = "Status: Pembelian berhasil!";
                }



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
               // toast.show();
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
    private void initRetro() {
        final String BASE_URL = "http://sandbox.topanlabs.com:8123";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        orstatus = retrofit.create(mahaint.class);
        authenticator = retrofit.create(tokenint.class);
        getToken();
        apiService = retrofit.create(checkoutint.class);


    }

    private void cekAdaNIM() {


        Call<checkoutmodel> call = apiService.getOrder(nim, token);
        call.enqueue(new Callback<checkoutmodel>() {
            @Override
            public void onResponse(Call<checkoutmodel> call, Response<checkoutmodel> response) {
                Context context = getApplicationContext();

                int statusCode = response.code();
                if (statusCode == 404) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(waitTransfer.this);

                    // set title dialog
                    alertDialogBuilder.setTitle("Order Tidak Ditemukan");

                    // set pesan dari dialog
                    alertDialogBuilder
                            .setMessage("Mohon ulangi dari awal.")
                            //.setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("Asiap", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // jika tombol diklik, maka akan menutup activity ini

                                    Intent w = new Intent(waitTransfer.this, MainActivity.class);
                                    editor.putString("lagibeli", "tidak");
                                    editor.apply();
                                    startActivity(w);
                                    finish();

                                }
                            });


                    // membuat alert dialog dari builder
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // menampilkan alert dialog
                    alertDialog.show();
                } else {
                    checkoutmodel checkoutmodel = response.body();
                    nominal = checkoutmodel.getJumlah();
                    nominul = Integer.parseInt(nominal);
                    String jam = checkoutmodel.getJam();
                    String banks = checkoutmodel.getBank();
                    String status = checkoutmodel.getStatus();
                    if (banks.equals("bni")) {
                        txtrektopan.setText("BNI - 043 1366 227");
                        rekeningku = "0431366227";
                    } else if (banks.equals("bca")) {
                        txtrektopan.setText("BCA - 046 1924 190");
                        rekeningku = "0461924190";
                    }
                    if (status.equals("nope")) {
                        status = "Status: Menunggu verifikasi transfer";
                    } else if (status.equals("pass")) {
                        status = "Status: Pembelian berhasil!";
                        editor.putString("lagibeli", "tidak");
                        editor.putString("premium", "ya");
                        editor.apply();
                        showaDialog("Pembelian Berhasil", "Terimakasih kawan, selamat menikmati UNSOED Pass premium! Semoga kuliahnya dimudahkan, hehe.", "none");

                    }
                    txtstatus.setText(status);

                    DateTimeFormatter dateStringFormat = DateTimeFormat
                            .forPattern("HH:mm");
                    DateTime date1 = dateStringFormat.parseDateTime(jam);
                    date1 = date1.plusHours(3);
                    String jamakh = dateStringFormat.print(date1);
                    txttrfsebelum.setText("Mohon transfer sebelum: " + jamakh);
                    txtstatus.setText(status);
                    formatter = new DecimalFormat("#,###,###");
                    number = formatter.format(nominul);
                    txtnominal.setText("Rp. " + number);

                    CharSequence text = "Status Terupdate";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


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
    private void showaDialog(String title, String pesan, final String konteks){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(waitTransfer.this);

        // set title dialog
        alertDialogBuilder.setTitle(title);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage(pesan)
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Asiap",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini

                            Intent w = new Intent(waitTransfer.this, MainActivity.class);
                            startActivity(w);
                            finish();

                    }
                });



        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}
