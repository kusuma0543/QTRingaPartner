package com.ringaapp.ringapartner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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

public class DispSellProdDet extends AppCompatActivity {

    private TextView tv_prod_title,tv_prod_desc,tv_prod_cost,sell_dis_add_images_tv;
    private String s_prod_title,s_prod_desc,s_prod_cost,s_dispprod_partner_uid;
    private Button but_publish_prod;
    private ImageView sell_dis_add_images;

    public static final String UPLOAD_KEY = "proof_images";
    public static final String UPLOAD_KEYTWO="partner_uid";
    private int PICK_IMAGE_REQUEST = 11;
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_sell_prod_det);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();
        s_dispprod_partner_uid = user.get("uid");
        Intent intent = getIntent();
        s_prod_title = intent.getStringExtra("send_prod_title");
        s_prod_desc = intent.getStringExtra("send_prod_desc");
        s_prod_cost = intent.getStringExtra("send_prod_cost");

        tv_prod_title = findViewById(R.id.product_title);
        tv_prod_desc = findViewById(R.id.product_desc);
        tv_prod_cost = findViewById(R.id.product_cost);
        but_publish_prod = findViewById(R.id.publish_prod_but);
        sell_dis_add_images = findViewById(R.id.sell_dis_add_images);
        sell_dis_add_images_tv = findViewById(R.id.sell_dis_add_images_tv);

        tv_prod_title.setText(s_prod_title);
        tv_prod_desc.setText(s_prod_desc);
        tv_prod_cost.setText(s_prod_cost);
        but_publish_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sell_dis_add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        sell_dis_add_images_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
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
                    new JSONTask().execute(GlobalUrl.partner_retproofimages+"?"+UPLOAD_KEYTWO+"="+uidimagex);

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
                loading = ProgressDialog.show(DispSellProdDet.this, "Uploading Image", "Please wait...",true,true);

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
                data.put(UPLOAD_KEYTWO,s_dispprod_partner_uid);
                data.put(UPLOAD_KEY, uploadImage);


                String result = rh.sendPostRequest(GlobalUrl.partner_uploadproofimages,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    public class JSONTask extends AsyncTask<String,String, List<ProofImageRet>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected List<ProofImageRet> doInBackground(String... params) {
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
                List<ProofImageRet> movieModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    ProofImageRet categorieslist = gson.fromJson(finalObject.toString(),ProofImageRet.class);
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
        protected void onPostExecute(final List<ProofImageRet> movieModelList) {
            super.onPostExecute(movieModelList);
            if(movieModelList != null) {
              MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.imageret, movieModelList);
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
        private List<ProofImageRet> movieModelList;
        private int resource;
        Context context;
        private LayoutInflater inflater;
        MovieAdapter(Context context, int resource, List<ProofImageRet> objects) {
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
                holder.get_rowcount = convertView.findViewById(R.id.getcount);


                convertView.setTag(holder);
            }
            else {
                holder = (MovieAdapter.ViewHolder) convertView.getTag();
            }
            ProofImageRet categorieslist= movieModelList.get(position);
            Picasso.with(context).load(categorieslist.getProof_images()).fit().error(R.drawable.pinns).fit().into(holder.menuimage);
            holder.get_rowcount.setText(categorieslist.getRow_count());
            String s_rowacount=holder.get_rowcount.getText().toString();
            if(s_rowacount.matches("4")||s_rowacount.equals("4"))
            {
                docv_imagesel.setVisibility(View.INVISIBLE);
                docv_itemsel.setVisibility(View.INVISIBLE);
                front.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "You Can able to select onl two Images", Toast.LENGTH_SHORT).show();

            }
            else if(s_rowacount.matches("1")||s_rowacount.equals("1"))
            {
                front.setText("(front and back images - "+ s_rowacount+")");
            }
            else if(s_rowacount.matches("2")||s_rowacount.equals("2"))
            {
                front.setText("(front and back images - "+ s_rowacount+")");
            }else if(s_rowacount.matches("3")||s_rowacount.equals("3"))
            {
                front.setText("(front and back images - "+ s_rowacount+")");
            }
            return convertView;
        }
        class ViewHolder{
            private ImageView menuimage;
            private TextView get_rowcount;

        }
    }


}


