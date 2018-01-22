package com.ringaapp.ringapartner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;

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

public class JobsListCount extends AppCompatActivity {
    private GridView home_gridview;
String jobbuy_partner_uid;
    private SessionManager session;
    private SQLiteHandler db;
    TextView jobstotal_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list_count);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();
        jobbuy_partner_uid = user.get("uid");
        getJobsMyCount(jobbuy_partner_uid);

        jobstotal_tv=findViewById(R.id.getjobs_total);
        home_gridview=findViewById(R.id.home_gridview);
        new kilomilo().execute(GlobalUrl.partner_buyjobslist_ret);
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
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.buyjobs, movieModelList);
                home_gridview.setAdapter(adapter);
                home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        buyjobs categorieslist = movieModelList.get(position);


                        new SweetAlertDialog(JobsListCount.this).
                                setTitleText("Confirm Recharge").
                                setContentText("Your Recharge Amount is\n "+categorieslist.getJobs_value()+"\n"
                                +"for "+categorieslist.getJobs_count()).showCancelButton(true)
                                .setConfirmText("Buy Jobs").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();

                            }
                        }).show();
                    }
                });

                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
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
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
            final ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new ViewHolder();
                holder.menuname=(TextView) convertView.findViewById(R.id.home_getrupees);
                holder.menuname2=(TextView) convertView.findViewById(R.id.home_getjobs);



                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            buyjobs categorieslist= movieModelList.get(position);

            holder.menuname.setText(categorieslist.getJobs_value());
            holder.menuname2.setText(categorieslist.getJobs_count());



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
                        String jobcounttool = users.getString("lead_partner_uid");
                        jobstotal_tv.setText(jobcounttool);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Jobs",Toast.LENGTH_SHORT).show();

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

}
