package com.example.hp.phase1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hp.phase1.UserObj;

public class DB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "RegistrationDB";
    private static final String[] COLUMNS_pswd = {"password"};
    private static final String[] COLUMNS_mobiles = {"mobilenum1", "mobilenum2", "mobilenum3", "mobilenum4"};
    private static final String[] COLUMNS_survey = {"val1", "val2", "val3", "val4","val5"};
    String CREATE_TABLE_USER = "CREATE TABLE if not exists userInfo (mobilenum TEXT, password TEXT, firstname TEXT, lastname TEXT, emailid TEXT, address1 TEXT,address2 TEXT, city TEXT, state TEXT, country TEXT)";
    String CREATE_TABLE_MOBILE = "CREATE TABLE if not exists frnsMobile (usermobile TEXT,mobilenum1 TEXT, mobilenum2 TEXT, mobilenum3 TEXT, mobilenum4 TEXT)";
    String CREATE_TABLE_SOCNETWRK = "CREATE TABLE if not exists twitterFBMobile (usermobile TEXT,twitterid TEXT,fbid TEXT)";
    String CREATE_TABLE_SURVEY = "CREATE TABLE if not exists crimeSurvey (mobilenum TEXT,val1 INTEGER,val2 INTEGER,val3 INTEGER,val4 INTEGER,val5 INTEGER)";


    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create  table


        // create table
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MOBILE);
        db.execSQL(CREATE_TABLE_SOCNETWRK);
        db.execSQL(CREATE_TABLE_SURVEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed

        // create fresh books table
        this.onCreate(db);
    }

    public void deleteTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS userInfo");
        db.execSQL("DROP TABLE IF EXISTS frnsMobile");
        db.execSQL("DROP TABLE IF EXISTS twitterFBMobile");
        db.execSQL("DROP TABLE IF EXISTS crimeSurvey");


    }


    public int addUser(UserObj user) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        deleteTables();
        this.onCreate(db);
        // 2. create ContentValues to add key "column"/value

        ContentValues values = new ContentValues();
        values.put("mobilenum", user.mobileNum); // get title
        values.put("password", user.password); // get author
        values.put("firstname", user.firstName);
        values.put("lastname", user.lastName);
        values.put("emailid", user.email);
        values.put("address1", user.address1);
        values.put("address2", user.address2);
        values.put("city", user.city);
        values.put("state", user.state);
        values.put("country", user.country);


        Cursor cursor =
                db.query("userInfo", // a. table
                        COLUMNS_pswd, // b. column names
                        " mobilenum = ?", // c. selections
                        new String[]{String.valueOf(user.mobileNum)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit


        if (cursor.getCount() == 0) {

            // 3. insert
            try {
                db.insert("userInfo", // table
                        null, //nullColumnHack
                        values); // key/value -> keys = column names/ values = column values

                // 4. close
                db.close();
                return 1;
            } catch (Exception e) {
                return 111;
            }
        } else {
            //  deleteTables();
            return 111;
        }
    }

    public void addPhnum(mobilenumbersObj mobilenumbersObj, String mobilenum) {
        //for logging

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("DROP TABLE IF EXISTS frnsMobile");
        // 2. create ContentValues to add key "column"/value
//this.onCreate(db);
        ContentValues values = new ContentValues();
        values.put("usermobile", mobilenum);
        values.put("mobilenum1", mobilenumbersObj.mobilenum1);
        values.put("mobilenum2", mobilenumbersObj.mobilenum2);
        values.put("mobilenum3", mobilenumbersObj.mobilenum3);
        values.put("mobilenum4", mobilenumbersObj.mobilenum4);
        try {
            this.onCreate(db);
            // 3. insert
            db.insert("frnsMobile", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

            // 4. close
            db.close();
        } catch (Exception e) {
            this.onCreate(db);
        }
    }

    public void addCrimeSurvey(String mobilenum, int val1, int val2, int val3, int val4, int val5) {
        //for logging

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("DROP TABLE IF EXISTS frnsMobile");
        // 2. create ContentValues to add key "column"/value

        ContentValues values = new ContentValues();
        values.put("mobilenum", mobilenum);
        values.put("val1", val1);
        values.put("val2", val2);
        values.put("val3", val3);
        values.put("val4", val4);
        values.put("val5", val5);

        try {
            this.onCreate(db);
            // 3. insert
            db.insert("crimeSurvey", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

            // 4. close
            db.close();
        } catch (Exception e) {
            this.onCreate(db);
        }
    }

    public void addSocNetwrk(String mobilenum, String tweetid, String fbid) {
        //for logging

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("DROP TABLE IF EXISTS frnsMobile");
        // 2. create ContentValues to add key "column"/value
//this.onCreate(db);
        ContentValues values = new ContentValues();
        values.put("usermobile", mobilenum);
        values.put("twitterid", tweetid);
        values.put("fbid", fbid);

        try {
            this.onCreate(db);
            // 3. insert
            db.insert("twitterFBMobile", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

            // 4. close
            db.close();
        } catch (Exception e) {
            this.onCreate(db);
        }
    }

    public String checkUser(String mob) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("userInfo", // a. table
                        COLUMNS_pswd, // b. column names
                        " mobilenum = ?", // c. selections
                        new String[]{String.valueOf(mob)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        try {
            return ((cursor.getString(0)));
        } catch (Exception e) {
            return "Number doesn't exist";
        }

    }


    public void updatePassword(String password,String mobilenumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
       // 3. updating row
        int i = db.update("userInfo", //table
                values, // column/value
              "mobilenum = ?", // selections
                new String[] { String.valueOf(mobilenumber) }); //selection args

        // 4. close
        db.close();
    }




    public mobilenumbersObj getMobilenum(String mob) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("frnsMobile", // a. table
                        COLUMNS_mobiles, // b. column names
                        " usermobile = ?", // c. selections
                        new String[]{String.valueOf(mob)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        mobilenumbersObj mobilenumbersObj = new mobilenumbersObj(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return (mobilenumbersObj);
    }

    public CrimeSurveyObj getCrimeObj(String mob) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("crimeSurvey", // a. table
                        COLUMNS_survey, // b. column names
                        "mobilenum = ?", // c. selections
                        new String[]{String.valueOf(mob)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        try {
            if (cursor != null)
                cursor.moveToFirst();
            CrimeSurveyObj crimeSurveyObj = new CrimeSurveyObj(Integer.valueOf(cursor.getString(0)), Integer.valueOf(cursor.getString(1)), Integer.valueOf(cursor.getString(2)), Integer.valueOf(cursor.getString(3)), Integer.valueOf(cursor.getString(4)));
            return crimeSurveyObj;
        }
        catch (Exception e) {
        return null;

        }
    }

}