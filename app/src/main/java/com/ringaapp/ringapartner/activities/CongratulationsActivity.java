package com.ringaapp.ringapartner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ringaapp.ringapartner.R;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class CongratulationsActivity extends AppCompatActivity {
ImageView image_first,image_seond;
    KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image_first=findViewById(R.id.first_congratulations);
        image_seond=findViewById(R.id.second_congratulations);
        image_first.setVisibility(View.VISIBLE);
        konfettiView=findViewById(R.id.viewKonfetti);

        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(3000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(10, 4f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(30, 4000L);
        image_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_first.setVisibility(View.INVISIBLE);
                image_seond.setVisibility(View.VISIBLE);
            }
        });
        image_seond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_seond.setVisibility(View.INVISIBLE);
                image_first.setVisibility(View.VISIBLE);
                startActivity(new Intent(CongratulationsActivity.this,CategoryMain.class));
            }
        });

    }

}
