package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.topanlabs.unsoedpass.memo.memoModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class memoAdd extends AppCompatActivity {
TextInputEditText namatkul, txtRuangan, txtCatatan;
    AutoCompleteTextView txtJenis;
TextView txtTanggal, txtJam;
    SharedPreferences mSettings;
    SimpleDateFormat sdf;
    SharedPreferences.Editor editor;
kelasInt kelasService;
String tokenkita, kodekelas;
MaterialButton btnTanggal, btnJam, btnSend;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);
        btnTanggal = findViewById(R.id.btn_tanggal);
        btnJam= findViewById(R.id.btn_jam);
        btnSend = findViewById(R.id.btnSend);
        namatkul = findViewById(R.id.txtNamatkul);
        txtRuangan = findViewById(R.id.txtRuangan);
        txtCatatan= findViewById(R.id.txtcatatan);
        txtTanggal = findViewById(R.id.txtTanggal);
        txtJam = findViewById(R.id.txtJam);
        getSupportActionBar().setTitle("Tambah Memo");
        mSettings = getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        tokenkita = mSettings.getString("token","token");
        kodekelas = mSettings.getString("kodekelas","rai");
        String[] COUNTRIES = new String[] {"Tugas", "Tugas Besar", "Kuis"};
        final String BASE_URL = "https://api1.myunsoed.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        // matkul = new kelasModel("0","0","0");
        kelasService =
                retrofit.create(kelasInt.class);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);

        txtJenis =
                findViewById(R.id.txtJenis);
        txtJenis.setAdapter(adapter);
        Calendar myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                 sdf = new SimpleDateFormat(myFormat, Locale.US);

                int day = myCalendar.get(Calendar.DAY_OF_WEEK);
                String harini = "o";
                switch (day) {
                    case Calendar.SUNDAY:
                        harini = "Minggu";
                        break;
                    case Calendar.MONDAY:
                        harini = "Senin";
                        break;
                    case Calendar.TUESDAY:
                        harini = "Selasa";
                        break;
                    case Calendar.WEDNESDAY:
                        harini = "Rabu";
                        break;
                    case Calendar.THURSDAY:
                        harini = "Kamis";
                        break;
                    case Calendar.FRIDAY:
                        harini = "Jumat";
                        break;
                    case Calendar.SATURDAY:
                        harini = "Sabtu";
                        break;

                }
                txtTanggal.setText(harini+", "+sdf.format(myCalendar.getTime()));

            }
        };
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(memoAdd.this,R.style.AlertDialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnJam.setOnClickListener((View v) -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(memoAdd.this,R.style.AlertDialogTheme, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    //txtJam.setText(selectedHour + ":" + selectedMinute);
                    txtJam.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }
            }, hour, minute, true);//Yes 24 hour time
            //mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        btnSend.setOnClickListener((View v) -> {
            Log.v("nabila", Boolean.toString(!txtJenis.getText().toString().isEmpty()) +Boolean.toString(!txtCatatan.getText().toString().isEmpty())+ Boolean.toString(!txtRuangan.getText().toString().isEmpty())+
                    Boolean.toString(!txtTanggal.getText().toString().equals("Tanggal"))+ Boolean.toString(!txtJam.getText().toString().equals("Jam"))+ Boolean.toString(!namatkul.getText().toString().isEmpty()));
            if (txtJenis.getText().toString().isEmpty() || txtCatatan.getText().toString().isEmpty() ||  txtRuangan.getText().toString().isEmpty() ||
            txtTanggal.getText().toString().equals("Tanggal") || txtJam.getText().toString().equals("Jam") || namatkul.getText().toString().isEmpty()) {
                showDialog();
            } else {

                memoModel maukirim = new memoModel(kodekelas, namatkul.getText().toString(), txtJam.getText().toString(), txtRuangan.getText().toString(), sdf.format(myCalendar.getTime()), 0, txtJenis.getText().toString(), txtCatatan.getText().toString(), 0);
                Call<memoModel> call = kelasService.buatMemo(tokenkita, maukirim);
                call.enqueue(new Callback<memoModel>() {
                    @Override
                    public void onResponse(Call<memoModel> call, Response<memoModel> response) {
                        int statusCode = response.code();
                        if (statusCode == 201) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Berhasil menambah memo", Toast.LENGTH_SHORT);
                            toast.show();
                            //Intent i = new Intent(memoAdd.this, memoList.class);
                           // startActivity(i);
                           // finish();
                            onBackPressed();

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Gagal menambah matkul", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent i = new Intent(memoAdd.this, kelasPengganti.class);
                            startActivity(i);
                            finish();
                        }


                        //Log.d("raisan", mahasiswaArrayList.toString());
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<memoModel> call, Throwable t) {

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


    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(memoAdd.this, R.style.AlertDialogTheme2);

        // set title dialog
        alertDialogBuilder.setTitle("Eits, dilengkapi dulu yaa");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Pastiin tanggal, jam, serta semua isian ngga ada yang kosong.")
                //.setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke" ,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        dialog.cancel();

                    }
                });


        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
