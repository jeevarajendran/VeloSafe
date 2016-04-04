package com.techgenie.velosafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Class Name : RegionContract
 * Purpose : This class describes the inserting data into sqlite.Here three tables are creating,
 * Regionbin,userdetails,bikedetails
 *
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "VeloSafe.db";
    public static final int DATABASE_VERSION = 7;
    public static final String CREATE_REGIONBIN_QUERY = "create table "+
            RegionContract.RegionBinInfo.TABLE_NAME+"(id integer primary key, "+
            RegionContract.RegionBinInfo.REGION_NAME+" TEXT,"+
            RegionContract.RegionBinInfo.REGION_CORD_X+" DOUBLE,"+
            RegionContract.RegionBinInfo.REGION_CORD_Y+" DOUBLE,"+
            RegionContract.RegionBinInfo.REGION_WEIGHT+" INTEGER,"+
            RegionContract.RegionBinInfo.REGION_ISSAFE+" TEXT);";
    public static final String CREATE_USERDETAILS_QUERY = "create table "+
            UserDetailsContract.UserDetailsInfo.TABLE_NAME+"(id integer primary key, "+
            UserDetailsContract.UserDetailsInfo.USER_FNAME+" TEXT,"+
            UserDetailsContract.UserDetailsInfo.USER_LNAME+" TEXT,"+
            UserDetailsContract.UserDetailsInfo.USER_EMAIL+" TEXT,"+
            UserDetailsContract.UserDetailsInfo.USER_PASSWORD+" TEXT,"+
            UserDetailsContract.UserDetailsInfo.USER_CONTACTNO+" INTEGER,"+
            UserDetailsContract.UserDetailsInfo.USER_AREA+" TEXT);";
    public static final String CREATE_BIKEDETAILS_QUERY = "create table "+
            BikeDetailsContract.BikeDetailsInfo.TABLE_NAME+"(id integer primary key, "+
            BikeDetailsContract.BikeDetailsInfo.BIKE_MAKE+" TEXT,"+
            BikeDetailsContract.BikeDetailsInfo.BIKE_MODEL+" TEXT,"+
            BikeDetailsContract.BikeDetailsInfo.BIKE_FRAMENO+" TEXT,"+
            BikeDetailsContract.BikeDetailsInfo.BIKE_COLOR+" TEXT);";
    public static final String CONTACTS_COLUMN_NAME = "name";
    private HashMap hp;

    /**
     * Method : DBHandler
     * Purpose : Checking whether db is already existing
     *
     */

    public DBHandler(Context context)
    {
        //if db version is not changed then the db will not be created again
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("constructor", "DB creation / open");
    }

    /**
     * Method : onCreate
     * Purpose :Creating tables for RegionBin,userdetails,bikedetails
     *
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Creating tables");
        db.execSQL(
                CREATE_REGIONBIN_QUERY
        );
        db.execSQL(
                CREATE_USERDETAILS_QUERY
        );
        db.execSQL(
                CREATE_BIKEDETAILS_QUERY
        );
        Log.e("onCreate", "Tables creation");
    }

    /**
     * Method : onUpgrade
     * Purpose :Dropping the table if there table exists
     *
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Dropping tables");
        String[] tables = TablesContract.TABLES;
        for(int tablecount =0; tablecount < tables.length ; tablecount++) {
            String table_name = tables[tablecount];
            db.execSQL("DROP TABLE IF EXISTS " + table_name);
        }
        onCreate(db);
    }

    /**
     * Method : insertRegionBins
     * Purpose :inserting the regions into sqlite -region name,region cordinatex,region cordinate y,
     * region weight,region is safe
     *
     */

    public boolean insertRegionBins  (String region_name, Double region_cord_x,
                                      Double region_cord_y, int region_weight, String region_isSafe)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RegionContract.RegionBinInfo.REGION_NAME, region_name);
        contentValues.put(RegionContract.RegionBinInfo.REGION_CORD_X, region_cord_x);
        contentValues.put(RegionContract.RegionBinInfo.REGION_CORD_Y, region_cord_y);
        contentValues.put(RegionContract.RegionBinInfo.REGION_WEIGHT, region_weight);
        contentValues.put(RegionContract.RegionBinInfo.REGION_ISSAFE, region_isSafe);
        db.insert(RegionContract.RegionBinInfo.TABLE_NAME, null, contentValues);
        return true;
    }
    /**
     * Method : insertUserData
     * Purpose :inserting the user data into sqlite
     *
     */
    public boolean insertUserData(String user_fname,String user_lname,String user_email
            ,String user_password,String user_contactno,String user_area) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_FNAME, user_fname);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_LNAME, user_lname);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_EMAIL, user_email);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_PASSWORD, user_password);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_CONTACTNO, user_contactno);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_AREA, user_area);
        db.insert(UserDetailsContract.UserDetailsInfo.TABLE_NAME, null, contentValues);
        return true;
    }
    /**
     * Method : insertUserData
     * Purpose :updating the user data into sqlite
     *
     */
    public boolean updateUserData(String user_fname,String user_lname,String user_contactno,
                                  String user_area)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_FNAME, user_fname);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_LNAME, user_lname);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_CONTACTNO, user_contactno);
        contentValues.put(UserDetailsContract.UserDetailsInfo.USER_AREA, user_area);
        Cursor idCursor = db.rawQuery("select id from " + UserDetailsContract.UserDetailsInfo.TABLE_NAME, null);
        idCursor.moveToFirst();
        final String user_id = idCursor.getString(0);
        db.update(UserDetailsContract.UserDetailsInfo.TABLE_NAME, contentValues, "id = ? ", new String[]{user_id});
        return true;
    }

    /**
     * Method : updateBikeData
     * Purpose :updating the bike data into sqlite
     *
     */
    public boolean updateBikeData(String bike_make,String bike_modelno,String bike_frameno,String bike_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_MAKE, bike_make);
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_MODEL, bike_modelno);
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_FRAMENO, bike_frameno);
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_COLOR, bike_color);
        Cursor idCursor = db.rawQuery("select id from " + BikeDetailsContract.BikeDetailsInfo.TABLE_NAME, null);
        idCursor.moveToFirst();
        final String bike_id = idCursor.getString(0);
        System.out.println("GOT THE ID" + bike_id);
        db.update(BikeDetailsContract.BikeDetailsInfo.TABLE_NAME, contentValues, "id = ? ", new String[] {bike_id});
        return true;
    }

    /**
     * Method : getUserData
     * Purpose :Getting user data from the sqlite
     *
     */

    public Cursor getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+UserDetailsContract.UserDetailsInfo.TABLE_NAME, null );
        return res;
    }

    /**
     * Method : insertBikeData
     * Purpose :inserting bike data into sqlite
     *
     */

    public boolean insertBikeData(String bike_make,String bike_modelno,String bike_frameno,String bike_color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_MAKE, bike_make);
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_MODEL, bike_modelno);
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_FRAMENO, bike_frameno);
        contentValues.put(BikeDetailsContract.BikeDetailsInfo.BIKE_COLOR, bike_color);
        db.insert(BikeDetailsContract.BikeDetailsInfo.TABLE_NAME, null, contentValues);
        return true;
    }

    /**
     * Method : getBikeData
     * Purpose :Getting user data from the sqlite
     *
     */

    public Cursor getBikeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + BikeDetailsContract.BikeDetailsInfo.TABLE_NAME,
                null);
        return res;
    }

    /**
     * Method : getAllRegionBins
     * Purpose :This method is used for getting region data using array
     *
     */
    public ArrayList getAllRegionBins() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+RegionContract.RegionBinInfo.TABLE_NAME, null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(RegionContract.RegionBinInfo.REGION_NAME)));
            res.moveToNext();
        }
        return array_list;
    }


}
