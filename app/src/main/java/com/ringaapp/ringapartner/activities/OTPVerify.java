package com.ringaapp.ringapartner.activities;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class OTPVerify extends AppCompatActivity implements View.OnFocusChangeListener,View.OnClickListener,View.OnKeyListener,TextWatcher, OTPListener {
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinHiddenEditText;
    private Button butotp_verify;
    private TextView tvotp_mobile,tv_otpresend,k,secondk;
    private static final String FORMAT = "%02d:%02d";
    CountDownTimer bb;
    String fromforgot,last_number,authsuid,partner_uname,checkdoc,cityName,otpemail,otpfulladress;
    private ProgressDialog dialog;

    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        if (isConnectedToNetwork()) {

            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());


            Intent intent = getIntent();

            last_number = intent.getStringExtra("mobile_number");
            fromforgot = intent.getStringExtra("fromforgot");
            authsuid = intent.getStringExtra("authuid");
            mPinFirstDigitEditText = (EditText) findViewById(R.id.pinone);
            mPinSecondDigitEditText = (EditText) findViewById(R.id.pintwo);
            mPinThirdDigitEditText = (EditText) findViewById(R.id.pinthree);
            mPinForthDigitEditText = (EditText) findViewById(R.id.pinfour);
            mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
            tvotp_mobile = (TextView) findViewById(R.id.tvotp_mobile);
            final TextView countdown = (TextView) findViewById(R.id.countdown);
            butotp_verify = (Button) findViewById(R.id.butotp_verify);
            tv_otpresend = (TextView) findViewById(R.id.tvotp_resend);
            tv_otpresend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertme(last_number);
                }
            });
            tvotp_mobile.setText(last_number);
            k = (TextView) findViewById(R.id.k);
            secondk = (TextView) findViewById(R.id.secondk);
            dialog = new ProgressDialog(OTPVerify.this);
            dialog = new ProgressDialog(OTPVerify.this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
            bb = new CountDownTimer(50000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                    countdown.setText("" + String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }


                public void onFinish() {
                    bb.cancel();
                    Toast.makeText(OTPVerify.this, "Please click Resend to get OTP", Toast.LENGTH_SHORT).show();
                    bb.cancel();
                    countdown.setVisibility(View.GONE);
                    k.setVisibility(View.GONE);
                    secondk.setVisibility(View.GONE);


                }
            }.start();
            setPINListeners();

            butotp_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String s1 = mPinFirstDigitEditText.getText().toString().trim();
                    String s2 = mPinSecondDigitEditText.getText().toString().trim();
                    String s3 = mPinThirdDigitEditText.getText().toString().trim();
                    String s4 = mPinForthDigitEditText.getText().toString().trim();
                    String s = s1 + s2 + s3 + s4;

                    if(s1.equals("")||s2.equals("")||s3.equals("")||s4.equals(""))
                    {
                        Toast.makeText(OTPVerify.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.show();

                        otp_check(last_number, s, authsuid);

                    }


                }
            });

        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(OTPVerify.this,OTPVerify.class);
                    startActivity(intent);
                }
            });
        }
        OtpReader.bind(this,"RINGAA");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pinone:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pintwo:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pinthree:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pinfour:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                   // showSoftKeyboard(mPinHiddenEditText);
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(mPinHiddenEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }
                break;


            default:
                break;
        }
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {

                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0) {
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
                            mPinHiddenEditText.post(new Runnable() {
                                @Override
                                public void run() {
                                    mPinHiddenEditText.setSelection(mPinHiddenEditText.getText().toString().length());
                                }
                            });
                        }

                        break;
                    }

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(mPinHiddenEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

            mPinForthDigitEditText.setText(s.charAt(3) + "");
        }
    }
    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.otp_bottom));
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.otp_bottom));
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }



    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View v) {

    }
    public void otp_check(final String sphone1,final String sphone2,final String sphone3) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_otpverify, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        dialog.cancel();
                        JSONObject users = jObj.getJSONObject("user_det");
                        String uname1 = users.getString("partner_mobilenumber");
                        String uname2=users.getString("partner_otp_identification");
                        String uname3=users.getString("partner_uid");
                        partner_uname=users.getString("partner_name");
                        otpemail=users.getString("partner_email");
                        otpfulladress=users.getString("partner_address");
                        String locality=users.getString("partner_locality");
                        String partner_cityname=users.getString("partner_cityname");
                        String latitude=users.getString("partner_latitude");
                        String longitude=users.getString("partner_longitude");
                        checkdoc=users.getString("partner_passess");
                        if(locality.equals(""))
                        {
                            verifyclick(uname1,uname2,uname3);

                        }
                        else
                        {
                                if(checkdoc.matches("")||equals("null")|| checkdoc.matches("null"))
                                {
                                    session.setLogin(true);

                                    Intent intentmm=new Intent(OTPVerify.this,DocVerification.class);
                                    intentmm.putExtra("mobile_number",uname1);
                                    intentmm.putExtra("otp_identification",uname2);
                                    intentmm.putExtra("oneuid",uname3);
                                    intentmm.putExtra("user_unamehome",partner_uname);
                                    intentmm.putExtra("user_city", partner_cityname);
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("useruidentire",uname3);
                                    editor.putString("user_city", partner_cityname);
                                    editor.apply();
                                    db.addUser(partner_uname, otpemail, uname3, uname1,otpfulladress,cityName,locality,latitude,longitude);

                                    startActivity(intentmm);

                                    finish();
                                }
                                else
                                {
                                    session.setLogin(true);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("useruidentire",uname3);
                                    editor.putString("user_city", partner_cityname);
                                    editor.apply();
                                    db.addUser(partner_uname, otpemail, uname3, uname1,otpfulladress,cityName,locality,latitude,longitude);

                                    startActivity(new Intent(OTPVerify.this,CategoryMain.class));
                                }

                        }
                    }
                    else
                    {
                        dialog.cancel();

                        Toast.makeText(getApplicationContext(),"Please enter correct otp",Toast.LENGTH_SHORT).show();
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
                insert.put("partner_mobilenumber",sphone1);
                insert.put("partner_otp_identification", sphone2);
                insert.put("partner_uid",sphone3);
                return insert;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public void insertme(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_resendotp, new Response.Listener<String>() {
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

                params.put("partner_mobilenumber",s1);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void verifyclick(final String s1,final String s2,final String s3)
    {
//        new SweetAlertDialog(OTPVerify.this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Location Selection")
//                .setContentText("Are you sure to use your location?")
//                .setCancelText("No,Select New ")
//                .setConfirmText("Yes,use it!")
//                .showCancelButton(true)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        gps = new TrackGps(OTPVerify.this);
//
//                        Double lat = gps.getLatitude();
//                        Double lng = gps.getLongitude();
//                        LatLng sydney = new LatLng(lat, lng);
//                        Geocoder geocoder = new Geocoder(OTPVerify.this, Locale.getDefault());
//                        // List<Address> addresses = null;
//                        try {
//                            List<Address> addresses = geocoder.getFromLocation(lat,lng, 1);
//                            final String addressn = addresses.get(0).getSubLocality();
//                           cityName = addresses.get(0).getLocality();
//                            final String adminArea = addresses.get(0).getAddressLine(0);
//                            final String pincode=addresses.get(0).getPostalCode();
//                            final String latitu=Double.toString(lat);
//                            final String longitu=Double.toString(lng);
//                            final String alladd=adminArea+" "+addressn+" "+cityName+" "+pincode;
////                        Toast.makeText(getApplicationContext(), "Your selected area : " + address, Toast.LENGTH_SHORT).show();
////
////                        Toast.makeText(getApplicationContext(), "Your selected city: " + cityName, Toast.LENGTH_SHORT).show();
//                            new SweetAlertDialog(OTPVerify.this, SweetAlertDialog.SUCCESS_TYPE)
//                                    .setTitleText("Your Location").setContentText(addressn).setConfirmText("OK")
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            if(checkdoc.equals("0"))
//                                            {
//                                                session.setLogin(true);
//
//                                                db.addUser(partner_uname, otpemail, s3, s1,alladd,cityName,addressn,latitu,longitu);
//
//                                                Intent intentmm=new Intent(OTPVerify.this,PartnerSerSel.class);
//
//                                                rideme(s3,alladd,cityName,addressn,pincode,latitu,longitu);
//
//                                                intentmm.putExtra("mobile_number",s1);
//                                                intentmm.putExtra("otp_identification",s2);
//                                                intentmm.putExtra("oneuid",s3);
//                                                intentmm.putExtra("user_unamehome",partner_uname);
//                                                intentmm.putExtra("user_city", cityName);
//                                                intentmm.putExtra("user_area", addressn);
//                                              //intentmm.putExtra("updtaedimage",uname5);
//                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
//                                                SharedPreferences.Editor editor = preferences.edit();
//                                                editor.putString("useruidentire",s3);
//                                                editor.putString("user_city", cityName);
//                                                //   editor.putString("user_area", addressn);
//                                                //   editor.putString("sharedaddress",addressn);
//                                                // editor.putString("sharedprofileimages",user_sharedimage);
//                                                editor.apply();
//                                                startActivity(intentmm);
//                                                finish();
//
//                                            }
//                                            else
//                                            {
//                                                session.setLogin(true);
//
//                                                db.addUser(partner_uname, otpemail, s3, s1,alladd,cityName,addressn,latitu,longitu);
//                                                startActivity(new Intent(OTPVerify.this,CategoryMain.class));
//                                            }
//
//                                        }
//                                    })
//                                    .show();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        //  Toast.makeText(getApplicationContext(), "Your Location was ", Toast.LENGTH_SHORT).show();
//                        sDialog.cancel();
//
//                    }
//                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {

                Intent intentm=new Intent(OTPVerify.this,PartnerMapSel.class);

                intentm.putExtra("mobile_number",s1);
                intentm.putExtra("otp_identification",s2);
                intentm.putExtra("usrmapemail",otpemail);
                intentm.putExtra("oneuid",s3);

                startActivity(intentm);
                finish();
//
//
//            }
//        })
//                .show();

    }


    public void rideme(final String uidthree, final String s1, final String s2, final String s3, final String s4, final String s5, final String s6) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_placepick, new Response.Listener<String>() {
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
                params.put("partner_uid",uidthree);
                params.put("partner_address",s1);
                params.put("partner_cityname",s2);
                params.put("partner_locality",s3);

                params.put("partner_pincode", s4);

                params.put("partner_latitude", s5);
                params.put("partner_longitude",s6);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public void otpReceived(String smsText) {
        //Do whatever you want to do with the text
        String otpnumbers=smsText.replaceAll("[^0-9]", "");
        otp_check(last_number, otpnumbers,authsuid);
        //  Toast.makeText(this,otpnumbers,Toast.LENGTH_LONG).show();
        //Log.d("Otp",smsText);
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
