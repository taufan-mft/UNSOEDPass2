package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class TrialHabis extends AppCompatActivity {
Button beli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_habis);
       /* beli = (Button) findViewById(R.id.loginbutton);
        beli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TrialHabis.this, checkoutPay.class);
                startActivity(i);
            }
        });
        TextView txt= (TextView) findViewById(R.id.alasaniklan); //txt is object of TextView
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        }); */
        //getSupportActionBar().hide();
    }
}
