package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class beritaView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_view);
        WebView webView = (WebView) findViewById(R.id.webview);
        final ProgressBar pbar = findViewById(R.id.pbar);
        pbar.setMax(100);
        pbar.setProgress(1);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {


                    pbar.setProgress(progress);


               if (progress == 100) {
                    pbar.setVisibility(WebView.GONE);

               }
            }

        });
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://10.10.10.15/blabs");
        }
}
