package com.ringaapp.ringapartner;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AbsRuntimePermission {
    private Button home_butsignin,home_butsignup;
    private static final int REQUEST_PERMISSION = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAppPermissions(new String[]{
                        Manifest.permission.READ_SMS,

                        Manifest.permission.ACCESS_COARSE_LOCATION,

                        Manifest.permission.READ_EXTERNAL_STORAGE,

                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.CALL_PHONE,
                },

                R.string.msg,REQUEST_PERMISSION);
        home_butsignin=(Button) findViewById(R.id.sbutsingin);
        home_butsignup=(Button) findViewById(R.id.sbutsignup);
        home_butsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        home_butsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
