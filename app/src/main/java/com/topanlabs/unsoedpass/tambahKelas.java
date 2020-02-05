package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tambahKelas extends AppCompatActivity {
    Button btnTanggal, btnJam, btnSend;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText txtTanggal, txtJam, txtNamatkul, txtRuangan;
    String kodekelas;
    String tokenkita;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    kelasInt kelasService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kelas);
        btnTanggal= findViewById(R.id.btnTanggal);
        txtTanggal = findViewById(R.id.txtTanggal);
        btnJam = findViewById((R.id.Jam));
        txtJam = findViewById(R.id.txtJam);
        txtNamatkul = findViewById(R.id.txtMatkul);
        txtRuangan = findViewById(R.id.txtRuangan);
        btnSend = findViewById(R.id.btnSend);
        mSettings = getSharedPreferences("Settings", 0);
        tokenkita = mSettings.getString("token","token");
        kodekelas = mSettings.getString("kodekelas","rai");
        myCalendar = Calendar.getInstance();
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        // matkul = new kelasModel("0","0","0");
        kelasService =
                retrofit.create(kelasInt.class);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                txtTanggal.setText(sdf.format(myCalendar.getTime()));

            }
        };
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(tambahKelas.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnJam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(tambahKelas.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //txtJam.setText(selectedHour + ":" + selectedMinute);
                        txtJam.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            kelasModel maukirim = new kelasModel(kodekelas, txtNamatkul.getText().toString(), txtJam.getText().toString(), txtRuangan.getText().toString(), txtTanggal.getText().toString());
                Call<kelasModel> call = kelasService.buatMatkul(tokenkita,maukirim);
                call.enqueue(new Callback<kelasModel>() {
                    @Override
                    public void onResponse(Call<kelasModel> call, Response<kelasModel> response) {
                        int statusCode = response.code();
                        if (statusCode == 201) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Berhasil menambah matkul", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent i = new Intent(tambahKelas.this, kelasPengganti.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Gagal menambah matkul", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent i = new Intent(tambahKelas.this, kelasPengganti.class);
                            startActivity(i);
                            finish();
                        }



                        //Log.d("raisan", mahasiswaArrayList.toString());
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<kelasModel> call, Throwable t) {

                        Context context = getApplicationContext();
                        CharSequence text = "Error TL12";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }

                    //showDialog();
                });

            }
        });
    }
}
