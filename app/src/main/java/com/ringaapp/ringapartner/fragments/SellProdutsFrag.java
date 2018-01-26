package com.ringaapp.ringapartner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.activities.DispSellProdDet;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SellProdutsFrag extends Fragment {
    private SessionManager session;
    private SQLiteHandler db;
    private SearchableSpinner partnerproductsel_spinner;
    private ArrayList<String> students;
    private JSONArray result;
    String sellprod_partneruid,uisu,selec,categget;

    private EditText et_prod_title,et_prod_desc,et_prod_cost;
    private String s_prod_title,s_prod_desc,s_prod_cost,s_prod_costs_getprod_uid;
    private Button but_upload;


    public static SellProdutsFrag newInstance()
    {
        SellProdutsFrag fragment= new SellProdutsFrag();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.fragment_sell_produts, container, false);
        session = new SessionManager(getActivity());
        db = new SQLiteHandler(getActivity());
        final HashMap<String, String> user = db.getUserDetails();
        sellprod_partneruid=user.get("uid");
           // partnerproductsel_spinner=view.findViewById(R.id.partnerproductsel_spinner);
         //   students = new ArrayList<String>();
          //  int initialposition = partnerproductsel_spinner.getSelectedItemPosition();
          //  partnerproductsel_spinner.setSelection(initialposition, false);//clear selection

            //getData();

//        partnerproductsel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                selec = parent.getItemAtPosition(position).toString();
//                categget = getCategNamez(position);
//                Toast.makeText(getActivity(), selec, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), categget, Toast.LENGTH_SHORT).show();
//
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        et_prod_title=view.findViewById(R.id.product_title);
        et_prod_desc=view.findViewById(R.id.product_desc);
        et_prod_cost=view.findViewById(R.id.product_cost);
        but_upload=view.findViewById(R.id.product_upload);
        but_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_prod_title=et_prod_title.getText().toString();
                s_prod_desc=et_prod_desc.getText().toString();
                s_prod_cost=et_prod_cost.getText().toString();
                if(s_prod_title.equals("")||s_prod_desc.equals("")||s_prod_cost.equals(""))
                {
                    Toast.makeText(getActivity(), "PLease enter all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    insertme(sellprod_partneruid,s_prod_cost,s_prod_title, s_prod_desc);

                }
            }
        });
        return view;

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        partnerproductsel_spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, students));
    }
    private String getCategNamez(int position){
        String serviceuid="";
        try {
            JSONObject json = result.getJSONObject(position);
            serviceuid = json.getString("service_categ_uid");
            // Toast.makeText(PartnerSerSel.this,serviceuid , Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceuid;
    }
    public void insertme(final String uidp,final String s1, final String s2,final String s3) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_insert_sellproduct, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        s_prod_costs_getprod_uid = users.getString("product_uid");
                        Intent intent=new Intent(getActivity(),DispSellProdDet.class);
                        intent.putExtra("send_prod_title",s_prod_title);
                        intent.putExtra("send_prod_desc",s_prod_desc);
                        intent.putExtra("send_prod_cost",s_prod_cost);
                        intent.putExtra("send_prod_uid",s_prod_costs_getprod_uid);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Checking Details",Toast.LENGTH_SHORT).show();

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
                params.put("partner_uid", uidp);

                params.put("product_price", s1);
                params.put("product_name", s2);
                params.put("product_details",s3);



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
