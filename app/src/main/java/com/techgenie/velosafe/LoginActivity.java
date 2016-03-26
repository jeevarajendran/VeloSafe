package com.techgenie.velosafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import android.view.View;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    Context context = this;

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
                            URL url = new URL("http://10.6.62.30:8080/MainHandler/ServerHandler/MainHandler");
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

//        loginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();


        new Thread(new Runnable() {
            public void run() {

                try{
//                    URL url = new URL("http://10.6.45.178:8080/velotest/MainHandler");
//                    URLConnection connection = url.openConnection();
//
//                    Log.d("Connected to URL", connection.toString());
//
                    String email = inputEmail.getText().toString();
                    String password = inputPassword.getText().toString();
//                    //inputString = URLEncoder.encode(inputString, "UTF-8");
//
//                    Log.d("inputEmail", email);
//
//                    connection.setDoOutput(true);
//
//                    Log.d("set Do Output","Done");
//
//
//                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//
//                    Log.d("Out : ", out.toString());
//
//                    out.write(email);
//
//                    Log.d("wrote :", "Wrote");
//
//                    out.close();
//
//                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//
//                    Log.d("in reader", in.toString());
//
//
//                    String returnString="";
//                    //doubledValue =0;
//                    responseString = "";
//                    while ((returnString = in.readLine()) != null)
//                    {
//                        responseString = returnString;
//                    }
//                    in.close();
                    URL url = new URL("http://192.168.0.15:8080/MainHandler/ServerHandler/MainHandler");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    Log.d("Connected to URL ****", connection.toString());
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("email", email);
                    jsonObj.put("password", password);
                    connection.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("page=login&login_details=" + jsonObj.toString());
                    out.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder sb=new StringBuilder();
                    String returnString;
                    while ((returnString = in.readLine()) != null)
                    {
                        sb.append(returnString);
                    }
                    in.close();
                    String returnMsg = sb.toString();
                    System.out.println("String retrieved from server : " + returnMsg);
                    String parts[] = returnMsg.split("&");
                    if (parts[0].equals("not match")){
                        System.out.println("password not match");
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "The email and the password do not match", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if(parts[0].equals("not registerd")){
                        System.out.println("email not registered");
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "This email has not been registered", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if(parts[0].equals("login successful")){
                        System.out.println("login successful");
                        String jsonString = parts[1];
                        JSONArray jsonArrayResponse = new JSONArray(jsonString);
                        System.out.println("JSON Array retrieved from server : " + jsonArrayResponse);

                        JSONObject eachRegionBinJSONObject = null;
                        String region_name;
                        Double region_cord_x;
                        Double region_cord_y;
                        int region_weight;
                        String region_isSafe;
                        DBHandler myDB = new DBHandler(context);

                        for(int i=0;i<jsonArrayResponse.length();i++)
                        {
                            eachRegionBinJSONObject = jsonArrayResponse.getJSONObject(i);
                            region_name = eachRegionBinJSONObject.getString("region_name");
                            region_cord_x = eachRegionBinJSONObject.getDouble("region_cord_x");
                            region_cord_y = eachRegionBinJSONObject.getDouble("region_cord_y");
                            region_weight = eachRegionBinJSONObject.getInt("region_weight");
                            region_isSafe = eachRegionBinJSONObject.getString("region_isSafe");



                            myDB.insertRegionBins(region_name,region_cord_x,region_cord_y,region_weight, region_isSafe);

                        }
                        myDB.close();
                        System.out.println("done with saving to sqlite part");
                        Intent intent = new Intent(getApplicationContext(), HeatMap.class);
                        startActivity(intent);
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "Logged in successfully!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }




//                    runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                                    R.style.AppTheme);
//                            progressDialog.setIndeterminate(true);
//                            progressDialog.setMessage("Loging in...");
//                            progressDialog.show();
//                            Toast.makeText(getBaseContext(), "Login successful. Welcome", Toast.LENGTH_LONG).show();
//
//                        }
//                    });

                }catch(Exception e)
                {
                    Log.d("Exception",e.toString());
                }
            }
        }).start();
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    /*@Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }*/

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
