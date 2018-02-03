package com.ringaapp.ringapartner.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.ringaapp.ringapartner.GlobalUrl.GlobalUrl;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.db_javaclasses.ProofImageRet;
import com.ringaapp.ringapartner.dbhandlers.SQLiteHandler;
import com.ringaapp.ringapartner.dbhandlers.SessionManager;
import com.ringaapp.ringapartner.javaclasses.AppController;
import com.ringaapp.ringapartner.javaclasses.RequestHandler;
import com.ringaapp.ringapartner.javaclasses.Utility;
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
import java.util.Map;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{
private TextView docv_imagesel;
private ImageView docv_itemsel;
    String hsleradio;

    private RadioGroup shome_groupone;
    private RadioButton shome_oneradio,shom_tworadio;
    private String userChoosenTask;


    public static final String UPLOAD_KEY = "proof_images";
    public static final String UPLOAD_KEYTWO="partner_uid";
    private int PICK_IMAGE_REQUEST = 11;
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    private String uidimagex;
    private GridView linearLayout;
    EditText mEdit;
    String sleradio;
    private ProgressDialog dialog;
    private Button butallupload;
    private SessionManager session;
    private SQLiteHandler db;
    TextView front;
    private int REQUEST_CAMERA = 1, SELECT_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Documents Upload");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        if (isConnectedToNetwork()) {
            dialog = new ProgressDialog(this);
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. Please wait...");
            Intent intent = getIntent();
            uidimagex = intent.getStringExtra("uidimagex");
            //  x="5a2799e95c05f9.57886214";

//        final HashMap<String, String> user = db.getUserDetails();
//        uidimagex=user.get("uid");

            //Toast.makeText(this, uidimagex, Toast.LENGTH_SHORT).show();
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
            mEdit = findViewById(R.id.getcharge);

            shome_groupone = (RadioGroup) findViewById(R.id.shome_radioone);


            front=findViewById(R.id.front);

            docv_imagesel = findViewById(R.id.docv_imagesel);
            docv_itemsel = findViewById(R.id.docv_itemsel);

            butallupload = findViewById(R.id.alluploadbut);
            shom_tworadio = findViewById(R.id.sradio_two);
            shome_oneradio = findViewById(R.id.sradio_one);

            docv_imagesel.setOnClickListener(this);
            docv_itemsel.setOnClickListener(this);
            butallupload.setOnClickListener(this);
//        shome_oneradio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sleradio = "Free";
//                butallupload.setVisibility(View.VISIBLE);
//
//            }
//        });
//        shom_tworadio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sleradio = mEdit.getText().toString();
//            butallupload.setVisibility(View.VISIBLE);
//            }
//        });
//        mEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEdit.setSelection(0);
//                butallupload.setVisibility(View.VISIBLE);
//
//            }
//        });
        }
        else
        {
            setContentView(R.layout.content_ifnointernet);
            DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder_fornointernet);
            desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HomeScreen.this,HomeScreen.class);
                    startActivity(intent);
                }
            });
        }

    }
    private void selectImage() {
        final CharSequence[] items = {  "Choose from Gallery","Take Photo",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(HomeScreen.this);

                if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        showFileChooser();
                }  else if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
//             else if(PICK_PDF_REQUEST==1)
//             {
//                  if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                         filePath = data.getData();
//                         uploadMultipart();
//                      Toast.makeText(this, "Document Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                      new JSONTasks().execute(GlobalUrl.partner_docret+"?"+UPLOAD_KEYTWO+"="+uidimage);
//
//                  }
//
//             }
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
                loading = ProgressDialog.show(HomeScreen.this, "Uploading Image", "Please wait...",true,true);
                mEdit.setVisibility(View.VISIBLE);

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


                String result = rh.sendPostRequest(GlobalUrl.partner_uploadproofimages,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == docv_imagesel) {
            selectImage();
        }
        if(v==docv_itemsel)
        {
            selectImage();
        }
//        if (v == docv_docsel) {
//            showFileChoosers();
//        }
        if(v==butallupload)
//        { int selectedId = shome_groupone.getCheckedRadioButtonId();
//            shome_oneradio =  findViewById(selectedId);
//            if (shome_oneradio.getText().toString().equals("Free")) {
//                sleradio = "Free";
//            }
//            if (shome_oneradio.getText().toString().equals("Type Your amount")) {
//                sleradio = mEdit.getText().toString();
//            }


        {
            int selectedId = shome_groupone.getCheckedRadioButtonId();

            shome_oneradio =  findViewById(selectedId);

            sleradio=shome_oneradio.getText().toString();

                if(sleradio.equals("Free"))
                {
                    hsleradio="Free";
                    callmetoupload();

                }
                else if(sleradio.equals("Custom Charge"))
                {

                    hsleradio=mEdit.getText().toString();

                        callmetoupload();


                }
                else if(sleradio.equals(""))
                {

                    Toast.makeText(this, "please enter value between 1-1000", Toast.LENGTH_SHORT).show();


                }










//
       }

    }
    public void callmetoupload()
    {
        uploadbf(uidimagex,hsleradio);
       // Toast.makeText(this, sleradio, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeScreen.this,UploadPartnerServImages.class));

    }
