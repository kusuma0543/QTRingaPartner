package com.ringaapp.ringapartner.dbhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kusuma on 1/6/2018.
 */

public class SQLiteHandler extends SQLiteOpenHelper {


    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "partner_table";

    // Login table name
    private static final String TABLE_USER = "partner";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_PNUM = "mobilenumer";
    private static final String KEY_ADDRESS = "user_address";
    private static final String KEY_CITY = "user_city";
    private static final String KEY_LOCALITY = "user_locality";

    private static final String KEY_LATITUDE = "user_latitude";
    private static final String KEY_LONGITUDE = "user_longitude";
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                +  KEY_PNUM + " TEXT ," +KEY_ADDRESS + " TEXT ," +KEY_CITY +
                " TEXT ," +KEY_LOCALITY + " TEXT ," +KEY_LATITUDE + " TEXT ,"
                +KEY_LONGITUDE + " TEXT " + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String mobilenumer,String user_address,String user_city,String user_locality,String user_latitude,String user_longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_PNUM,mobilenumer);
        values.put(KEY_ADDRESS, user_address);
        values.put(KEY_CITY,user_city);
        values.put(KEY_LOCALITY,user_locality);
        values.put(KEY_LATITUDE, user_latitude);
        values.put(KEY_LONGITUDE,user_longitude);
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New user inserted into sqlite: " + id);

    }
    public void updateuser(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email// Email

        long id =db.update(TABLE_USER,values,"id=1", null);
        db.close(); // Closing database connection
        Log.d(TAG, "New user updated into sqlite: " + id);

    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("mobilenumer", cursor.getString(4));
            user.put("user_address", cursor.getString(5));
            user.put("user_city", cursor.getString(6));
            user.put("user_locality", cursor.getString(7));
            user.put("user_latitude", cursor.getString(8));
            user.put("user_longitude", cursor.getString(9));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}