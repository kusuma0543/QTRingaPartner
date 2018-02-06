package com.ringaapp.ringapartner.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.activities.ServiceTracking;
import com.ringaapp.ringapartner.db_javaclasses.home_accerejjobss;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class OnGoingPartJobs extends Fragment {
    TextView gettext;
    ImageView gifImageView;
    String ongoingjobspartuid;
    private ListView partnerhome_listview;
    private ProgressDialog dialog;
    private SessionManager session;
    private SQLiteHandler db;
    long days;
    private String take_user_number;
  //  FloatingActionButton homebut_buy;

    public static OnGoingPartJobs newInstance() {
        OnGoingPartJobs fragment= new OnGoingPartJobs();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view=inflater.inflate(R.layout.fragment_on_going_part_jobs, container, false);
        session = new SessionManager(getActivity());
        db = new SQLiteHandler(getActivity());

        final HashMap<String, String> user = db.getUserDetails();
        ongoingjobspartuid=user.get("uid");
        gettext=view.findViewById(R.id.text_list);
        gifImageView=view.findViewById(R.id.gif_list);

        dialog=new ProgressDialog(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
//        homebut_buy =view. findViewById(R.id.postad_partner);
//
//        homebut_buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.support.v4.app.Fragment selectedFragment = showprod.newInstance();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.contentContainer, selectedFragment);
//                transaction.commit();
//            }
//        });
        dialog.setMessage("Loading. Please wait...");
        partnerhome_listview=view.findViewById(R.id.partnerongoingjobs_listview);
        String URLL = GlobalUrl.partner_ongoingjobs+"?partner_uid="+ongoingjobspartuid;
        new kilomilo().execute(URLL);
        return view;
    }
    public class MovieAdap extends ArrayAdapter {
        private List<home_accerejjobss> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;

        MovieAdap(Context context, int resource, List<home_accerejjobss> objects) {
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
            final MovieAdap.ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                holder = new MovieAdap.ViewHolder();

                holder.textone = (TextView) convertView.findViewById(R.id.partnerhome_usernames);
                holder.textthree = (TextView)convertView.findViewById(R.id.partnerhome_usersubcategs);
                holder.textfour = (TextView)convertView.findViewById(R.id.partnerhome_usercateg);
                holder.text_gettingnumber = (TextView)convertView.findViewById(R.id.geting_number);
                holder.text_getingdate = (TextView)convertView.findViewById(R.id.gettingdate);
                holder.text_dispdate = (TextView)convertView.findViewById(R.id.partnerhome_whenaccepted);

                holder.ongoingbut_msg = convertView.findViewById(R.id.partneraccrej_chatbut);
                holder.ongoingbut_call = convertView.findViewById(R.id.partneraccrej_callbut);

                convertView.setTag(holder);
            }//ino
            else {
                holder = (MovieAdap.ViewHolder) convertView.getTag();
            }
            home_accerejjobss ccitacc = movieModelList.get(position);
            holder.textthree.setText(ccitacc.getService_subcateg_name());
            holder.textone.setText(ccitacc.getUser_name());
            holder.textfour.setText(ccitacc.getService_categ_name());
            holder.text_gettingnumber.setText(ccitacc.getUser_mobile_number());
            holder.text_getingdate.setText(ccitacc.getService_booking_createddate());

            String strThatDay = holder.text_getingdate.getText().toString();
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
            days = diff / (24 * 60 * 60 * 1000);
            holder.text_dispdate.setText("accepted before "+days+" days");


                holder.ongoingbut_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         take_user_number=holder.text_gettingnumber.getText().toString();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+take_user_number));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                });
            return convertView;
        }

        class ViewHolder {
            public TextView textone,textthree,textfour,text_dispdate,text_gettingnumber,text_getingdate;
            public Button ongoingbut_msg,ongoingbut_call;

        }
    }

    public class kilomilo extends AsyncTask<String, String, List<home_accerejjobss>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected List<home_accerejjobss> doInBackground(String... params) {
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
                JSONArray parentArray = parentObject.getJSONArray("ongoing");
                List<home_accerejjobss> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    home_accerejjobss catego = gson.fromJson(finalObject.toString(), home_accerejjobss.class);
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
        protected void onPostExecute(final List<home_accerejjobss> movieMode) {
            super.onPostExecute(movieMode);
            dialog.dismiss();

            if (movieMode== null)
            {
                partnerhome_listview.setVisibility(View.INVISIBLE);
                gifImageView.setVisibility(View.VISIBLE);


            }
            else
            {gifImageView.setVisibility(View.INVISIBLE);
                gettext.setVisibility(View.INVISIBLE);
                partnerhome_listview.setVisibility(View.VISIBLE);

                MovieAdap adapter = new MovieAdap(getActivity(), R.layout.ongoingjibs, movieMode);
                partnerhome_listview.setAdapter(adapter);
                partnerhome_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        home_accerejjobss item = movieMode.get(position);
                        Intent intent = new Intent(getActivity(),ServiceTracking.class);
                        intent.putExtra("partnerhome_bookingid",item.getBooking_uid());
                        intent.putExtra("partnerhome_subcategname",item.getService_subcateg_name());
                        intent.putExtra("partnerhome_username",item.getUser_name());
                        intent.putExtra("partnerhome_usermobile",item.getUser_mobile_number());
                        intent.putExtra("partnerhome_usermail",item.getUser_email());
                        intent.putExtra("partnerhome_address",item.getService_booking_address());
                        intent.putExtra("partnerhome_sendcateg",item.getService_categ_name());
                        intent.putExtra("partnerhome_sendaccpteddate",Long.toString(days));

                        String intentm="checkintentfromgoing";
                        intent.putExtra("check",intentm);

                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }
    }

}
