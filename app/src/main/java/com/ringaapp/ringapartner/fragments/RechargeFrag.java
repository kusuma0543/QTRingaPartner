package com.ringaapp.ringapartner.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
//import com.ebs.android.sdk.Config;
//import com.ebs.android.sdk.EBSPayment;
//import com.ebs.android.sdk.PaymentRequest;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
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
   // FloatingActionButton homebut_buy;

    private static String HOST_NAME = "";
String values;

    ArrayList<HashMap<String, String>> custom_post_parameters;

    private static final int ACC_ID = 27375;// Provided by EBS
    private static final String SECRET_KEY = "5dc4a34eaca1ccd5ffe98385c517b0ca";// Provided by EBS
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

values=categorieslist.getJobs_value();
                        new SweetAlertDialog(getActivity())
                                .setTitleText("Confirm Recharge")
                                .setContentText("Your Recharge Amount is\n "+"\u20B9."+categorieslist.getJobs_value()+"\n"
                                        +"for "+categorieslist.getJobs_count()+" leads").showCancelButton(true)
                                .setConfirmText("Buy Leads").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                updatemypurchasedet(jobbuy_partner_uid,categorieslist.getJobs_count(),categorieslist.getJobs_value(),jobcounttool);
callEbsKit(getActivity());                         }
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
    private void callEbsKit(FragmentActivity rechargeFrag) {
//
//        PaymentRequest.getInstance().setTransactionAmount(
//                String.format("%.2f", values));
//
//
//        PaymentRequest.getInstance().setAccountId(ACC_ID);
//        PaymentRequest.getInstance().setSecureKey(SECRET_KEY);
//
//        PaymentRequest.getInstance().setReferenceNo("223");
//
//        PaymentRequest.getInstance().setBillingEmail("test@testmail.com");
//
//        PaymentRequest.getInstance().setFailureid("1");
//
//        PaymentRequest.getInstance().setCurrency("INR");
//
//        PaymentRequest.getInstance().setTransactionDescription(
//                "Test Transaction");
//
//        PaymentRequest.getInstance().setBillingName("Test_Name");
//        PaymentRequest.getInstance().setBillingAddress("North Mada Street");
//        PaymentRequest.getInstance().setBillingCity("Chennai");
//        PaymentRequest.getInstance().setBillingPostalCode("600019");
//        PaymentRequest.getInstance().setBillingState("Tamilnadu");
//        PaymentRequest.getInstance().setBillingCountry("IND");
//        PaymentRequest.getInstance().setBillingPhone("01234567890");
//        PaymentRequest.getInstance().setShippingName("Test_Name");
//        PaymentRequest.getInstance().setShippingAddress("North Mada Street");
//        PaymentRequest.getInstance().setShippingCity("Chennai");
//        PaymentRequest.getInstance().setShippingPostalCode("600019");
//        PaymentRequest.getInstance().setShippingState("Tamilnadu");
//        PaymentRequest.getInstance().setShippingCountry("IND");
//        PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
//        PaymentRequest.getInstance().setShippingPhone("01234567890");
//        PaymentRequest.getInstance().setLogEnabled("1");
//
//        PaymentRequest.getInstance().setHidePaymentOption(true);
//
//        PaymentRequest.getInstance().setHideCashCardOption(true);
//
//        PaymentRequest.getInstance().setHideCreditCardOption(true);
//
//        PaymentRequest.getInstance().setHideDebitCardOption(true);
//
//        PaymentRequest.getInstance().setHideNetBankingOption(true);
//
//        PaymentRequest.getInstance().setHideStoredCardOption(true);
//
//        custom_post_parameters = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> hashpostvalues = new HashMap<String, String>();
//        hashpostvalues.put("account_details", "saving");
//        hashpostvalues.put("merchant_type", "gold");
//        custom_post_parameters.add(hashpostvalues);
//
//        PaymentRequest.getInstance()
//                .setCustomPostValues(custom_post_parameters);
//
//        EBSPayment.getInstance().init(rechargeFrag, ACC_ID, SECRET_KEY,
//                Config.Mode.ENV_TEST, Config.Encryption.ALGORITHM_MD5, HOST_NAME);

    }
}
