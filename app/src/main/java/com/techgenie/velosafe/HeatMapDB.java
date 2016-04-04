package com.techgenie.velosafe;

import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONObject;


/*
    Class HeatMapDB
    purpose: write region bins and location inforamation to the local database
 */
public class HeatMapDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "HeatMapDB.db";
    public static final String TABLE_NAME = "SafeRegions";

    private HashMap hp;
    Context HeatMapActivity;


    public HeatMapDB(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
        HeatMapActivity=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table SafeRegions" +
                        "(Region text, Latitude text,Longitude text, Colour text, Distance text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /*
        Method inserRecord
        Inserts  region, coordinates, distance from current location  and color data to the
        local database
     */
    public boolean insertRecord(String Region,String Latitude,String Longitude, String Colour,
                                LatLng currentLocation)
    {
        GoogleServer googleServer=new GoogleServer(HeatMapActivity);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Region", Region);
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        contentValues.put("Colour", Colour);
        contentValues.put("Distance", String.format("%.2f", (googleServer.ContactServer
                (currentLocation, new LatLng(Double.parseDouble(Latitude),
                        Double.parseDouble(Longitude))))));
        db.insert(TABLE_NAME, null, contentValues);
        Log.d("status", "record inserted");
        return true;
    }

    /*
     Method: truncate
     truncates local database
 */
    public void truncate()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from  " + TABLE_NAME);
    }

    /*
    Method: getAll
    returns all the records from the local database
     */

    public JSONArray getAll(LatLng currentLocation){
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME +
                " Order by CAST (Distance as real) ASC", null);
        res.moveToFirst();
        try {
            while (res.isAfterLast() == false) {
                jsonObject = new JSONObject();
                jsonObject.put("Region", res.getString(res.getColumnIndex("Region")));
                jsonObject.put("Latitude", res.getString(res.getColumnIndex("Latitude")));
                jsonObject.put("Longitude", res.getString(res.getColumnIndex("Longitude")));
                jsonObject.put("Colour", res.getString(res.getColumnIndex("Colour")));
                jsonObject.put("Distance", res.getString(res.getColumnIndex("Distance")));
                jsonArray.put(jsonObject);
                res.moveToNext();
            }

        }
        catch (Exception e){
            Log.d("Error :: "," retriving Data from Database...");
        }
        return jsonArray;
    }
}
