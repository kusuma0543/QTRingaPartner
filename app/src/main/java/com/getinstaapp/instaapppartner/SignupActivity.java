package com.getinstaapp.instaapppartner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout profil_section;
    private EditText sedsignup_name,sedsignup_mobile,sedsignup_mail,sedsignup_pswd,sedignup_referalcode;
    private RadioGroup shome_groupone,Shome_grouptwo;
    private RadioButton shome_oneradio,shom_tworadio;
    private CheckBox checkbox;
    private TextView tvsignup_signin,tvsignup_tc;
    private Button shome_submit;
    CatLoadingView mView;
    private String emailInput,emailPattern;
    private String sname,semail,smobile,spassword,sradio_one,sradio_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        profil_section=(LinearLayout) findViewById(R.id.poo);
        sedsignup_name=(EditText) findViewById(R.id.sedsignup_name);
        sedsignup_mail=(EditText) findViewById(R.id.sedsignup_mail);
        sedsignup_mobile=(EditText) findViewById(R.id.sedsignup_mobile);
        sedsignup_pswd=(EditText) findViewById(R.id.sedsignup_pswd);
        sedignup_referalcode=(EditText) findViewById(R.id.sedsignup_referalcode);
        shome_groupone=(RadioGroup) findViewById(R.id.shome_radioone);
        Shome_grouptwo=(RadioGroup) findViewById(R.id.shome_radiotwo);
        checkbox=(CheckBox) findViewById(R.id.shome_checkbox);
        tvsignup_signin=(TextView) findViewById(R.id.tvsignup_signin);
        tvsignup_tc=(TextView) findViewById(R.id.tvsignup_tc);

        shome_submit=(Button) findViewById(R.id.sbutsignup_signup);
        shome_submit.setOnClickListener(this);

        sedsignup_name.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    sedsignup_name.setTextColor(Color.RED);
                    sedsignup_name.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedsignup_mail.setTextColor(Color.BLACK);
                    sedsignup_mobile.setTextColor(Color.BLACK);
                    sedsignup_pswd.setTextColor(Color.BLACK);
                    sedignup_referalcode.setTextColor(Color.BLACK);
                }
            }
        });
        sedsignup_mail.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    sedsignup_mail.setTextColor(Color.RED);
                    sedsignup_mail.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedsignup_name.setTextColor(Color.BLACK);
                    sedsignup_mobile.setTextColor(Color.BLACK);
                    sedsignup_pswd.setTextColor(Color.BLACK);
                    sedignup_referalcode.setTextColor(Color.BLACK);
                }
            }
        });
        sedsignup_mobile.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                   sedsignup_mobile.setTextColor(Color.RED);
                    sedsignup_mobile.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedsignup_mail.setTextColor(Color.BLACK);
                    sedsignup_name.setTextColor(Color.BLACK);
                    sedsignup_pswd.setTextColor(Color.BLACK);
                    sedignup_referalcode.setTextColor(Color.BLACK);
                }
            }
        });
        sedsignup_pswd.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    sedsignup_pswd.setTextColor(Color.RED);
                    sedsignup_pswd.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedsignup_mail.setTextColor(Color.BLACK);
                    sedsignup_mobile.setTextColor(Color.BLACK);
                    sedsignup_name.setTextColor(Color.BLACK);
                    sedignup_referalcode.setTextColor(Color.BLACK);

                }
            }
        });
        sedignup_referalcode.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){
                    sedignup_referalcode.setTextColor(Color.RED);
                    sedignup_referalcode.setBackgroundResource( R.drawable.edittext_afterseslect);
                    sedsignup_mail.setTextColor(Color.BLACK);
                    sedsignup_mobile.setTextColor(Color.BLACK);
                    sedsignup_name.setTextColor(Color.BLACK);
                    sedsignup_pswd.setTextColor(Color.BLACK);

                }
            }
        });
        tvsignup_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        tvsignup_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignupActivity.this, TermsAndConditions.class);
//                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sbutsignup_signup:
                nsignin();
               // mView = new CatLoadingView();

               // mView.show(getSupportFragmentManager(), "");

                break;
        }


    }
    private void nsignin() {
        emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        emailInput = sedsignup_mail.getText().toString().trim();

        int selectedId = shome_groupone.getCheckedRadioButtonId();
        int selectedIdtwo=Shome_grouptwo.getCheckedRadioButtonId();

        shome_oneradio = (RadioButton) findViewById(selectedId);
        shom_tworadio=(RadioButton) findViewById(selectedIdtwo);

        sname = sedsignup_name.getText().toString();
        semail = sedsignup_mail.getText().toString();
        smobile = sedsignup_mobile.getText().toString();
        spassword = sedsignup_pswd.getText().toString();
        sradio_one=shome_oneradio.getText().toString();
        sradio_two=shom_tworadio.getText().toString();





        Toast.makeText(SignupActivity.this,
                shome_oneradio.getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(SignupActivity.this,
                shom_tworadio.getText(), Toast.LENGTH_SHORT).show();

        if (sedsignup_name.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();

        }
        else {
            if (emailInput.matches(emailPattern)) {



                if (isConnectedToNetwork()) {
                    if (sedsignup_mobile.length() == 10) {
                        if (checkbox.isChecked()) {
                           // Toast.makeText(getApplication(),sradio_one,Toast.LENGTH_SHORT).show();
                           // Toast.makeText(getApplication(),sradio_two,Toast.LENGTH_SHORT).show();
                           insertme(sname, semail, smobile, spassword, sradio_one, sradio_two);
                            Toast.makeText(SignupActivity.this, "THANK YOU!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, OTPVerify.class);
                            intent.putExtra("mobile_number",smobile);


                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "Please check the Terms & Comditions m", Toast.LENGTH_SHORT).show();


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Valid Mobile Number",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignupActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                    if (sedsignup_mail.getText().toString().equals("") && sedsignup_mobile.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter fields", Toast.LENGTH_LONG).show();
                    }


                }


            } else {
                Toast.makeText(getApplicationContext(), "Invalid email address",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public void insertme(final String s1, final String s2,final String s3,final String s4,final String s5,final String s6) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_signup, new Response.Listener<String>() {
            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean abc = jObj.getBoolean("exits");
//
//
//                    if (abc)
//                    {
//                        JSONObject users = jObj.getJSONObject("users_detail");
//                        String uname1 = users.getString("mobile_number");
//
//
//
//                        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
//                        intent.putExtra("mobile_number",uname1);
//
//
//                        startActivity(intent);
//
//
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Account already exists!",Toast.LENGTH_SHORT).show();
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("partner_name", s1);
                params.put("partner_email", s2);
                params.put("partner_mobile",s3);
                params.put("partner_password",s4);
                params.put("partner_gender",s5);
                params.put("partner_type",s6);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
