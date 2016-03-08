package com.techgenie.velosafe;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditBikePopActivity extends AppCompatActivity {

    EditText inputBikeMake = null;
    EditText inputBikeModel = null;
    EditText inputBikeFrame = null;
    EditText inputBikeColor = null;

    Context context = this;
    DBHandler myDB = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bike_pop);


        inputBikeMake = (EditText) findViewById(R.id.edit_bike_make);
        inputBikeModel = (EditText) findViewById(R.id.edit_bike_model);
        inputBikeFrame = (EditText) findViewById(R.id.edit_bike_frame);
        inputBikeColor = (EditText) findViewById(R.id.edit_bike_color);

        Button edit_bike_details_save = (Button) findViewById(R.id.edit_bike_details_save);


        Intent inputBikeDetailsIntent = getIntent();

        final String bike_make = inputBikeDetailsIntent.getStringExtra("bike_make");
        final String bike_model = inputBikeDetailsIntent.getStringExtra("bike_model");
        final String bike_frame = inputBikeDetailsIntent.getStringExtra("bike_frame");
        final String bike_color = inputBikeDetailsIntent.getStringExtra("bike_color");

        inputBikeMake.setText(bike_make);
        inputBikeModel.setText(bike_model);
        inputBikeFrame.setText(bike_frame);
        inputBikeColor.setText(bike_color);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        edit_bike_details_save.setOnClickListener(new View.OnClickListener() {
            EditText editBikeMake = null;
            EditText editBikeModel = null;
            EditText editBikeFrame = null;
            EditText editBikeColor = null;

            @Override
            public void onClick(View v) {
                editBikeMake = (EditText) findViewById(R.id.edit_bike_make);
                editBikeModel = (EditText) findViewById(R.id.edit_bike_model);
                editBikeFrame = (EditText) findViewById(R.id.edit_bike_frame);
                editBikeColor = (EditText) findViewById(R.id.edit_bike_color);

                String editedBikeMake = editBikeMake.getText().toString();
                String editedBikeModel = editBikeModel.getText().toString();
                String editedBikeFrame = editBikeFrame.getText().toString();
                String editedBikeColor = editBikeColor.getText().toString();

                myDB = new DBHandler(context);

                System.out.println("CONNECTED to SQLITE:" + myDB);

                //put the user details in SQLITE

                System.out.println("Updated ***********:"+myDB.updateBikeData(editedBikeMake, editedBikeModel, editedBikeFrame, editedBikeColor));
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
                finish();
            }

        });
    }

}