package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;


public class beritaView extends AppCompatActivity {
String url, judul;
Boolean showsub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_view);
        WebView webView = (WebView) findViewById(R.id.webview);
        ConstraintLayout conste =  findViewById(R.id.root);
        final ProgressBar pbar = findViewById(R.id.pbar);
        showsub = true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Happening right now");
        url = getIntent().getStringExtra("url");
        if (getIntent().getStringExtra("judul")!=null){
            judul = getIntent().getStringExtra("judul");
            getSupportActionBar().setTitle(judul);
        }
        if (getIntent().getBooleanExtra("showsub", true)!= true){
            showsub = false;
        }
        Log.d("tadigita", url);
        pbar.setMax(100);
        pbar.setProgress(1);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
            if (showsub) {
                getSupportActionBar().setSubtitle(title); //Set Activity tile to page title.
            }
            }
            @Override
            public void onProgressChanged(WebView view, int progress) {


                    pbar.setProgress(progress);


               if (progress == 100) {
                    pbar.setVisibility(view.GONE);
                   ConstraintSet constraintSet = new ConstraintSet();
                   constraintSet.clone(conste);
                   constraintSet.connect(R.id.webview,ConstraintSet.TOP,R.id.root,ConstraintSet.TOP,0);
                   constraintSet.applyTo(conste);

               }
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(conste);
                constraintSet.connect(R.id.webview,ConstraintSet.TOP,R.id.root,ConstraintSet.TOP,0);
                constraintSet.applyTo(conste);

                    view.loadUrl("file:///android_asset/errort.html");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ((url != null && (!url.contains("myunsoed")))&&(url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });
       /* webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl(
                        "javascript:(function() { " +
                                "$(\".menu\").hide();"+
                                //+ "element.parentNode.removeChild(element);" +
                                "})()");
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);*/
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build()).build();
        mAdView.loadAd(adRequest);
        }
}
