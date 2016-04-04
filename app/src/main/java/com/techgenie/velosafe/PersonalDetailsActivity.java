package com.techgenie.velosafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Class Name : PersonalDetailsActivity
 * Purpose : Registering the personal details.Validating the details.If any error occurs in these
 * fields error will show
 */
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

    /* method:continueToBike
     * purpose:checking all fields are filled.If all the fields are filled then only user can
     * goes continue the registration
     *
     */

    public void continueToBike(Intent inputIntent ) {
        Log.d(TAG, "continueToBike");
        if (!validate()) {
            onContinueFailed();
            return;
        }
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

    /* method:continueToBik
     * purpose:Method for error displaying when fields are not complete or when error is there
     */

    public void onContinueFailed() {
        Toast.makeText(getBaseContext(), "Correct the  errors to continue!", Toast.LENGTH_LONG)
                .show();
        continuePersonal.setEnabled(true);
    }

    /* method:continueToBik
     * purpose:validating the  personal details
     */

    public boolean validate() {
        boolean valid = true;
        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String contactNo = inputContactNo.getText().toString();
        String area = inputArea.getText().toString();
        if (firstName.isEmpty() || firstName.length() < 3) {
            inputFirstName.setError("At least 3 characters");
            valid = false;
        } else {
            inputFirstName.setError(null);
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            inputLastName.setError("At least 3 characters");
            valid = false;
        } else {
            inputLastName.setError(null);
        }
        if (contactNo.isEmpty() || !android.util.Patterns.PHONE.matcher(contactNo).matches()) {
            inputContactNo.setError("Enter a valid contact Number");
            valid = false;
        } else {
            inputContactNo.setError(null);
        }
        return valid;
    }
}