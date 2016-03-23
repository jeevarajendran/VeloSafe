package com.techgenie.velosafe;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class BikeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BikeDetailsActivity";
    private static final int REQUEST_BIKE = 101;
    private static int RESULT_LOAD_IMAGE = 102;

    Context context = this;
    DBHandler myDB = null;
    SQLiteDatabase db = null;

    Button registerButton = null;
    EditText bikeMake = null;
    EditText bikeModelNo = null;
    EditText bikeFrameNo = null;
    EditText bikeColor = null;
    ImageView bikeImage = null;
    ImageView imageView = null;
    Bitmap selectedImageBitmap = null;
    //DBHandler mydb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerButton = (Button) findViewById(R.id.register_button);
        bikeMake = (EditText) findViewById(R.id.bike_make);
        bikeModelNo = (EditText) findViewById(R.id.bike_model_number);
        bikeFrameNo = (EditText) findViewById(R.id.bike_frame_number);
        bikeColor = (EditText) findViewById(R.id.bike_color);
        bikeImage = (ImageView) findViewById(R.id.bike_picture);
        imageView = (ImageView) findViewById(R.id.bike_picture);
        final Intent inputIntent = getIntent();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(inputIntent);

            }
        });
    }

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
                selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                //imageView.setImageURI(selectedImage);
                imageView.setImageBitmap(selectedImageBitmap);
                Toast.makeText(this, "Displayed Image", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open the image", Toast.LENGTH_LONG).show();
            }
        }
    }

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
                    String responseString = "";
                    //URL url = new URL("https://pacific-scrubland-42954.herokuapp.com/MainHandler");
                    //URL url = new URL("http://10.6.56.150:8070/velosafe/Velosafe");
                    URL url = new URL("http://192.168.56.1:8080/MainHandler/ServerHandler/MainHandler");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    Log.d("Connected to URL ****", connection.toString());
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
                    byte[] bikeImageBytes = bikeImage.toString().getBytes();

                    Log.d("Connected to URL ***:", inputEmail);
                    //inputString = URLEncoder.encode(inputString, "UTF-8");
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

                    // Code for image

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                    //enable it later . This is needed . Just commenting out as because some issues with string parsing for images
                    //jsonObj.put("bike_image", encodedImage);

                    //System.out.println("Bitmap before sending :" + selectedImageBitmap);
                    //System.out.println("String before Sending :" + encodedImage);

                    connection.setDoOutput(true);

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream ());
                    out.writeBytes("page=registration&reg_details=" + jsonObj.toString());
                    out.close();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder sb=new StringBuilder();

                    // inData.read
                    while ((returnString = in.readLine()) != null)
                    {
                        sb.append(returnString);
                    }
                    in.close();

                    String jsonString = sb.toString();

                    System.out.println("JSON String retrieved from server : " + jsonString);

                    //JSONObject jsonObjResponse = new JSONObject(jsonString);
                    JSONArray jsonArrayResponse = new JSONArray(jsonString);
                    System.out.println("JSON Array retrieved from server : " + jsonArrayResponse);

                    JSONObject eachRegionBinJSONObject = null;
                    String region_name = null;
                    Double region_cord_x;
                    Double region_cord_y;
                    int region_weight;
                    myDB = new DBHandler(context);

                    System.out.println("CONNECTED to SQLITE:" + myDB);

                    //put the user details in SQLITE

                    myDB.insertUserData(inputFirstName,inputLastName,inputEmail,inputPassword,inputContactNo,inputArea);
                    myDB.insertBikeData(inputBikeMake,inputBikeModelNo,inputBikeFrameNo,inputBikeColor);

                    for(int i=0;i<jsonArrayResponse.length();i++)
                    {
                        eachRegionBinJSONObject = jsonArrayResponse.getJSONObject(i);
                        region_name = eachRegionBinJSONObject.getString("region_name");
                        region_cord_x = eachRegionBinJSONObject.getDouble("region_cord_x");
                        region_cord_y = eachRegionBinJSONObject.getDouble("region_cord_y");
                        region_weight = eachRegionBinJSONObject.getInt("region_weight");



                        myDB.insertRegionBins(region_name,region_cord_x,region_cord_y,region_weight);

                    }

                    String name = "";

                    ArrayList testAL = myDB.getAllRegionBins();
                    Iterator il = testAL.iterator();
                    while(il.hasNext())
                    {
                        name = (String) il.next();
                        System.out.println(name);
                    }

                    //Toast.makeText(getBaseContext(), "Sqlite connected", Toast.LENGTH_LONG).show();

                    myDB.close();
                    //String jsonbikeimagersponse = (String) jsonObjResponse.get("bike_image");
                    //System.out.println("String from server :" + jsonbikeimagersponse);
                    Intent intent = new Intent(getApplicationContext(), ImageTestActivity.class);
                    intent.putExtra("bike_image", encodedImage);
                    startActivityForResult(intent, REQUEST_BIKE);

                }catch(Exception e)
                {
                    Log.d("Exception", e.toString());
                }
            }
        }).start();
    }

    /*@Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }*/

    public void onContinueFailed() {
        Toast.makeText(getBaseContext(), "Correct errors to continue!", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    public boolean validate() {
        return true;
    }

}

