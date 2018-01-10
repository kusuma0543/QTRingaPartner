package com.ringaapp.ringapartner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.thomashaertel.widget.MultiSpinner;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartnerSerSel extends AppCompatActivity  {
    LinearLayout myLayout;
    private SearchableSpinner partnercatsel_spinner;
    SearchableSpinner partnersubcatsel_spinner;
    private ArrayList<String> students;
    private JSONArray result;
    ArrayList<String> studentss = new ArrayList<String>();
    private JSONArray results;
    String uisu,selec,selecteds, myserviceid,categget;
    private TextView selectedcateg,addmore_srvsel;
    private CardView cardviewcateg;
    private ArrayAdapter<String> adapter;
    private Button bcategseladd;
    private SessionManager session;
    private SQLiteHandler db;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_ser_sel);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent=getIntent();
        final String partnerslelocation=intent.getStringExtra("user_city");
        final String partneruid=intent.getStringExtra("oneuid");

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

      //  Toast.makeText(this, partnerslelocation, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, partneruid, Toast.LENGTH_SHORT).show();
        myLayout = findViewById(R.id.GridLayout1);
        partnercatsel_spinner =  findViewById(R.id.partnercatsel_spinner);
        partnersubcatsel_spinner =  findViewById(R.id.mySpinner);
        bcategseladd=findViewById(R.id.categseladd);
        selectedcateg=findViewById(R.id.textnone);
        addmore_srvsel=findViewById(R.id.addmore_srvsel);
        partnercatsel_spinner.setTitle("Select Category Item");

        partnercatsel_spinner.setPositiveButton("OK");
        students = new ArrayList<String>();
        studentss = new ArrayList<String>();
        int initialposition=partnercatsel_spinner.getSelectedItemPosition();
        partnercatsel_spinner.setSelection(initialposition, false);//clear selection
        addmore_srvsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PartnerSerSel.this, "Please Select Categories", Toast.LENGTH_SHORT).show();
            }
        });
        getData();


        partnercatsel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selec = parent.getItemAtPosition(position).toString();
                 categget=getCategNamez(position);
                    getsData(categget);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        partnersubcatsel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedcateg.setVisibility(View.INVISIBLE);
                bcategseladd.setVisibility(View.VISIBLE);
                selecteds = parent.getItemAtPosition(position).toString();
                 myserviceid=getsuubcategNamez(position);
                 //Toast.makeText(PartnerSerSel.this, myserviceid, Toast.LENGTH_SHORT).show();

                final Button myEditText = new Button(PartnerSerSel.this); // Pass it an Activity or Context
                myEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                myLayout.addView(myEditText);
                myEditText.setText("  "+selecteds);
                myEditText.setTextColor(Color.WHITE);
                myEditText.setPadding(30,16,30,6);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 4, 0, 12);
                myEditText.setLayoutParams(params);
                Drawable img = getApplicationContext().getResources().getDrawable( R.drawable.ic_cancel_black_24dp );
                myEditText.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                myEditText.setBackground(getDrawable(R.drawable.rounded_editsecond));
                myEditText.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);



                 addmyservices(partneruid,myserviceid,partnerslelocation,categget);

              //  Toast.makeText(PartnerSerSel.this, "my selected service is was"+myserviceid, Toast.LENGTH_SHORT).show();
                myEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        delmyservices(partneruid,myserviceid,partnerslelocation);

                    }
                });
                bcategseladd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1=new Intent(PartnerSerSel.this,HomeScreen.class);
                      intent1.putExtra("uidimagex",partneruid);
                        startActivity(intent1);
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }
    private void getData() {
        StringRequest stringRequest = new StringRequest(GlobalUrl.partner_catsel_spinner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("result");
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                students.add(json.getString("service_categ_name"));
                uisu = json.getString("service_categ_name");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        partnercatsel_spinner.setAdapter(new ArrayAdapter<String>(PartnerSerSel.this, android.R.layout.simple_dropdown_item_1line, students));
    }

    private void getsData(String s) {
        String users_updatedloc_servm = GlobalUrl.partner_subcatsel_spinner + "?service_categ_uid=" + s;
        StringRequest stringRequest = new StringRequest(users_updatedloc_servm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            results = j.getJSONArray("result");
                            getsStudents(results);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getsStudents(JSONArray j) {
        studentss.clear();
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                studentss.add(json.getString("service_subcateg_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, studentss);
        partnersubcatsel_spinner.setAdapter(adapter);
    }
    private String getCategNamez(int position){
        String serviceuid="";
        try {
            JSONObject json = result.getJSONObject(position);
            serviceuid = json.getString("service_categ_uid");
            Toast.makeText(PartnerSerSel.this,serviceuid , Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceuid;
    }
    private String getsuubcategNamez(int position){
        String serviceuid="";
        try {
            JSONObject json = results.getJSONObject(position);
            serviceuid = json.getString("service_subcateg_uid");
            Toast.makeText(PartnerSerSel.this,serviceuid , Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceuid;
    }
    public void addmyservices(final String spartneruid, final String spartnerselser,final String spartnersellocation,final String spartnercateg) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_selservices, new Response.Listener<String>() {
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
                params.put("partner_uid", spartneruid);
                params.put("service_subcateg_uid", spartnerselser);
                params.put("service_district_place",spartnersellocation);
                params.put("service_categ_uid",spartnercateg);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void delmyservices(final String sdpartneruid, final String sdpartnerselser,final String sdpartnerlocation) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_delservices, new Response.Listener<String>() {
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
                params.put("partner_uid", sdpartneruid);
                params.put("service_subcateg_uid", sdpartnerselser);
                params.put("service_district_place", sdpartnerlocation);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}

