package com.techgenie.velosafe;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                whichPopIntent.putExtra("whichPopup","bike");
                startActivity(new Intent(ReportActivity.this, BikeDetailsPopupActivity.class));

            }
        });




    }

    public void PickLostDateonClick(View view) {
        System.out.println("hello");
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

    private void updateDateLabel(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        TextView textview = (TextView) findViewById(R.id.lost_time_date);
        textview.setText("Lost Date: " + sdf.format(myCalendar.getTime()));
    }



    public void PickLostTimeonClick(View view) {
        System.out.println("hello");
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

    private void updateTimeLabel(){
        String myFormat = "h:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        TextView textview = (TextView) findViewById(R.id.lost_time_time);
        textview.setText("Lost Time: " + sdf.format(myCalendar.getTime()));
    }

    public void PickLostLocationonClick(View view){
        System.out.println("select the location of lost.");
    }

    private void updatePlaceLabel(){
        TextView textview = (TextView) findViewById(R.id.lost_location);
        textview.setText("Lost Location: " + "To be done");
    }

   /* public void ReportSubmitButtononClick(View view){
        Intent intent = new Intent(this, ReportMapsActivity.class);
        startActivity(intent);
    }*/

}