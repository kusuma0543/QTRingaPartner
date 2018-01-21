package com.ringaapp.ringapartner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jetradar.desertplaceholder.DesertPlaceholder;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isConnectedToNetwork()) {
            setContentView(R.layout.activity_splash);
            Intent i = new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Splash.this,Splash.class);
                    startActivity(intent);
                }
            });
        }

    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
