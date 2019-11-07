package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Map;

public class masukCapcay extends AppCompatActivity {
    Button btnRefresh, btnLanjut;
    EditText edtCapcay;
    Map<String, String> kukis;
    ByteArrayInputStream winnyaw;
    Bitmap bmp;
    GetCapcay getCapcay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk_capcay);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnLanjut = findViewById(R.id.btnLanjut);

        edtCapcay = findViewById(R.id.edtCapcay);
        getCapcay =new GetCapcay();
        getCapcay.execute(new String[]{"KONTOL"});
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCapcay =new GetCapcay();
                getCapcay.execute(new String[]{"KONTOL"});

            }
        });
        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(masukCapcay.this, sinkronizer.class);
                i.putExtra("kukis", (Serializable) kukis);
                i.putExtra("fromCapcay",true);

                i.putExtra("capcayku", edtCapcay.getText().toString());
                startActivity(i);
                finish();
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
                Log.d("Winny", kukis.toString());

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

            ImageView image = (ImageView) findViewById(R.id.tempatCapcay);

            image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                    image.getHeight(), false));
            // winnyau.setText(kukis.toString());



        }
    }
}
