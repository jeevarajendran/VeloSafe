package com.techgenie.velosafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Region;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public boolean insertRecord(String Region,String Latitude,String Longitude, String Colour, LatLng currentLocation)
    {
        GoogleServer googleServer=new GoogleServer(HeatMapActivity);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Region", Region);
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        contentValues.put("Colour", Colour);
        contentValues.put("Distance", String.format("%.2f", (googleServer.ContactServer(currentLocation, new LatLng(Double.parseDouble(Latitude),Double.parseDouble(Longitude))))));
        db.insert(TABLE_NAME, null, contentValues);
        Log.d("status", "record inserted");
        return true;
    }

    public Cursor getData(String Colour){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SafeRegions where Colour="+Colour+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateRecord (String Region,String Latitude,String Longitude, String Colour)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Region", Region);
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        contentValues.put("Colour", Colour);
        db.update(TABLE_NAME, contentValues, "Region = ? ", new String[] { Region } );
        return true;
    }

    public Integer deleteRecord (String Region)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "Region = ? ",
                new String[] { Region });

    }

    public void truncate()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from  " + TABLE_NAME);
    }

    public JSONArray getAll(LatLng currentLocation){
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME +" Order by CAST (Distance as real) ASC", null);
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
