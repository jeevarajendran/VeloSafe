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
 * Class Name : RegistrationActivity
 * Purpose : In this registration activity user have to enter the personal details.validating
 *  the personal details.If any error occur it will shows the error
 *
 */
public class RegistrationActivity extends AppCompatActivity {
    private final String TAG = RegistrationActivity.class.getSimpleName();
    private static final int REQUEST_REGISTRATION = 100;
    AppCompatButton continueRegistration = null;
    EditText inputEmail = null;
    EditText inputPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        continueRegistration = (AppCompatButton) findViewById(R.id.continue_registration);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        continueRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToPersonal();
            }
        });
    }
    /* method:continueToPersonal
     * purpose:checking the Email and password are filled.If it is filled the user can continue the personal details
     *
     */

    public void continueToPersonal() {
        Log.d(TAG, "continueToPersonal");
        if (!validate()) {
            onContinueFailed();
            return;
        }
        Intent intent = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
        intent.putExtra("inputEmail",inputEmail.getText().toString());
        intent.putExtra("inputPassword",inputPassword.getText().toString());
        startActivityForResult(intent, REQUEST_REGISTRATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTRATION) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    /* method:onContinueFailed
     * purpose:Method for error displaying when fields are not complete or error is there
     *
     */

    public void onContinueFailed() {
        Toast.makeText(getBaseContext(), "Correct the  errors to continue!", Toast.LENGTH_LONG).show();
        continueRegistration.setEnabled(true);
    }

    /* method:validate
     * purpose:Validating the user
     *
     */

    public boolean validate() {
        boolean valid = true;
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("Atleast 4 characters should be there and it contain alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }
}
