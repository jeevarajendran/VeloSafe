package com.techgenie.velosafe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class Name : PopActivity
 * Purpose : In this class editing the personal details
 *
 */

public class PopActivity extends AppCompatActivity {
    Context context = this;
    DBHandler myDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new DBHandler(context);
        Cursor userDetailsCursor = myDB.getUserData();
        userDetailsCursor.moveToFirst();
        final String user_fname = userDetailsCursor.getString(1);
        final String user_lname = userDetailsCursor.getString(2);
        final String user_contactno = userDetailsCursor.getString(5);
        final String user_area = userDetailsCursor.getString(6);
        setContentView(R.layout.activity_pop);
        ((TextView)findViewById(R.id.FirstNameValue)).setText(user_fname);
        ((TextView)findViewById(R.id.LastNameValue)).setText(user_lname);
        ((TextView)findViewById(R.id.ContactNoValue)).setText(user_contactno);
        ((TextView)findViewById(R.id.AreaValue)).setText(user_area);
        Button personalEdit = (Button) findViewById(R.id.personalEdit);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        personalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personalEditIntent = new Intent(PopActivity.this, EditPersonalPopActivity.class);
                personalEditIntent.putExtra("user_fname",user_fname);
                personalEditIntent.putExtra("user_lname",user_lname);
                personalEditIntent.putExtra("user_contactno",user_contactno);
                personalEditIntent.putExtra("user_area",user_area);
                startActivity(personalEditIntent);
                finish();
            }
        });
    }
}
