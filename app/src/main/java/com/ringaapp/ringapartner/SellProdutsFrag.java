package com.ringaapp.ringapartner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SellProdutsFrag extends Fragment {
    private SessionManager session;
    private SQLiteHandler db;
    private SearchableSpinner partnerproductsel_spinner;
    private ArrayList<String> students;
    private JSONArray result;
    String uisu,selec,selecteds, myserviceid,categget;
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
            partnerproductsel_spinner=view.findViewById(R.id.partnerproductsel_spinner);
            students = new ArrayList<String>();
            int initialposition = partnerproductsel_spinner.getSelectedItemPosition();
            partnerproductsel_spinner.setSelection(initialposition, false);//clear selection

            getData();

        partnerproductsel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selec = parent.getItemAtPosition(position).toString();
                categget = getCategNamez(position);
                Toast.makeText(getActivity(), selec, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), categget, Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {

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
}
