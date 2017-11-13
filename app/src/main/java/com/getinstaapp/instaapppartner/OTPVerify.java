package com.getinstaapp.instaapppartner;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;

public class OTPVerify extends AppCompatActivity implements View.OnFocusChangeListener,View.OnClickListener,View.OnKeyListener,TextWatcher {
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinHiddenEditText;
    private Button butotp_verify;
    private TextView tvotp_mobile,tv_otpresend;
    String fromforgot,last_number;
    private static final String FORMAT = "%02d:%02d";
    CountDownTimer bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        Intent intent=getIntent();

        last_number=intent.getStringExtra("mobile_number");
        fromforgot=intent.getStringExtra("fromforgot");

        mPinFirstDigitEditText = (EditText) findViewById(R.id.pinone);
        mPinSecondDigitEditText = (EditText) findViewById(R.id.pintwo);
        mPinThirdDigitEditText = (EditText) findViewById(R.id.pinthree);
        mPinForthDigitEditText = (EditText) findViewById(R.id.pinfour);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
        tvotp_mobile=(TextView) findViewById(R.id.tvotp_mobile);
        final TextView countdown = (TextView) findViewById(R.id.countdown);
        butotp_verify=(Button) findViewById(R.id.butotp_verify);
        tv_otpresend=(TextView) findViewById(R.id.tvotp_resend);
        tv_otpresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
insertme(last_number);
            }
        });
        tvotp_mobile.setText(last_number);

        bb= new CountDownTimer(50000,1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                countdown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }


            public void onFinish() {
                bb.cancel();
                Toast.makeText(OTPVerify.this, "Please click Resend to get OTP", Toast.LENGTH_SHORT).show();
                //  Intent intent=new Intent(OTPVerify.this,LoginActivity.class);

                // startActivity(intent);
                finish();


            }

        }.start();
        setPINListeners();

        butotp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1=mPinFirstDigitEditText.getText().toString().trim();
                String s2=mPinSecondDigitEditText.getText().toString().trim();
                String s3=mPinThirdDigitEditText.getText().toString().trim();
                String s4=mPinForthDigitEditText.getText().toString().trim();
                String s=s1+s2+s3+s4;
                otp_check(last_number,s);


            }
        });

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
                    showSoftKeyboard(mPinHiddenEditText);
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
            mPinForthDigitEditText.setText(s.charAt(3) + "");
        }
    }
    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.ggg));
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.ggg));
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
    public void otp_check(final String sphone1,final String sphone2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_otpverify, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("user_det");
                        String uname1 = users.getString("partner_mobile");

                       // String uname2 = users.getString("partner_otp");

                        Intent intent=new Intent(OTPVerify.this,HomeScreen.class);
                        intent.putExtra("mobile_number",uname1);
                      //  intent.putExtra("partner_otp",uname2);
//                        if (fromforgot.equals("fromforgot"))
//                        {
//                            Intent intent1=new Intent(OTPVerify.this,ResetPassword.class);
//                            intent.putExtra("mobile_number",uname1);
//                            startActivity(intent1);
//                        }
//                        else
//                        {
                            startActivity(intent);
                            finish();
                       // }




//                        intent.putExtra("jijii",uname4);
//                        intent.putExtra("gkkk",uname5);
//
////                        SharedPreferences pref = PreferenceManager
////                                .getDefaultSharedPreferences(LoginActivity.this);
////                        SharedPreferences.Editor editor = pref.edit();
////                        editor.putString ("uid", uname3);
////                        editor.putString ("uname", uname5);
////                        editor.commit();
//


                        //   Toast.makeText(getApplicationContext(),mobile_number,Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
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
                insert.put("partner_mobile",sphone1);
                insert.put("partner_otp", sphone2);

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

                params.put("partner_mobile",s1);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
