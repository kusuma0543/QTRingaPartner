package com.ringaapp.ringapartner.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.activities.CategoryMain;
import com.ringaapp.ringapartner.db_javaclasses.buyjobs;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RechargeFrag extends Fragment {
    private GridView home_gridview;
    String jobbuy_partner_uid;
    private SessionManager session;
    private SQLiteHandler db;
    TextView jobstotal_tv; String jobcounttool;

    public static RechargeFrag newInstance() {
        RechargeFrag fragment= new RechargeFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recharge, container, false);


        session = new SessionManager(getActivity());
        db = new SQLiteHandler(getActivity());
        final HashMap<String, String> user = db.getUserDetails();
        jobbuy_partner_uid = user.get("uid");
        getJobsMyCount(jobbuy_partner_uid);

        jobstotal_tv=view.findViewById(R.id.getjobs_total);
        home_gridview=view.findViewById(R.id.home_gridview);
        new kilomilo().execute(GlobalUrl.partner_buyjobslist_ret);
        return view;
    }
    public class kilomilo extends AsyncTask<String, String, List<buyjobs>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<buyjobs> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<buyjobs> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    buyjobs catego = gson.fromJson(finalObject.toString(), buyjobs.class);
                    milokilo.add(catego);
                }
                return milokilo;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<buyjobs> movieModelList) {
            super.onPostExecute(movieModelList);
            if(movieModelList != null) {
   MovieAdapter adapter = new MovieAdapter(getActivity(), R.layout.buyjobs, movieModelList);
                home_gridview.setAdapter(adapter);
                home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final buyjobs  categorieslist = movieModelList.get(position);


                        new SweetAlertDialog(getActivity())
                                .setTitleText("Confirm Recharge")
                                .setContentText("Your Recharge Amount is\n "+"\u20B9."+categorieslist.getJobs_value()+"\n"
                                        +"for "+categorieslist.getJobs_count()+" leads").showCancelButton(true)
                                .setConfirmText("Buy Leads").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                updatemypurchasedet(jobbuy_partner_uid,categorieslist.getJobs_count(),categorieslist.getJobs_value(),jobcounttool);
                                startActivity(new Intent(getActivity(),CategoryMain.class));
                            }
                        }).show();
                    }
                });

                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class MovieAdapter extends ArrayAdapter {
        private List<buyjobs> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        MovieAdapter(Context context, int resource, List<buyjobs> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context =context;
            this.resource = resource;
            inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getViewTypeCount() {
            return 1;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MovieAdapter.ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new MovieAdapter.ViewHolder();
                holder.menuname=(TextView) convertView.findViewById(R.id.home_getrupees);
                holder.menuname2=(TextView) convertView.findViewById(R.id.home_getjobs);



                convertView.setTag(holder);
            }
            else {
                holder = (MovieAdapter.ViewHolder) convertView.getTag();
            }
            buyjobs categorieslist= movieModelList.get(position);

            holder.menuname.setText("\u20B9."+categorieslist.getJobs_value());
            holder.menuname2.setText(categorieslist.getJobs_count()+" Leads");



            return convertView;
        }
        class ViewHolder{
            private TextView menuname,menuname2;
        }
    }

    public void getJobsMyCount(final String sphone1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_getmyjobscount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");
                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("lead");
                        jobcounttool = users.getString("lead_partner_uid");
                        jobstotal_tv.setText(jobcounttool);

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No Jobs",Toast.LENGTH_SHORT).show();

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
                insert.put("partner_uid", sphone1);
                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void updatemypurchasedet(final String sphone1,final String sphone2,final String sphone3,final String sphone4) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_update_purchase_set, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> insert = new HashMap<String, String>();
                insert.put("partner_uid", sphone1);
                insert.put("purchased_jobs", sphone2);
                insert.put("purchased_value", sphone3);
                insert.put("jobs_added", sphone4);
                return insert;

            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
