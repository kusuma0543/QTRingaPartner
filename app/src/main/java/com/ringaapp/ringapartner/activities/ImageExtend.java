package com.ringaapp.ringapartner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;

import java.util.HashMap;

public class ImageExtend extends AppCompatActivity {

    private SessionManager session;
    private SQLiteHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_extend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent it=getIntent();
        String jfg=it.getStringExtra("images");
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        final HashMap<String, String> user = db.getUserDetails();
         String pfdgrofilpageuid=user.get("uid");
         String pfdgrofilpagename=user.get("name");
         toolbar.setTitle(pfdgrofilpagename);



        WebView webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("http://quaticstech.in/projecti1/ringa/service_profile_lightbox.php?partnerxid="+pfdgrofilpageuid);

    }

}
