package com.techgenie.velosafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    public void continueToPersonal() {
        Log.d(TAG, "continueToPersonal");

        // TODO : enable validation later

        if (!validate()) {
            onContinueFailed();
            return;
        }

        continueRegistration.setEnabled(false);

        Intent intent = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
        intent.putExtra("inputEmail",inputEmail.getText().toString());
        intent.putExtra("inputPassword",inputPassword.getText().toString());
        startActivityForResult(intent, REQUEST_REGISTRATION);
    }

    /*public void onSignupSuccess() {
        createAccount.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTRATION) {
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
        continueRegistration.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        /* if (name.isEmpty() || name.length() < 3) {
            inputFirstName.setError("at least 3 characters");
            valid = false;
        } else {
            inputFirstName.setError(null);
        }
        */

        /*if (contactNo.isEmpty() || !android.util.Patterns.PHONE.matcher(contactNo).matches()) {
            inputContactNo.setError("enter a valid contact Number");
            valid = false;
        } else {
            inputContactNo.setError(null);
        }*/

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }
}