package com.ringaapp.ringapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class ServiceTracking extends AppCompatActivity {
    String tracking_bookingid,tracking_username,tracking_servicename,tracking_usermobile,tracking_usermail,tracking_useraddress;
    private ImageView tracking_imgongoing,tracking_imgstart,tracking_imgdone;
     TextView tracking_tvusername,tracking_tv_servicename,tracking_tvusermobile,tracking_tvusermail,tracking_tvuser_address;
     Button tracking_butstart,tracking_butcompleted;

    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Service Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        Intent intent=getIntent();
        tracking_bookingid=intent.getStringExtra("partnerhome_bookingid");
        tracking_username=intent.getStringExtra("partnerhome_username");
        tracking_servicename=intent.getStringExtra("partnerhome_subcategname");
        tracking_usermobile=intent.getStringExtra("partnerhome_usermobile");
        tracking_usermail=intent.getStringExtra("partnerhome_usermail");
        tracking_useraddress=intent.getStringExtra("partnerhome_address");
        String checkintent=intent.getStringExtra("check");
        tracking_tvusername=findViewById(R.id.partnertrack_username);
        tracking_tv_servicename=findViewById(R.id.partnertrack_servicename);
        tracking_tvusermobile=findViewById(R.id.partnertrack_mobile);
        tracking_tvusermail=findViewById(R.id.partnertrack_mail);
        tracking_tvuser_address=findViewById(R.id.partnertrack_address);

        tracking_imgongoing=findViewById(R.id.tracking_ongoing);
        tracking_imgstart=findViewById(R.id.tracking_start);
        tracking_imgdone=findViewById(R.id.tracking_done);

        tracking_butstart=findViewById(R.id.partnertrack_start);
        tracking_butcompleted=findViewById(R.id.partnertrack_completed);


        tracking_tvusername.setText(tracking_username);
        tracking_tv_servicename.setText(tracking_servicename);
        tracking_tvusermobile.setText(tracking_usermobile);
        tracking_tvusermail.setText(tracking_usermail);
        tracking_tvuser_address.setText(tracking_useraddress);

        if(checkintent.equals("checkintentfromgoing"))
        {
            tracking_butstart.setVisibility(View.VISIBLE);
            tracking_butcompleted.setVisibility(View.VISIBLE);

        }
        else if(checkintent.equals("checkintentfromfinish"))
        {
            tracking_butstart.setVisibility(View.INVISIBLE);
            tracking_butcompleted.setVisibility(View.INVISIBLE);
            tracking_imgongoing.setVisibility(View.INVISIBLE);
            tracking_imgstart.setVisibility(View.INVISIBLE);
            tracking_imgdone.setVisibility(View.VISIBLE);
        }

        tracking_butstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracking_imgongoing.setVisibility(View.INVISIBLE);
                tracking_imgstart.setVisibility(View.VISIBLE);
                updatestartedjobs(tracking_bookingid);

            }
        });
        tracking_butcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracking_imgstart.setVisibility(View.INVISIBLE);
                tracking_imgdone.setVisibility(View.VISIBLE);
                donejobs(tracking_bookingid);
                startActivity(new Intent(ServiceTracking.this,CategoryMain.class));
                finish();
            }
        });
    }
    public void updatestartedjobs(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_startedjobs, new Response.Listener<String>() {
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

}
