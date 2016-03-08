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

public class EditPersonalPopActivity extends AppCompatActivity {

    EditText inputFirstName = null;
    EditText inputLastName = null;
    EditText inputContactNo = null;
    EditText inputArea = null;

    Context context = this;
    DBHandler myDB = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_pop);

        inputFirstName = (EditText) findViewById(R.id.edit_personal_firstname);
        inputLastName = (EditText) findViewById(R.id.edit_personal_lastname);
        inputContactNo = (EditText) findViewById(R.id.edit_personal_contactno);
        inputArea = (EditText) findViewById(R.id.edit_personal_area);

        Button edit_personal_details_save = (Button) findViewById(R.id.edit_personal_details_save);


        Intent inputPersonalDetailsIntent = getIntent();

        final String user_fname = inputPersonalDetailsIntent.getStringExtra("user_fname");
        final String user_lname = inputPersonalDetailsIntent.getStringExtra("user_lname");
        final String user_contactno = inputPersonalDetailsIntent.getStringExtra("user_contactno");
        final String user_area = inputPersonalDetailsIntent.getStringExtra("user_area");

        inputFirstName.setText(user_fname);
        inputLastName.setText(user_lname);
        inputContactNo.setText(user_contactno);
        inputArea.setText(user_area);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        edit_personal_details_save.setOnClickListener(new View.OnClickListener() {
            EditText editFirstName = null;
            EditText editLastName = null;
            EditText editContactNo = null;
            EditText editArea = null;

            @Override
            public void onClick(View v) {
                editFirstName = (EditText) findViewById(R.id.edit_personal_firstname);
                editLastName = (EditText) findViewById(R.id.edit_personal_lastname);
                editContactNo = (EditText) findViewById(R.id.edit_personal_contactno);
                editArea = (EditText) findViewById(R.id.edit_personal_area);

                String editedFirstName = editFirstName.getText().toString();
                String editedLastName = editLastName.getText().toString();
                String editedContactNo = editContactNo.getText().toString();
                String editedtArea = editArea.getText().toString();

                myDB = new DBHandler(context);

                System.out.println("CONNECTED to SQLITE:" + myDB);

                //put the user details in SQLITE

                System.out.println("Updated ***********:"+myDB.updateUserData(editedFirstName, editedLastName, editedContactNo, editedtArea));
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}

