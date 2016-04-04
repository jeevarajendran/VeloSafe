package com.techgenie.velosafe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class Name : BikeDetailsPopupActivity
 * Purpose : The purpose of this activity is it displays bike details in report
 */

public class BikeDetailsPopupActivity extends AppCompatActivity {

    Context context = this;
    DBHandler myDB = null;
    SQLiteDatabase db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new DBHandler(context);
        Cursor bikeDetailsCursor = myDB.getBikeData();
        System.out.println(bikeDetailsCursor);
        bikeDetailsCursor.moveToFirst();
        final String bike_make = bikeDetailsCursor.getString(1);
        final String bike_model = bikeDetailsCursor.getString(2);
        final String bike_frame = bikeDetailsCursor.getString(3);
        final String bike_color = bikeDetailsCursor.getString(4);
        setContentView(R.layout.activity_bike_details_popup);
        ((TextView)findViewById(R.id.BikeMakeValue)).setText(bike_make);
        ((TextView)findViewById(R.id.BikeModelValue)).setText(bike_model);
        ((TextView)findViewById(R.id.BikeFrameValue)).setText(bike_frame);
        ((TextView)findViewById(R.id.BikeColorValue)).setText(bike_color);
        Button bikeEdit = (Button) findViewById(R.id.bikeEdit);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        bikeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bikeEditIntent = new Intent(BikeDetailsPopupActivity.this,
                        EditBikePopActivity.class);
                bikeEditIntent.putExtra("bike_make", bike_make);
                bikeEditIntent.putExtra("bike_model", bike_model);
                bikeEditIntent.putExtra("bike_frame", bike_frame);
                bikeEditIntent.putExtra("bike_color", bike_color);
                startActivity(bikeEditIntent);
                finish();
            }
        });
    }


}