//    private void showFileChoosers() {
//        Intent intent = new Intent();
//        intent.setType("application/pdf");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
//    }
//
//
//
//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this,     android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//        }
//
//        ActivityCompat.requestPermissions(this, new String[]{    android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//    public void uploadMultipart() {
//       // String name = editText.getText().toString().trim();
//        String path = FilePath.getPath(this, filePath);
//
//        if (path == null) {
//
//            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
//        } else {
//
//            try {
//                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(this, uploadId, GlobalUrl.partner_docv_upload)
//                        .addFileToUpload(path, "file") //Adding file
//                        .addParameter("name", uidimage) //Adding text parameter to the request
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//            } catch (Exception exc) {
//                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    public class JSONTask extends AsyncTask<String,String, List<ProofImageRet>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

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
            dialog.dismiss();
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
            final ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new ViewHolder();
                holder.menuimage = (ImageView)convertView.findViewById(R.id.docv_imagedissi);
                holder.get_rowcount = convertView.findViewById(R.id.getcount);


                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            ProofImageRet categorieslist= movieModelList.get(position);
            Picasso.with(context).load(categorieslist.getProof_images()).fit().error(R.drawable.pinns).fit().into(holder.menuimage);
            holder.get_rowcount.setText(categorieslist.getRow_count());
                String s_rowacount=holder.get_rowcount.getText().toString();
                if(s_rowacount.matches("2")||s_rowacount.equals("2"))
                {
                    docv_imagesel.setVisibility(View.INVISIBLE);
                    docv_itemsel.setVisibility(View.INVISIBLE);
                    front.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "You Can able to select only two Images", Toast.LENGTH_SHORT).show();

                }else if(s_rowacount.matches("1")||s_rowacount.equals("1"))
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





//    public class JSONTasks extends AsyncTask<String,String, List<docret>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog.show();
//
//        }
//        @Override
//        protected List<docret> doInBackground(String... params) {
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//            try {
//                URL url = new URL(params[0]);
//
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                InputStream stream = connection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuilder buffer = new StringBuilder();
//                String line ="";
//                while ((line = reader.readLine()) != null){
//                    buffer.append(line);
//                }
//                String finalJson = buffer.toString();
//                JSONObject parentObject = new JSONObject(finalJson);
//                JSONArray parentArray = parentObject.getJSONArray("result");
//                List<docret> movieModelList = new ArrayList<>();
//                Gson gson = new Gson();
//                for(int i=0; i<parentArray.length(); i++) {
//                    JSONObject finalObject = parentArray.getJSONObject(i);
//
//                    docret categorieslist = gson.fromJson(finalObject.toString(),docret.class);
//                    movieModelList.add(categorieslist);
//
//
//                }
//                return movieModelList;
//            } catch (JSONException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                if(connection != null) {
//                    connection.disconnect();
//                }
//                try {
//                    if(reader != null) {
//                        reader.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return  null;
//        }
//        @Override
//        protected void onPostExecute(final List<docret> movieModelList) {
//            super.onPostExecute(movieModelList);
//            dialog.dismiss();
//            if(movieModelList != null) {
//                MovieAdapters adapter = new MovieAdapters(getApplicationContext(), R.layout.docret, movieModelList);
//                listdoc.setVisibility(View.VISIBLE);
//                listdoc.setAdapter(adapter);
//                butallupload.setVisibility(View.VISIBLE);
//                adapter.notifyDataSetChanged();
//                listdoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        docret item = movieModelList.get(position);
//                        String gg=item.getPartner_documents();
//
//                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ gg);
//                        Intent target = new Intent(Intent.ACTION_VIEW);
//                        target.setDataAndType(Uri.fromFile(file),"application/pdf");
//                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                        Intent intent = Intent.createChooser(target, "Open File");
//                        try {
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            // Instruct the user to install a PDF reader here, or something
//                        }
//                    }
//                });
//            }
//            else {
//                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public class MovieAdapters extends ArrayAdapter {
//        private List<docret> movieModelList;
//        private int resource;
//        Context context;
//        private LayoutInflater inflater;
//        MovieAdapters(Context context, int resource, List<docret> objects) {
//            super(context, resource, objects);
//            movieModelList = objects;
//            this.context =context;
//            this.resource = resource;
//            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        }
//        @Override
//        public int getViewTypeCount() {
//            return 1;
//        }
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final ViewHolder holder  ;
//            if(convertView == null){
//                convertView = inflater.inflate(resource,null);
//                holder = new ViewHolder();
//                holder.menuimage = convertView.findViewById(R.id.texttopic);
//
//
//
//                convertView.setTag(holder);
//            }
//            else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            docret categorieslist= movieModelList.get(position);
//            //Picasso.with(context).load(categorieslist.getPartner_documentname()).fit().error(R.drawable.texttopic).fit().into(holder.menuimage);
//            holder.menuimage.setText(categorieslist.getPartner_documentname());
//            return convertView;
//        }
//        class ViewHolder{
//            private TextView menuimage;
//
//        }
//    }

    public void uploadbf(final String sdpartneruid, final String sdpartnerbudeget) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalUrl.partner_allbudfeadet, new Response.Listener<String>() {
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
                params.put("partner_uid", sdpartneruid);
                params.put("partner_budget", sdpartnerbudeget);
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


