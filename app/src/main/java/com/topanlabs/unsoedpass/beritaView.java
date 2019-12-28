package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class beritaView extends AppCompatActivity {
String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_view);
        WebView webView = (WebView) findViewById(R.id.webview);
        final ProgressBar pbar = findViewById(R.id.pbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Happening right now");
        url = getIntent().getStringExtra("url");
        Log.d("tadigita", url);
        pbar.setMax(100);
        pbar.setProgress(1);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getSupportActionBar().setSubtitle(title); //Set Activity tile to page title.
            }
            @Override
            public void onProgressChanged(WebView view, int progress) {


                    pbar.setProgress(progress);


               if (progress == 100) {
                    pbar.setVisibility(WebView.GONE);

               }
            }

        });
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        }
}
