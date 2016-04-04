package com.techgenie.velosafe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * Class Name : BikeDetailsActivity
 * Purpose : The purpose of this activity the after registering the user the user details and bike
 * details collected and push intio the server.and collecting the region bit and inserting data into
 * the sqlite database
 */

public class BikeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BikeDetailsActivity";
    private static final int REQUEST_BIKE = 101;
    private static int RESULT_LOAD_IMAGE = 102;
    Context context = this;
    DBHandler myDB = null;
    Button registerButton = null;
    EditText bikeMake = null;
    EditText bikeModelNo = null;
    EditText bikeFrameNo = null;
    EditText bikeColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_details);
        registerButton = (Button) findViewById(R.id.register_button);
        bikeMake = (EditText) findViewById(R.id.bike_make);
        bikeModelNo = (EditText) findViewById(R.id.bike_model_number);
        bikeFrameNo = (EditText) findViewById(R.id.bike_frame_number);
        bikeColor = (EditText) findViewById(R.id.bike_color);
        final Intent inputIntent = getIntent();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(inputIntent);

            }
        });
    }
    /*method:loadPictureButtonOnClick
     purpose:This method describes the loading the image.If any error occurs it will shows the error
     */
    public void loadPictureButtonOnClick(View v){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data,"image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            InputStream inputStream;
            try {
               inputStream = getContentResolver().openInputStream(selectedImage);
                Toast.makeText(this, "Displayed Image", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open the image", Toast.LENGTH_LONG).show();
            }
        }
    }
    /*method:signup
     purpose:This method shows only the validate user can goes to the other page
     */

    public void signup(final Intent inputIntent){
        Log.d(TAG, "signup");

        if (!validate()) {
            onContinueFailed();
            return;
        }

        new Thread(new Runnable() {

            public void run() {
                try{
                    String returnString="";
                    HttpURLConnection connection=new Connection().connect();
                    Log.d("Connected to URL", connection.toString());
                    String inputEmail = inputIntent.getExtras().getString("inputEmail","");
                    String inputPassword = inputIntent.getExtras().getString("inputPassword","");
                    String inputFirstName = inputIntent.getExtras().getString("inputFirstName", "");
                    String inputLastName = inputIntent.getExtras().getString("inputLastName", "");
                    String inputContactNo = inputIntent.getExtras().getString("inputContactNo", "");
                    String inputArea = inputIntent.getExtras().getString("inputArea", "");
                    String inputBikeMake = bikeMake.getText().toString();
                    String inputBikeModelNo = bikeModelNo.getText().toString();
                    String inputBikeFrameNo = bikeFrameNo.getText().toString();
                    String inputBikeColor = bikeColor.getText().toString();
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("user_email", inputEmail);
                    jsonObj.put("user_password",inputPassword);
                    jsonObj.put("user_firstName", inputFirstName);
                    jsonObj.put("user_lastName",inputLastName);
                    jsonObj.put("user_contactNo", inputContactNo);
                    jsonObj.put("user_area", inputArea);
                    jsonObj.put("bike_make", inputBikeMake);
                    jsonObj.put("bike_modelNo", inputBikeModelNo);
                    jsonObj.put("bike_frameNo", inputBikeFrameNo);
                    jsonObj.put("bike_color", inputBikeColor);
                    connection.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream ());
                    out.writeBytes("page=registration&reg_details=" + jsonObj.toString());
                    System.out.println("page=registration&reg_details=" + jsonObj.toString());
                    out.close();
                    System.out.println("1\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb=new StringBuilder();
                    while ((returnString = in.readLine()) != null)
                    {
                        System.out.println(returnString);
                        sb.append(returnString);
                    }
                    in.close();
                    String jsonString = sb.toString();
                    System.out.println("JSON String retrieved from server : " + jsonString);
                    if (jsonString.equals("Already registered")){
                        System.out.println("it is registered");
                        BikeDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(),
                                        "The email address has already been registered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    }

                    JSONArray jsonArrayResponse = new JSONArray(jsonString);
                    System.out.println("JSON Array retrieved from server : " + jsonArrayResponse);
                    JSONObject eachRegionBinJSONObject = null;
                    String region_name = null;
                    Double region_cord_x;
                    Double region_cord_y;
                    int region_weight;
                    String region_isSafe;
                    myDB = new DBHandler(context);
                    System.out.println("CONNECTED to SQLITE:" + myDB);
                    myDB.insertUserData(inputFirstName,inputLastName,inputEmail,inputPassword,inputContactNo,inputArea);
                    myDB.insertBikeData(inputBikeMake,inputBikeModelNo,inputBikeFrameNo,inputBikeColor);
                    for(int i=0;i<jsonArrayResponse.length();i++)
                    {
                        eachRegionBinJSONObject = jsonArrayResponse.getJSONObject(i);
                        region_name = eachRegionBinJSONObject.getString("region_name");
                        region_cord_x = eachRegionBinJSONObject.getDouble("region_cord_x");
                        region_cord_y = eachRegionBinJSONObject.getDouble("region_cord_y");
                        region_weight = eachRegionBinJSONObject.getInt("region_weight");
                        region_isSafe = eachRegionBinJSONObject.getString("region_isSafe");
                        myDB.insertRegionBins(region_name,region_cord_x,region_cord_y,region_weight,region_isSafe);
                    }

                    String name = "";
                    ArrayList testAL = myDB.getAllRegionBins();
                    Iterator il = testAL.iterator();
                    while(il.hasNext())
                    {
                        name = (String) il.next();
                        System.out.println(name);
                    }
                    myDB.close();
                }catch(Exception e)
                {
                    Log.d("Exception", e.toString());
                }
            }
        }).start();
    }

    /*method:onContinueFailed
    purpose:This method shows error message.only the user filled all field.User can goes to other
    pages
    */
    public void onContinueFailed() {
        Toast.makeText(getBaseContext(), "Correct errors to continue!", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }
    /*method:validate(
   purpose:This method is to validate.Then it returns
   */
    public boolean validate() {
        return true;
    }

}
