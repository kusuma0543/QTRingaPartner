package com.ringaapp.ringapartner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.db_javaclasses.Bannerlist;
import com.ringaapp.ringapartner.db_javaclasses.Reviewlistpartner;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;
import com.ringaapp.ringapartner.javaclasses.Helper;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class ProfilePage extends AppCompatActivity {
    private ListView listview_service,review_list;
    FlipperLayout flipperLayout_service;
    String profilpageuid;
    private ImageView partner_image;
    private TextView partner_name,partner_address,partner_about,partner_rating,partner_ratingtwo,
            partner_activesince,partner_rating_count;
    Float ratemap;
    private RatingBar partner_ratingbar;
    private Button partner_profeditbut,servpdet_visitingcharge;
    String spartner_uid,spartner_name,spartner_address,spartner_image,spartner_about
            ,spartner_rating,spartner_ratingtotral,spartner_visitingcharge,spartner_activesince;

    private SessionManager session;
    private SQLiteHandler db;

ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        final HashMap<String, String> user = db.getUserDetails();
        profilpageuid=user.get("uid");

        flipperLayout_service = findViewById(R.id.flipper_layout_service);
        listview_service =  findViewById(R.id.s_service);

        partner_image=findViewById(R.id.partner_profimage);
        partner_name=findViewById(R.id.servpdet_name);
        partner_address=findViewById(R.id.servpdet_localityname);
        partner_about=findViewById(R.id.servpdet_desc);
        partner_ratingbar=findViewById(R.id.servpdet_ratingbar);
        partner_ratingtwo=findViewById(R.id.servpdet_totalreview);
        servpdet_visitingcharge=findViewById(R.id.servpdet_visitingcharge);
        partner_activesince=findViewById(R.id.servpdet_lastupdated);
        review_list=findViewById(R.id.review_part_list);
        partner_rating_count=findViewById(R.id.servpdet_ratingcount);
      //  webView = (WebView)findViewById(R.id.webView);
       // partner_profeditbut=findViewById(R.id.partner_profbut);



        String URLL = "http://quaticstech.in/projecti1andro/android_partner_servprovimagesslider.php?partner_uid="+profilpageuid;
    new kilomilo().execute(URLL);
//        WebView webView = (WebView) findViewById(R.id.webView);
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.loadUrl("http://quaticstech.in/projecti1/ringa/service_profile_lightbox.php?partnerxid="+profilpageuid);
        getmyprofiledetails(profilpageuid);

        String review_partner = "http://quaticstech.in/projecti1andro/android_users_partner_review.php?partner_uid=" + profilpageuid;
        new Reviewlistpart().execute(review_partner);

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
     view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
         @Override
         public void onFlipperClick(FlipperView flipperView) {
            Intent ing=new Intent(ProfilePage.this,ImageExtend.class);
            ing.putExtra("imagesuid",profilpageuid);
            startActivity(ing);
         }
     });

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
                        spartner_rating=users.getString("partner_avg");
                        spartner_ratingtotral=users.getString("partner_total");
                        spartner_visitingcharge=users.getString("partner_visitingfee");
                        spartner_activesince=users.getString("partner_active_since");

                        partner_name.setText(spartner_name);
                        partner_address.setText(spartner_address);
                        partner_about.setText(spartner_about);
                        ratemap = Float.parseFloat(spartner_rating);
                        partner_ratingbar.setRating(ratemap);
///                        partner_rating.setText(spartner_rating+"/5");
                        partner_ratingtwo.setText(spartner_rating+"/5");
                        partner_rating_count.setText("Based on number of Reviews :"+spartner_ratingtotral);
                        servpdet_visitingcharge.setText("\u20B9."+spartner_visitingcharge);
                        partner_activesince.setText(spartner_activesince);


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


    public class Reviewlistpart extends AsyncTask<String, String, List<Reviewlistpartner>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Reviewlistpartner> doInBackground(String... params) {
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
                List<Reviewlistpartner> fda = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Reviewlistpartner reviwl = gson.fromJson(finalObject.toString(), Reviewlistpartner.class);
                    fda.add(reviwl);
                }
                return fda;
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
        protected void onPostExecute(final List<Reviewlistpartner> rplist) {
            super.onPostExecute(rplist);
            if (rplist!= null)
            {

                ReviewPartner adapter = new ReviewPartner(getApplicationContext(), R.layout.review_card_list, rplist);
                // review_list.setAdapter(adapter);

                review_list.setAdapter(adapter);
                Helper.getListViewSize(review_list);


                adapter.notifyDataSetChanged();


            }
            else
            {
                Toast.makeText(getApplicationContext(),"No Services available for your selection", Toast.LENGTH_SHORT).show();
            }
        }
    }public class ReviewPartner extends ArrayAdapter {
        private List<Reviewlistpartner> ReviewListpar;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        ReviewPartner(Context context, int resource, List<Reviewlistpartner> objects) {
            super(context, resource, objects);
            ReviewListpar = objects;
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
            final ReviewPartner.ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                holder = new ReviewPartner.ViewHolder();

                holder.reviwer_name =  convertView.findViewById(R.id.review_name);
                holder.reviwer_place =  convertView.findViewById(R.id.review_place);
                holder.reviwer_feedback =  convertView.findViewById(R.id.review_feedback);
                holder.reviwer_profile_image = convertView.findViewById(R.id.review_profile_image);
                convertView.setTag(holder);
            }//ino
            else {
                holder = (ReviewPartner.ViewHolder) convertView.getTag();
            }
            final Reviewlistpartner reviewpartlist = ReviewListpar.get(position);
            holder.reviwer_name.setText(reviewpartlist.getUser_name());
            holder.reviwer_place.setText(reviewpartlist.getUser_address_cityname());
            holder.reviwer_feedback.setText(reviewpartlist.getUser_feedback());
            Picasso.with(context).load(reviewpartlist.getUser_profile_image()).fit().error(R.drawable.pinns).fit().
                    into(holder.reviwer_profile_image);
            return convertView;
        }

        class ViewHolder {
            public TextView reviwer_name,reviwer_place,reviwer_feedback;
            public CircleImageView reviwer_profile_image;


        }
    }
}
