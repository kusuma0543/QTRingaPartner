package com.ringaapp.ringapartner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.javaclasses.TrackGps;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;
import com.ringaapp.ringapartner.javaclasses.AppUtils;
import com.ringaapp.ringapartner.javaclasses.FetchAddressIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PartnerMapSel extends FragmentActivity implements LocationListener, OnMapReadyCallback {
    private Button mylocationnav;

    private GoogleMap mMap;
    private TrackGps gps;

    ListView list29;
    private Button picka;
    int PLACE_PICKER_REQUEST = 1;
    public LatLng mCenterLatLong;
    private Button ridenow;
    String address;
    String cityName;
    String stateName;
    String postlCode;
    String adminarea;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    private AddressResultReceiver mResultReceiver;
     String uidthree,partmapemail,partmapmobile,alllatittude,alllongitude,alld;
    List<Address> addressess;
    private ProgressDialog dialog;

    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_map_sel);
        Intent intk = getIntent();
        uidthree = intk.getStringExtra("oneuid");
        partmapemail = intk.getStringExtra("usrmapemail");
        partmapmobile = intk.getStringExtra("mobile_number");

        if (isConnectedToNetwork()) {
           // Toast.makeText(getApplicationContext(), uidthree, Toast.LENGTH_SHORT).show();
            dialog = new ProgressDialog(PartnerMapSel.this);
            dialog = new ProgressDialog(PartnerMapSel.this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
            dialog.show();
            list29 = (ListView) findViewById(R.id.listview);
            ridenow = (Button) findViewById(R.id.ride);
            mylocationnav = findViewById(R.id.mylocnav);
            session = new SessionManager(getApplicationContext());
            db = new SQLiteHandler(getApplicationContext());

            mylocationnav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMapReady(mMap);
                }
            });
            ridenow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (address == null) {
                        Toast.makeText(getApplicationContext(), "Please select one location to move further", Toast.LENGTH_SHORT).show();
                    }
                    Geocoder geocoder = new Geocoder(PartnerMapSel.this, Locale.getDefault());
                    try {
                        addressess = geocoder.getFromLocation(mCenterLatLong.latitude, mCenterLatLong.longitude, 1);
                        address = addressess.get(0).getSubLocality();
                        cityName = addressess.get(0).getLocality();
                        stateName = addressess.get(0).getAdminArea();
                        postlCode = addressess.get(0).getPostalCode();
                        adminarea = addressess.get(0).getAddressLine(0);
                        ;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    alllatittude = Double.toString(mCenterLatLong.latitude);
                    alllongitude = Double.toString(mCenterLatLong.longitude);
                    alld = adminarea + " " + address + " " + cityName + " " + postlCode;


                    rideme(uidthree, alld, cityName, address, postlCode, alllatittude, alllongitude);


                }
            });
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync((OnMapReadyCallback) PartnerMapSel.this);
            picka = (Button) findViewById(R.id.pick);
            picka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent inkl;
                    try {
                        inkl = builder.build(PartnerMapSel.this);
                        startActivityForResult(inkl, PLACE_PICKER_REQUEST);

                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();

                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    }


                }
            });


            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("IN")
                    .build();

            autocompleteFragment.setFilter(typeFilter);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    // Toast.makeText(getApplicationContext(), "Place: " + place.getName(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getApplicationContext(), "Place: " + place.getLatLng(), Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    LatLng sydneys = place.getLatLng();

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydneys, 6.5f));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
                    mMap.setMaxZoomPreference(15.5f);
                    mMap.setMinZoomPreference(6.5f);

                }

                @Override
                public void onError(Status status) {

                    Toast.makeText(getApplicationContext(), "An error occurred: " + status, Toast.LENGTH_SHORT).show();

                }
            });

        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PartnerMapSel.this,PartnerMapSel.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gps = new TrackGps(PartnerMapSel.this);
        mMap = googleMap;
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


                mMap.clear();
               // getmygps();
                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);
dialog.cancel();
                    startIntentService(mLocation);
                    Geocoder geocoder = new Geocoder(PartnerMapSel.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(mCenterLatLong.latitude, mCenterLatLong.longitude, 1);
                        address = addresses.get(0).getSubLocality();
                        cityName = addresses.get(0).getLocality();
                        stateName = addresses.get(0).getAdminArea();
                      //  Toast.makeText(getApplicationContext(), "Your selected area : " + address, Toast.LENGTH_SHORT).show();

                       // Toast.makeText(getApplicationContext(), "Your selected city: " + cityName, Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getmygpsd();


    }


    protected void startIntentService(Location mLocation) {

        //getmygps();

        Intent intent = new Intent(this, FetchAddressIntentService.class);

        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);
            getmygps();
            displayAddressOutput();

            getmygps();

            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {


            }


        }

    }
    protected void displayAddressOutput() {
        try {
            if (mAreaOutput != null)


                Toast.makeText(getApplicationContext(),"mAddressOutput",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void getmygps()
    {
        gps = new TrackGps(PartnerMapSel.this);

        Double lat = gps.getLatitude();
        Double lng = gps.getLongitude();
        LatLng sydney = new LatLng(lat, lng);
        float zoomLevel =10;

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pinns
        ))
                .position(new LatLng(lat,lng)));


dialog.cancel();

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat,lng))
                .radius(1000)
                .fillColor(Color.argb(20, 255, 0, 255))
                .strokeColor(Color.BLUE)
                .strokeWidth(2.0f));

    }
    public void getmygpsd()
    {
        gps = new TrackGps(PartnerMapSel.this);

        Double lat = gps.getLatitude();
        Double lng = gps.getLongitude();
        LatLng sydney = new LatLng(lat, lng);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,6.5f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
        mMap.setMaxZoomPreference(15.5f);
        mMap.setMinZoomPreference(6.5f);



    }

    public void rideme(final String uidthree, final String s1, final String s2, final String s3, final String s4, final String s5, final String s6) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_placepick, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("exits");

                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users_detail");

                        String uname1 = users.getString("partner_name");
                        String checkdoc=users.getString("partner_passess");
                        if(checkdoc.equals("0"))
                        {
                            session.setLogin(true);

                            db.addUser(uname1, partmapemail, uidthree, partmapmobile,alld,cityName,address,alllatittude,alllongitude);

                            Intent intentmm=new Intent(PartnerMapSel.this,PartnerSerSel.class);
                            intentmm.putExtra("oneuid",uidthree);
                            intentmm.putExtra("user_city",s2);
                            intentmm.putExtra("user_area",s3);
                            intentmm.putExtra("user_unamehome",uname1);
                            // intentmm.putExtra("updtaedimage",uname2);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PartnerMapSel.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("useruidentire",uidthree);

                            editor.putString("user_city", cityName);




                            editor.apply();
                            startActivity(intentmm);

                        }
                        else {
                            session.setLogin(true);

                            db.addUser(uname1, partmapemail, uidthree, partmapmobile,alld,cityName,address,alllatittude,alllongitude);

                            startActivity(new Intent(PartnerMapSel.this,CategoryMain.class));

                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter correct otp",Toast.LENGTH_SHORT).show();

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
                params.put("partner_uid",uidthree);
                params.put("partner_address",s1);
                params.put("partner_cityname",s2);
                params.put("partner_locality",s3);

                params.put("partner_pincode", s4);

                params.put("partner_latitude", s5);
                params.put("partner_longitude",s6);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
