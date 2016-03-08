package com.techgenie.velosafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

//import android.view.View;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText inputEmail = null;
    EditText inputPassword = null;
    TextView signupLink = null;
    Button loginButton= null;
    Integer doubledValue =0;
    String responseString= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.login_button);
        signupLink = (TextView) findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

                //delete these later
                /* new Thread(new Runnable() {
                    public void run() {

                        try{
                            URL url = new URL("http://192.168.56.1:8080/MainHandler/MainHandler");
                            URLConnection connection = url.openConnection();

                            Log.d("Connected to URL", connection.toString());

                            String email = inputEmail.getText().toString();
                            //inputString = URLEncoder.encode(inputString, "UTF-8");

                            Log.d("inputEmail", email);

                            connection.setDoOutput(true);

                            Log.d("set Do Output","Done");


                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

                            Log.d("Out : ", out.toString());

                            out.write(email);

                            Log.d("wrote :", "Wrote");

                            out.close();

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                            Log.d("in reader", in.toString());


                            String returnString="";
                            //doubledValue =0;
                            responseString = "";
                            while ((returnString = in.readLine()) != null)
                            {
                                responseString = returnString;
                            }
                            in.close();


                            runOnUiThread(new Runnable() {
                                public void run() {

                                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                                            R.style.AppTheme);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("Loging in...");
                                    progressDialog.show();

                                    Toast.makeText(getBaseContext(), "Login successful. Welcome", Toast.LENGTH_LONG).show();

                                }
                            });

                        }catch(Exception e)
                        {
                            Log.d("Exception",e.toString());
                        }
                    }
                }).start();*/

            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login(){

        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();


        new Thread(new Runnable() {
            public void run() {

                try{
                    URL url = new URL("http://192.168.56.1:8080/MainHandler/MainHandler");
                    URLConnection connection = url.openConnection();

                    Log.d("Connected to URL", connection.toString());

                    String email = inputEmail.getText().toString();
                    //inputString = URLEncoder.encode(inputString, "UTF-8");

                    Log.d("inputEmail", email);

                    connection.setDoOutput(true);

                    Log.d("set Do Output","Done");


                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

                    Log.d("Out : ", out.toString());

                    out.write(email);

                    Log.d("wrote :", "Wrote");

                    out.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                    Log.d("in reader", in.toString());


                    String returnString="";
                    //doubledValue =0;
                    responseString = "";
                    while ((returnString = in.readLine()) != null)
                    {
                        responseString = returnString;
                    }
                    in.close();


                    runOnUiThread(new Runnable() {
                        public void run() {

                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                                    R.style.AppTheme);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Loging in...");
                            progressDialog.show();

                            Toast.makeText(getBaseContext(), "Login successful. Welcome", Toast.LENGTH_LONG).show();

                        }
                    });

                }catch(Exception e)
                {
                    Log.d("Exception",e.toString());
                }
            }
        }).start();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

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