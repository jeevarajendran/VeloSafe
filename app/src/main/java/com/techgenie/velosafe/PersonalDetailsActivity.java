package com.techgenie.velosafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalDetailsActivity extends AppCompatActivity {

    private final String TAG = PersonalDetailsActivity.class.getSimpleName();
    private static final int REQUEST_PERSONAL = 0;
    AppCompatButton continuePersonal = null;
    EditText inputFirstName = null;
    EditText inputLastName = null;
    EditText inputContactNo = null;
    EditText inputArea = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        inputFirstName = (EditText) findViewById(R.id.input_firstname);
        inputLastName = (EditText) findViewById(R.id.input_lastname);
        inputContactNo = (EditText) findViewById(R.id.input_contactno);
        inputArea = (EditText) findViewById(R.id.input_area);
        continuePersonal = (AppCompatButton) findViewById(R.id.continue_personal);
        final Intent inputIntent = getIntent();

        continuePersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToBike(inputIntent);
            }
        });
    }

    public void continueToBike(Intent inputIntent ) {
        Log.d(TAG, "continueToBike");

        // TODO : Enable validation later

        if (!validate()) {
            onContinueFailed();
            return;
        }

        continuePersonal.setEnabled(false);

        String inputEmail = inputIntent.getExtras().getString("inputEmail","");
        String inputPassword = inputIntent.getExtras().getString("inputPassword","");
        Intent intent = new Intent(getApplicationContext(), BikeDetailsActivity.class);
        intent.putExtra("inputEmail",inputEmail);
        intent.putExtra("inputPassword",inputPassword);
        intent.putExtra("inputFirstName",inputFirstName.getText().toString());
        intent.putExtra("inputLastName",inputLastName.getText().toString());
        intent.putExtra("inputContactNo",inputContactNo.getText().toString());
        intent.putExtra("inputArea",inputArea.getText().toString());

        startActivityForResult(intent, REQUEST_PERSONAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERSONAL) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onContinueFailed() {
        Toast.makeText(getBaseContext(), "Correct errors to continue!", Toast.LENGTH_LONG).show();
        continuePersonal.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;
        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String contactNo = inputContactNo.getText().toString();
        String area = inputArea.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            inputFirstName.setError("at least 3 characters");
            valid = false;
        } else {
            inputFirstName.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            inputLastName.setError("at least 3 characters");
            valid = false;
        } else {
            inputLastName.setError(null);
        }

        if (contactNo.isEmpty() || !android.util.Patterns.PHONE.matcher(contactNo).matches()) {
            inputContactNo.setError("enter a valid contact Number");
            valid = false;
        } else {
            inputContactNo.setError(null);
        }
        return valid;
    }
}