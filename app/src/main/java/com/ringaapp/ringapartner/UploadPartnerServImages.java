package com.ringaapp.ringapartner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadPartnerServImages extends AppCompatActivity implements View.OnClickListener{
    private TextView docv_imagesel;
    private ImageView docv_itemsel;

    public static final String UPLOAD_KEY = "partner_images";
    public static final String UPLOAD_KEYTWO="partner_uid";
    private int PICK_IMAGE_REQUEST = 11;
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    private String uidimagex;
    private GridView linearLayout;

    private ProgressDialog dialog;
    private Button butallupload;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_partner_serv_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarz);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        if (isConnectedToNetwork()) {
            dialog = new ProgressDialog(this);
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
           // Intent intent = getIntent();
           /// uidimagex = intent.getStringExtra("uidimagex");
            //  x="5a2799e95c05f9.57886214";

        final HashMap<String, String> user = db.getUserDetails();
        uidimagex=user.get("uid");

            Toast.makeText(this, uidimagex, Toast.LENGTH_SHORT).show();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            linearLayout = findViewById(R.id.listview);


            // listdoc=findViewById(R.id.listviewk);
            docv_imagesel = findViewById(R.id.docv_imagesel);
            docv_itemsel = findViewById(R.id.docv_itemsel);

            butallupload = findViewById(R.id.alluploadbut);

            docv_imagesel.setOnClickListener(this);
            docv_itemsel.setOnClickListener(this);
            butallupload.setOnClickListener(this);

        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(UploadPartnerServImages.this,UploadPartnerServImages.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(PICK_IMAGE_REQUEST==11)
        {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    uploadImage();
                    new JSONTask().execute(GlobalUrl.partner_imageret+"?"+UPLOAD_KEYTWO+"="+uidimagex);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UploadPartnerServImages.this, "Uploading Image", "Please wait...",true,true);

            }

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEYTWO,uidimagex);
                data.put(UPLOAD_KEY, uploadImage);


                String result = rh.sendPostRequest(GlobalUrl.partner_imageupload,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == docv_imagesel) {
            showFileChooser();
        }
        if(v==docv_itemsel)
        {
            showFileChooser();
        }

        if(v==butallupload)



        {
            startActivity(new Intent(UploadPartnerServImages.this,DocVerification.class));


            }

        }




    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    public class JSONTask extends AsyncTask<String,String, List<Imageret>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

        }
        @Override
        protected List<Imageret> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<Imageret> movieModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Imageret categorieslist = gson.fromJson(finalObject.toString(),Imageret.class);
                    movieModelList.add(categorieslist);


                }
                return movieModelList;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }
        @Override
        protected void onPostExecute(final List<Imageret> movieModelList) {
            super.onPostExecute(movieModelList);
            dialog.dismiss();
            if(movieModelList != null) {
              MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.profimageret, movieModelList);
                LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getApplicationContext());
                MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                linearLayout.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class MovieAdapter extends ArrayAdapter {
        private List<Imageret> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        MovieAdapter(Context context, int resource, List<Imageret> objects) {
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
            final MovieAdapter.ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new MovieAdapter.ViewHolder();
                holder.menuimage = (ImageView)convertView.findViewById(R.id.docv_imagedissi);


                convertView.setTag(holder);
            }
            else {
                holder = (MovieAdapter.ViewHolder) convertView.getTag();
            }
            Imageret categorieslist= movieModelList.get(position);
            Picasso.with(context).load(categorieslist.getPartner_images()).fit().error(R.drawable.pinns).fit().into(holder.menuimage);


            return convertView;
        }
        class ViewHolder{
            private ImageView menuimage;

        }
    }
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



}
