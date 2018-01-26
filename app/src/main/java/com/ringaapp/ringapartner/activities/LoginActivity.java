package com.ringaapp.ringapartner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout prof_section;
    private EditText sedlogin_mobile,sedlogin_pswd;
    private TextView tvlogin_forgot,tvlogin_singnup;
    private Button sbutlogin_login;
    private String sphone,spassword;
    private SessionManager session;
    private SQLiteHandler db;
    private  TextView show_pass;
    private Boolean isClicked = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prof_section=(LinearLayout) findViewById(R.id.prof_section);
        sedlogin_mobile=(EditText)findViewById(R.id.sedlogin_num);
        sedlogin_pswd=(EditText) findViewById(R.id.sedlogin_pswd);
        tvlogin_forgot=(TextView) findViewById(R.id.tvlogin_forgot);
        tvlogin_singnup=(TextView) findViewById(R.id.tvlogin_signup);
        sbutlogin_login=(Button) findViewById(R.id.sbutlogin_login);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        show_pass= findViewById(R.id.show_password);

        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    show_pass.setText("Hide");
                    sedlogin_pswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    show_pass.setText("Show");
                    sedlogin_pswd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, CategoryMain.class);
            startActivity(intent);
        }
        sedlogin_mobile.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    sedlogin_mobile.setTextColor(Color.RED);

                    sedlogin_mobile.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedlogin_pswd.setBackgroundResource(R.drawable.rounded_edittextred);

                    sedlogin_pswd.setTextColor(Color.BLACK);

                }

            }
        });
        sedlogin_pswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    sedlogin_pswd.setTextColor(Color.RED);

                    sedlogin_pswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedlogin_mobile.setBackgroundResource(R.drawable.rounded_edittextred);
                    sedlogin_mobile.setTextColor(Color.BLACK);
                }
            }
        });

        tvlogin_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });
        sbutlogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sedlogin_mobile.getText().toString().equals("") && sedlogin_pswd.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter fields", Toast.LENGTH_LONG).show();

                }
                else
                {

                    if (isConnectedToNetwork()) {
                        sphone = sedlogin_mobile.getText().toString();
                        spassword = sedlogin_pswd.getText().toString();
                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        dialog.setMessage("Loading. Please wait...");
                        dialog.show();
                        logininto(sphone,spassword);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please check your Internet", Toast.LENGTH_LONG).show();

                    }


                }
            }
        });
        tvlogin_singnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });


    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public void logininto(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                    if (abc)
                    { dialog.dismiss();
                        JSONObject users = jObj.getJSONObject("user_det");
                        String uname1 = users.getString("partner_mobilenumber");
                        String loginuid=users.getString("partner_uid");
                        Intent intent=new Intent(LoginActivity.this,OTPVerify.class);
                        intent.putExtra("mobile_number",uname1);
                        intent.putExtra("authuid",loginuid);
                        startActivity(intent);

                    }
                    else
                    {
                        dialog.cancel();

                        Toast.makeText(getApplicationContext(),"Please check number and Password",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("partner_mobilenumber", sphone1);
                insert.put("partner_password", sphone2);
                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}
