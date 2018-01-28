package com.ringaapp.ringapartner.fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.ringaapp.ringapartner.activities.AppreciationAccept;
import com.ringaapp.ringapartner.activities.CategoryMain;
import com.ringaapp.ringapartner.db_javaclasses.home_accerejjobs;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class NewJobs extends Fragment {
TextView gettext;
    String partnerhome_partneruid;
    private ListView partnerhome_listview;
    private ProgressDialog dialog;
    String getmyrejectid;
    String URLL;
    String URLCOUNT,jobcounttool;
    String getmid;
    GifImageView homebut_buy;

     KonfettiView konfettiView;
    private final int  FIVE_SECONDS=5000;
    GifImageView gifImageView;
    AlertDialog alertDialog1;
    FrameLayout fragmentContainer;
    CharSequence[] values = {" I am on other Project "," I cant do the Service right now",
            " Its not my Requirement "," I am out of Station "," My reason is not listed "};
final Handler handler=new Handler();
    private SessionManager session;
    private SQLiteHandler db;
    public static NewJobs newInstance() {
        NewJobs fragment= new NewJobs();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_new_jobs, container, false);
        fragmentContainer = view. findViewById(R.id.contentContainer);
        session = new SessionManager(getActivity());
        db = new SQLiteHandler(getActivity());
        final HashMap<String, String> user = db.getUserDetails();
        partnerhome_partneruid=user.get("uid");
         partnerhome_listview=view.findViewById(R.id.partnerhome_listview);
         gifImageView=view.findViewById(R.id.gif_list);
        dialog=new ProgressDialog(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        gettext=view.findViewById(R.id.text_list);
          homebut_buy =view. findViewById(R.id.postad_partner);

        homebut_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "szmx", Toast.LENGTH_SHORT).show();
//                    Fragment fragment = new SellProdutsFrag();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.tab_new, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                    android.support.v4.app.Fragment selectedFragment = SellProdutsFrag.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentContainer, selectedFragment);
                    transaction.commit();
                }
            });


        konfettiView=view.findViewById(R.id.viewKonfetti);
        URLL = GlobalUrl.partner_homeaccrejjobs+"?partner_uid="+partnerhome_partneruid;
        new kilomilo().execute(URLL);
        scheduleSendLocation();
       return view;
    }
    public void scheduleSendLocation() {

        handler.postDelayed(new Runnable() {
            public void run() {
                URLL = GlobalUrl.partner_homeaccrejjobs+"?partner_uid="+partnerhome_partneruid;

                new kilomilo().execute(URLL);
                Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
    public class MovieAdap extends ArrayAdapter {
        private List<home_accerejjobs> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<home_accerejjobs> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context = context;
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
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                holder = new ViewHolder();

                holder.textname = (TextView) convertView.findViewById(R.id.partnerhome_username);
                holder.text_categname = (TextView)convertView.findViewById(R.id.partnerhome_usercategname);
                holder.textsubcategname = (TextView)convertView.findViewById(R.id.partnerhome_usersubcateg);
                holder.text_locality = (TextView)convertView.findViewById(R.id.partnerhome_userlocality);

                holder.textbookingid=(TextView)convertView.findViewById(R.id.partner_bookingid);
                holder.butrejectbut=convertView.findViewById(R.id.partneraccrej_rejectbut);
                holder.butaccept=convertView.findViewById(R.id.partneraccrej_acceptbut);
                holder.text_booking_created_date=convertView.findViewById(R.id.booking_created_date);

                convertView.setTag(holder);
            }//ino
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            home_accerejjobs ccitacc = movieModelList.get(position);
            holder.textname.setText(ccitacc.getUser_name());
            holder.text_categname.setText(ccitacc.getService_subcateg_name());
            holder.textsubcategname.setText(ccitacc.getService_categ_name());
            holder.text_locality.setText(ccitacc.getUser_address_cityname());
            holder.textbookingid.setText(ccitacc.getBooking_uid());
            holder.text_booking_created_date.setText(ccitacc.getService_booking_createddate());




//date accepted
            String strThatDay = holder.text_booking_created_date.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = formatter.parse(strThatDay);//catch exception
            } catch (ParseException e) {

                e.printStackTrace();
            }

            Calendar thatDay = Calendar.getInstance();
            thatDay.setTime(d);


            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c.getTime());


            long diff = c.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis

            long days = diff / (24 * 60 * 60 * 1000);
            holder.text_booking_created_date.setText("accepted before "+days+" days");
//




            Intent notificationIntent = new Intent(getContext(), CategoryMain.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            long[] v = {500,1000};

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("RingaApp User Request!")
                   // .setSound(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.sweety))
                  .setSound(uri)
                    .setColor(Color.RED)
                    .setContentIntent(pendingIntent)
                    .setContentText("You got a service request from "+ccitacc.getUser_name()+" on "+ccitacc.getService_subcateg_name()+"\nPlease check RingaApp Partner Dashboard"
                    );

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();


            builder.setStyle(bigText);
            builder.setVibrate(v);
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
            notificationManager.notify(0, notification);


                holder.butaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         getmid=holder.textbookingid.getText().toString();

                       // acceptmeupdate(getmid);
                      //  updatestartedjobs(getmid);
                        getJobsMyCount(partnerhome_partneruid);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("storingbookingid",getmid);
                        editor.apply();

                        handler.removeCallbacksAndMessages(null);
//
//                        OnGoingPartJobs fragment2 = new OnGoingPartJobs();
//                        FragmentManager fragmentManager = getFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.contentContainer, fragment2);
//                        fragmentTransaction.commit();



                    }
                });

                holder.butrejectbut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         getmyrejectid=holder.textbookingid.getText().toString();
                        handler.removeCallbacksAndMessages(null);
                        CreateAlertDialogWithRadioButtonGroup();


                    }
                });

            return convertView;
        }

        class ViewHolder {
            public TextView textname,text_categname,textsubcategname,text_locality,text_booking_created_date,textbookingid;
            public Button butaccept,butrejectbut;

        }
    }

    public class kilomilo extends AsyncTask<String, String, List<home_accerejjobs>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected List<home_accerejjobs> doInBackground(String... params) {
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
                List<home_accerejjobs> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    home_accerejjobs catego = gson.fromJson(finalObject.toString(), home_accerejjobs.class);
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
        protected void onPostExecute(final List<home_accerejjobs> movieMode) {
            super.onPostExecute(movieMode);
            dialog.dismiss();
            handler.removeCallbacksAndMessages(null);
            if (movieMode== null)
            {
                partnerhome_listview.setVisibility(View.INVISIBLE);
                gifImageView.setVisibility(View.VISIBLE);
                gettext.setVisibility(View.VISIBLE);


            }
            else
            {

                        konfettiView.build()
                                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED)
                                .setDirection(0.0, 359.0)
                                .setSpeed(1f, 5f)
                                .setFadeOutEnabled(true)
                                .setTimeToLive(4000L)
                                .addShapes(Shape.RECT, Shape.CIRCLE)
                                .addSizes(new Size(12, 5f))
                                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                                .stream(40, 5000L);

                gifImageView.setVisibility(View.INVISIBLE);
                gettext.setVisibility(View.INVISIBLE);
                partnerhome_listview.setVisibility(View.VISIBLE);

                MovieAdap adapter = new MovieAdap(getActivity(), R.layout.home_accerejjobs, movieMode);
                partnerhome_listview.setAdapter(adapter);
//                partnerhome_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        home_accerejjobs item = movieMode.get(position);
//                        Intent intent = new Intent(CategoryMain.this,AcceptReject.class);
//                        intent.putExtra("partnerhome_bookingid",item.getBooking_uid());
//                        intent.putExtra("partnerhome_subcategname",item.getService_subcateg_name());
//                        intent.putExtra("partnerhome_username",item.getUser_name());
//                        startActivity(intent);
//                    }
//                });

                adapter.notifyDataSetChanged();
            }
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

                        if(jobcounttool.matches("0")|| jobcounttool.equals("0"))
                        {
                            Fragment nextFrag= new RechargeFrag();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentContainer, nextFrag,"findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
                            Toast.makeText(getContext(), jobcounttool, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            String ki= preferences.getString("storingbookingid", "");

                           // String ki=pref.getString("storingbookingid", null); // getting String
                            Toast.makeText(getContext(), "ko"+ki, Toast.LENGTH_SHORT).show();

                            acceptmeupdate(ki);
                            updatestartedjobs(ki);
                            Intent intent = new Intent(getActivity(),AppreciationAccept.class);

                            String intentm="fromaccept";
                            intent.putExtra("check",intentm);

                            startActivity(intent);
                        }
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
    public void acceptmeupdate(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_updateaccept, new Response.Listener<String>() {
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

                params.put("booking_uid",s1);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void updatestartedjobs(final String s1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_startedjobs, new Response.Listener<String>() {
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

                params.put("booking_uid",s1);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Your Reason for Rejection");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        String case0="I am on other Project";
                        Toast.makeText(getActivity(), case0, Toast.LENGTH_LONG).show();
                        rejectmeupdate(getmyrejectid,case0);
                        Intent intent1 = new Intent(getActivity(),AppreciationAccept.class);

                        String intents1="fromreject";
                        intent1.putExtra("check",intents1);

                        startActivity(intent1);
                        break;
                    case 1:
                        String case1="I cant do the Service right now";
                        Toast.makeText(getActivity(), case1, Toast.LENGTH_LONG).show();
                        rejectmeupdate(getmyrejectid,case1);
                        Intent intent2 = new Intent(getActivity(),AppreciationAccept.class);
                        String intents2="fromreject";
                        intent2.putExtra("check",intents2);

                        startActivity(intent2);
                        break;
                    case 2:

                        Toast.makeText(getActivity(), "Third Item Clicked", Toast.LENGTH_LONG).show();
                        String case2="Its not my Requirement";
                        rejectmeupdate(getmyrejectid,case2);
                        Intent intent3 = new Intent(getActivity(),AppreciationAccept.class);

                        String intents3="fromreject";
                        intent3.putExtra("check",intents3);

                        startActivity(intent3);
                        break;
                    case 3:

                        Toast.makeText(getActivity(), "FOur Item Clicked", Toast.LENGTH_LONG).show();
                        String case3="I am out of Station";
                        rejectmeupdate(getmyrejectid,case3);
                        Intent intent4 = new Intent(getActivity(),AppreciationAccept.class);

                        String intents4="fromreject";
                        intent4.putExtra("check",intents4);

                        startActivity(intent4);
                        break;
                    case 4:

                        Toast.makeText(getActivity(), "Five Item Clicked", Toast.LENGTH_LONG).show();
                        String case4="My reason is not listed";
                        rejectmeupdate(getmyrejectid,case4);
                        Intent intent5 = new Intent(getActivity(),AppreciationAccept.class);

                        String intents5="fromreject";
                        intent5.putExtra("check",intents5);

                        startActivity(intent5);
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
    public void rejectmeupdate(final String s1,final String s2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.getPartner_updatereject, new Response.Listener<String>() {
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

                params.put("booking_uid",s1);
                params.put("service_partner_rejectedreason",s2);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
