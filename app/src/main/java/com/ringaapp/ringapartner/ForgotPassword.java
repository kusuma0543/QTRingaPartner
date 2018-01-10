package com.ringaapp.ringapartner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    private EditText edforgotpswd;
    private Button butforgotpswd_otp;
    private String sforgot_mobile;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        edforgotpswd=(EditText) findViewById(R.id.edforgotpswd);
        butforgotpswd_otp=(Button) findViewById(R.id.butforgorpswd_otp);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        edforgotpswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    edforgotpswd.setTextColor(Color.RED);

                    edforgotpswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                }
            }
        });
        butforgotpswd_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edforgotpswd.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter Number", Toast.LENGTH_LONG).show();

                }
                else
                {
                    sforgot_mobile=edforgotpswd.getText().toString();
                    forgot_updateotp(sforgot_mobile);
                    Intent intent=new Intent(ForgotPassword.this,OTPVerifys.class);
                    String fromforgot="fromforgot";
                    intent.putExtra("fromforgot",fromforgot);
                    intent.putExtra("mobile_number",sforgot_mobile);

                    startActivity(intent);
                }

            }
        });

    }
    public void forgot_updateotp(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_forgorpass, new Response.Listener<String>() {
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
                params.put("partner_mobilenumber", s1);



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
