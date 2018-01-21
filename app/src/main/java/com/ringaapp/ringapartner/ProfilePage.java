package com.ringaapp.ringapartner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
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

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class ProfilePage extends AppCompatActivity {
    private ListView listview_service;
    FlipperLayout flipperLayout_service;
    String profilpageuid;
    private ImageView partner_image;
    private TextView partner_name,partner_address,partner_about;
    private Button partner_profeditbut;
    String spartner_uid,spartner_name,spartner_address,spartner_image,spartner_about;

    private SessionManager session;
    private SQLiteHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

//        final Intent intent=getIntent();
//        profilpageuid=intent.getStringExtra("uidtoprofile");

        final HashMap<String, String> user = db.getUserDetails();
        profilpageuid=user.get("uid");
        flipperLayout_service = findViewById(R.id.flipper_layout_service);
        listview_service =  findViewById(R.id.s_service);
        partner_image=findViewById(R.id.partner_profimage);
        partner_name=findViewById(R.id.partner_profname);
        partner_address=findViewById(R.id.partner_profaddress);
        partner_about=findViewById(R.id.partner_profintro);
        partner_profeditbut=findViewById(R.id.partner_profbut);
        String URLL = "http://quaticstech.in/projecti1andro/android_partner_servprovimagesslider.php?partner_uid="+profilpageuid;
        new kilomilo().execute(URLL);
        getmyprofiledetails(profilpageuid);
//        partner_profeditbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1=new Intent(ProfilePage.this,ProfileEdit.class);
//                intent1.putExtra("uidtoprofileedit",spartner_uid);
//                startActivity(intent1);
//            }
//        });
    }
        public class MovieAdap extends ArrayAdapter {
        private List<Bannerlist> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<Bannerlist> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context = context;
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
            final MovieAdap.ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                holder = new MovieAdap.ViewHolder();

                holder.textone = (TextView) convertView.findViewById(R.id.second_texttitle);
                // holder.nmn = (ImageView)convertView.findViewById(R.id.nmn);
                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            final Bannerlist ccitacc = movieModelList.get(position);

            FlipperView view = new FlipperView(getBaseContext());
            //Picasso.with(context).load(ccitacc.getPromotion_fullimage()).fit().error(R.drawable.load).fit().into(flipperLayout);
            view.setImageUrl(ccitacc.getPartner_images());
            flipperLayout_service.addFlipperView(view);


            return convertView;
        }

        class ViewHolder {
            public TextView textone;


        }
    }

    public class kilomilo extends AsyncTask<String, String, List<Bannerlist>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Bannerlist> doInBackground(String... params) {
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
                List<Bannerlist> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Bannerlist catego = gson.fromJson(finalObject.toString(), Bannerlist.class);
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
        protected void onPostExecute(final List<Bannerlist> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode== null)
            {
                Toast.makeText(getApplicationContext(),"No Services available for your selection", Toast.LENGTH_SHORT).show();
            }
            else
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.categorytwo, movieMode);
                listview_service.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
    public void getmyprofiledetails(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_profiledet, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");
                        spartner_uid = users.getString("partner_uid");
                        spartner_name=users.getString("partner_name");
                        spartner_address=users.getString("partner_address");
                        spartner_image=users.getString("partner_profile_pic");
                        spartner_about=users.getString("partner_about");

                        partner_name.setText(spartner_name);
                        partner_address.setText(spartner_address);
                        partner_about.setText(spartner_about);
                        //Picasso.with(getApplicationContext()).load(spartner_image).fit().error(R.drawable.ic_person_black_24dp).fit().into(partner_image);


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please check number and Password",Toast.LENGTH_SHORT).show();

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
                params.put("partner_uid",s1);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
