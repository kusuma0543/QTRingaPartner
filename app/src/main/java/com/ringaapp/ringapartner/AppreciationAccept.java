package com.ringaapp.ringapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class AppreciationAccept extends AppCompatActivity {
String fromaccpet;
TextView textgif;
Button button2;
    GifImageView gifImageView1,gifImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciation_accept);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        fromaccpet=intent.getStringExtra("check");
        gifImageView1=findViewById(R.id.gif_accept);
        gifImageView2=findViewById(R.id.gif_reject);
        textgif=findViewById(R.id.textgif);
        button2=findViewById(R.id.button2);
        if(fromaccpet.equals("fromaccept"))
        {
            gifImageView1.setVisibility(View.VISIBLE);
            gifImageView2.setVisibility(View.INVISIBLE);
            textgif.setText("Congratulations! Check OnGoingJobs for job details");
        }
        else
        {
            gifImageView2.setVisibility(View.VISIBLE);
            gifImageView1.setVisibility(View.INVISIBLE);
            textgif.setText("Oops! You Missed the Job");

        }
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppreciationAccept.this,CategoryMain.class));
            }
        });
    }


}
