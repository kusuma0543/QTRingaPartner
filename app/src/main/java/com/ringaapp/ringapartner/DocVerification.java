package com.ringaapp.ringapartner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DocVerification extends AppCompatActivity {
String partner_passess,partner_uidpartner;
private Button verify_proceedbut;
    private ProgressDialog dialog;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_verification);
        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
     //   dialog.show();
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        partner_uidpartner=preferences.getString("useruidentire","");

        final HashMap<String, String> user = db.getUserDetails();
        partner_uidpartner=user.get("uid");

       insertmybbokingdetails(partner_uidpartner);

    }
    public void insertmybbokingdetails(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_checkdocumentver, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");


                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        partner_passess = users.getString("partner_passess");
                        if(partner_passess.equals("0"))
                        {
                            setContentView(R.layout.activity_doc_verification);

                        }
                        else {
                            setContentView(R.layout.dec_verification_success);
                            verify_proceedbut=findViewById(R.id.verify_proceed);
                            dialog.dismiss();
                            verify_proceedbut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(DocVerification.this,CategoryMain.class));
                                    }
                                });
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Just Wait a moment.Documents is under Verification!",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("partner_uid", s1);



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
