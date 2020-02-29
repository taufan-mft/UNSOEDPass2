package com.topanlabs.unsoedpass.teammaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.topanlabs.unsoedpass.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class teamResult extends AppCompatActivity {
TextView txt1;
    ArrayList<String> namaorang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_result);
        txt1 = findViewById(R.id.textView3);
        Bundle bundle = getIntent().getExtras();
        namaorang = (ArrayList<String>) getIntent().getSerializableExtra("anggota");
        Log.d("NABILAA",String.valueOf((Serializable) namaorang));
        int jumgrup = Integer.parseInt(getIntent().getStringExtra("jumlahgrup"));
        Collections.shuffle(namaorang);
        int minOrang = (namaorang.size() / jumgrup) ;
        jumgrup -=1;
        Log.d("nabila","min:"+ Integer.toString(minOrang));
        String[][] arrayOfArrays = new String[jumgrup][];
        ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
        String placeholder="";
        for (int i = 0; i <= jumgrup; i++){
            ArrayList<String> group = new ArrayList<>();
            for (int a=0; a <= minOrang-1; a++){
                Log.d("NABILA", "apa"+namaorang.get(0));
                group.add(namaorang.get(0));
                namaorang.remove(0);
            }
            lists.add(group);
        }
        int grupkos = 0;
        int harusya = namaorang.size();
        Log.d("harusnya", Integer.toString(harusya));
        if (namaorang.size() > 0) {
            for (int i = 0; i <= harusya -1; i++)
            {
                Log.d("NABILAA","oke"+namaorang.size()+" "+i);
                // Log.d("NABILAA","oke"+String.valueOf((Serializable) namaorang));
                lists.get(grupkos).add(namaorang.get(0));
                namaorang.remove(0);
                if (grupkos != jumgrup) {
                    grupkos += 1;
                } else {
                    grupkos = 0;
                }
            }
        }
        Log.d("NABILAA","oke"+String.valueOf((Serializable) namaorang));
        String pholder = "";
        for (int i =0; i <= lists.size()-1; i++) {
            Log.d("nabila", "ini i" + i);
            pholder = pholder + "<br><b>Group " + Integer.toString(i+1)+"</b><br>";
            Log.d("pholder", pholder);
            for (int a =0; a <= lists.get(i).size()-1; a++){
                // pholder = "Group " + Integer.toString(i+1)+"\n";
                pholder = pholder + lists.get(i).get(a) +"<br>";
                Log.d("pholder", pholder);
            }
        }
        txt1.setText(Html.fromHtml(pholder));




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_timaker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, txt1.getText().toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
