package com.ringaapp.ringapartner.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;
import com.varunest.sparkbutton.SparkButton;

import java.util.HashMap;
import java.util.Map;

public class ServiceTracking extends AppCompatActivity {
    String tracking_bookingid, tracking_username, tracking_servicename, tracking_categ,
            tracking_usermobile, tracking_usermail, tracking_useraddress, tracking_acceptedbefore;


    TextView tracking_main_username, tracking_tvusername, tracking_tv_email, tracking_tv_usermobile,
            tracking_tv_categ, tracking_tv_subcateg, tracking_tvuser_address, tracking_tv_acceptedbefore;

    Button tracking_finished, tracking_usermsg, tracking_usercall;
    SparkButton tracking_butcompleted;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        Intent intent = getIntent();
        tracking_bookingid = intent.getStringExtra("partnerhome_bookingid");
        tracking_username = intent.getStringExtra("partnerhome_username");
        tracking_servicename = intent.getStringExtra("partnerhome_subcategname");
        tracking_usermobile = intent.getStringExtra("partnerhome_usermobile");
        tracking_usermail = intent.getStringExtra("partnerhome_usermail");
        tracking_useraddress = intent.getStringExtra("partnerhome_address");
        tracking_categ = intent.getStringExtra("partnerhome_sendcateg");
        tracking_acceptedbefore = intent.getStringExtra("partnerhome_sendaccpteddate");
        String checkintent = intent.getStringExtra("check");
        toolbar.setTitle(tracking_username);

        tracking_tvusername = findViewById(R.id.partnertrack_username);
        tracking_main_username = findViewById(R.id.parttrack_username);
        tracking_tv_email = findViewById(R.id.parttrack_email);
        tracking_tv_usermobile = findViewById(R.id.parttrack_mobile);
        tracking_tv_categ = findViewById(R.id.parttrack_categname);
        tracking_tv_subcateg = findViewById(R.id.parttrack_subcategname);
        tracking_tvuser_address = findViewById(R.id.parttrack_address);
        tracking_tv_acceptedbefore = findViewById(R.id.partnerhome_whenaccepted);

        tracking_finished = findViewById(R.id.finishedjob);
        tracking_butcompleted = findViewById(R.id.partnertrack_completed);
        tracking_usermsg = findViewById(R.id.msg_track);
        tracking_usercall = findViewById(R.id.call_track);

        tracking_tvusername.setText(tracking_username);
        tracking_tv_email.setText(tracking_usermail);
        tracking_tv_usermobile.setText(tracking_usermobile);
        tracking_tv_categ.setText(tracking_categ);
        tracking_tv_subcateg.setText(tracking_servicename);
        tracking_tvuser_address.setText(tracking_useraddress);
        tracking_main_username.setText(tracking_username);
        tracking_tv_acceptedbefore.setText("accepted before " + tracking_acceptedbefore + " days");

        if (checkintent.equals("checkintentfromgoing")) {
            playFacebookAnimation();
            tracking_finished.setVisibility(View.INVISIBLE);
            tracking_butcompleted.setVisibility(View.VISIBLE);

        } else if (checkintent.equals("checkintentfromfinish")) {
            tracking_finished.setVisibility(View.VISIBLE);
            tracking_butcompleted.setVisibility(View.INVISIBLE);

        }
        tracking_usercall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tracking_usermobile));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(ServiceTracking.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });

        tracking_butcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tracking_imgstart.setVisibility(View.INVISIBLE);
//                tracking_imgdone.setVisibility(View.VISIBLE);
                donejobs(tracking_bookingid);
                startActivity(new Intent(ServiceTracking.this,CategoryMain.class));
                finish();
            }
        });
    }

    public void donejobs(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_donejobs, new Response.Listener<String>() {
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("booking_uid",s1);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void playFacebookAnimation() {
        ((SparkButton) findViewById(R.id.partnertrack_completed)).playAnimation();
    }}
