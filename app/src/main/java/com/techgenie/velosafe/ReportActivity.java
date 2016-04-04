package com.techgenie.velosafe;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*Class:ReportActivity
  purpose:In this reporting activity user is reporting about the bike theft.The user can choose the
  calendar,time,select the location.The other personal details and bike details will take from the
  sever.If the reporting is failed it will show error messages
 */
public class ReportActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        Button personalButton = (Button) findViewById(R.id.personalDeatilsButton);
        Button bikeButton = (Button) findViewById(R.id.BikeDeatilsButton);
        TextView lostDate = (TextView) findViewById(R.id.lost_time_date);
        final Intent whichPopIntent = new Intent(ReportActivity.this, PopActivity.class);
        updateDateLabel();
        updateTimeLabel();
        updatePlaceLabel();
        personalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichPopIntent.putExtra("whichPopup","personal");
                startActivity(new Intent(ReportActivity.this, PopActivity.class));

            }
        });
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichPopIntent.putExtra("whichPopup", "bike");
                startActivity(new Intent(ReportActivity.this, BikeDetailsPopupActivity.class));

            }
        });
    }

    /* method:requestNewInterstitial
     * purpose:method for advertisment.Loading the advertisment
     *
     */

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    /* method:PickLostDateonClick
     * purpose:method for calender.User can choose date
     */

    public void PickLostDateonClick(View view) {
        new DatePickerDialog(ReportActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }

    };

    /* method:updateDateLabel
     * purpose:Updating the bike theft date
     */

    private void updateDateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        TextView textview = (TextView) findViewById(R.id.lost_time_date);
        textview.setText("Lost Date: " + sdf.format(myCalendar.getTime()));
    }

    /* method:PickLostTimeonClick
     * purpose:user can choose time
     */

    public void PickLostTimeonClick(View view) {
        new TimePickerDialog(ReportActivity.this, time, myCalendar
                .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),false).show();

    }
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute){
            myCalendar.set(Calendar.HOUR_OF_DAY, hour);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }
    };

    /* method:PickLostTimeonClick
    * purpose:user can choose time
    *
    * */
    private void updateTimeLabel(){
        String myFormat = "h:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        TextView textview = (TextView) findViewById(R.id.lost_time_time);
        textview.setText("Lost Time: " + sdf.format(myCalendar.getTime()));
    }

    /* method:PickLostLocationonClick
     * purpose:Selecting the location of bike theft.By cllicking on the button
     *
     */

    public void PickLostLocationonClick(View view) {
        System.out.println("select the location of lost.");
    }
    /* method:updatePlaceLabel
     * purpose:Selecting the lost location
     *
     */
    private void updatePlaceLabel() {
        TextView textview = (TextView) findViewById(R.id.lost_location);
        textview.setText("Lost Location: " + "(53.39242822,-6.29517351)");
    }

    /* method:updatePlaceLabel
     * purpose:Reporting the bike the theft.when user click on report button.It get all the
     * data from
     * the server.If any error occurs reports will be failed
     *
     */

    public void ReportSubmitButtononClick(View view) {
        System.out.println("let's report!");
        final Context context = this;
        DBHandler myDB = new DBHandler(context);
        Cursor userDetailsCursor = myDB.getUserData();
        Cursor bikeDetailsCursor = myDB.getBikeData();
        bikeDetailsCursor.moveToFirst();
        userDetailsCursor.moveToFirst();
        final String first_name = userDetailsCursor.getString(1);
        final String last_name = userDetailsCursor.getString(2);
        final String contact_number = userDetailsCursor.getString(5);
        final String area = userDetailsCursor.getString(6);
        final String bike_make = bikeDetailsCursor.getString(1);
        final String bike_model = bikeDetailsCursor.getString(2);
        final String bike_frame = bikeDetailsCursor.getString(3);
        final String bike_color = bikeDetailsCursor.getString(4);
        myDB.close();
        new Thread(new Runnable() {
            public void run() {
                try {
                    HttpURLConnection connection=new Connection().connect();
                    Log.d("Connected to URL", connection.toString());
                    TextView lost_location = (TextView) findViewById(R.id.lost_location);
                    TextView lost_time_date = (TextView) findViewById(R.id.lost_time_date);
                    TextView lost_time_time = (TextView) findViewById(R.id.lost_time_time);
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("lost_location", lost_location.getText().toString());
                    jsonObj.put("lost_time_date", lost_time_date.getText().toString());
                    jsonObj.put("lost_time_time", lost_time_time.getText().toString());
                    jsonObj.put("first_name", first_name);
                    jsonObj.put("last_name", last_name);
                    jsonObj.put("contact_number", contact_number);
                    jsonObj.put("area", area);
                    jsonObj.put("bike_make", bike_make);
                    jsonObj.put("bike_model", bike_model);
                    jsonObj.put("bike_frame", bike_frame);
                    jsonObj.put("bike_color", bike_color);
                    System.out.println(jsonObj.toString());
                    connection.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("page=report&report_details=" + jsonObj.toString());
                    out.close();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection
                            .getInputStream()));
                    StringBuilder sb=new StringBuilder();
                    String returnString;
                    while ((returnString = in.readLine()) != null) {
                        sb.append(returnString);
                    }
                    in.close();
                    String jsonString = sb.toString();
                    System.out.println("JSON String retrieved from server : " + jsonString);
                } catch (Exception e) {
                    Log.d("Reporting Failed", e.toString());
                }

            }

        }).start();
    }
}
