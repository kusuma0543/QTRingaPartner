package com.ringaapp.ringapartner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AcceptReject extends AppCompatActivity {
    private String accrej_username,accrej_selsubcateg,accrej_bookingdesc,accrej_bookingid;
    private TextView partnerhome_username,partnerhome_subcateg,partnerhome_desc;
    private Button partneraccreject_but,partneraccaccept_but;

    AlertDialog alertDialog1;
    CharSequence[] values = {" I am on other Project "," I cant do the Service right now",
            " Its not my Requirement "," I am out of Station "," My reason is not listed "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new com.ringaapp.ringapartner.MenusFragment()).commit();
        partnerhome_username=findViewById(R.id.partneraccept_username);
        partnerhome_subcateg=findViewById(R.id.partneraccept_usersubcateg);
        partnerhome_desc=findViewById(R.id.partneraccept_userdesc);

        partneraccreject_but=findViewById(R.id.partneraccrej_rejectbut);
        partneraccaccept_but=findViewById(R.id.partneraccrej_acceptbut);
        Intent intent1=getIntent();
        accrej_bookingid=intent1.getStringExtra("partnerhome_bookingid");
         accrej_username=intent1.getStringExtra("partnerhome_username");
        accrej_selsubcateg=intent1.getStringExtra("partnerhome_subcategname");
        accrej_bookingdesc=intent1.getStringExtra("partnerhome_bookingdesc");
            partnerhome_username.setText(accrej_username);
            partnerhome_subcateg.setText(accrej_selsubcateg);
            partnerhome_desc.setText(accrej_bookingdesc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        partneraccaccept_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptmeupdate(accrej_bookingid);
                startActivity(new Intent(AcceptReject.this,CategoryMain.class));
            }
        });
        partneraccreject_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialogWithRadioButtonGroup() ;
            }
        });
    }
    public void acceptmeupdate(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_updateaccept, new Response.Listener<String>() {
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
    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(AcceptReject.this);

        builder.setTitle("Select Your Reason for Rejection");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        String case0="I am on other Project";
                        Toast.makeText(AcceptReject.this, case0, Toast.LENGTH_LONG).show();
                        rejectmeupdate(accrej_bookingid,case0);
                        startActivity(new Intent(AcceptReject.this,CategoryMain.class));

                        break;
                    case 1:
                        String case1="I cant do the Service right now";
                        Toast.makeText(AcceptReject.this, case1, Toast.LENGTH_LONG).show();
                        rejectmeupdate(accrej_bookingid,case1);
                        startActivity(new Intent(AcceptReject.this,CategoryMain.class));

                        break;
                    case 2:

                        Toast.makeText(AcceptReject.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        String case2="Its not my Requirement";
                        rejectmeupdate(accrej_bookingid,case2);
                        startActivity(new Intent(AcceptReject.this,CategoryMain.class));

                        break;
                    case 3:

                        Toast.makeText(AcceptReject.this, "FOur Item Clicked", Toast.LENGTH_LONG).show();
                        String case3="I am out of Station";
                        rejectmeupdate(accrej_bookingid,case3);
                        startActivity(new Intent(AcceptReject.this,CategoryMain.class));

                        break;
                    case 4:

                        Toast.makeText(AcceptReject.this, "Five Item Clicked", Toast.LENGTH_LONG).show();
                        String case4="My reason is not listed";
                        rejectmeupdate(accrej_bookingid,case4);
                        startActivity(new Intent(AcceptReject.this,CategoryMain.class));

                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    public void rejectmeupdate(final String s1,final String s2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.getPartner_updatereject, new Response.Listener<String>() {
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
                params.put("service_partner_rejectedreason",s2);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
