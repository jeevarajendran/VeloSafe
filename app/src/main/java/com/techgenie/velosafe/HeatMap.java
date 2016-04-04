package com.techgenie.velosafe;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;


/*
    Class HeatMap
    Purpose:
    This class generates map from google server, put markers, add colors to the regions and
    contact server to get the region bins
 */

public class HeatMap extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static GoogleMap mMap;
    JSONArray jsonArray;
    JSONObject jsonObject;
    LatLng dublin;
    ImageButton findSafePlacesButton, launchButton,spotMe;
    SupportMapFragment mapFragment;
    Button imgButton;
    ProgressBar spinner;
    Intent intent;
    LocationListener locationListener;
    LatLng currentLocation;
    String provider;
    public MarkerOptions c;
    GPSTracker gpsTracker;
    HeatMapDB heatMapDB;
    GoogleServer googleServer;
    ProgressDialog progress;
    LatLng locationFromSafePlaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(HeatMap.this);
        setContentView(R.layout.activity_heat_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initializeObjects();

        /*
        Method launchButton.setOnClickListener
        invokes map generation
         */
        launchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                launchButton.setVisibility(View.INVISIBLE);
                findSafePlacesButton.setVisibility(View.VISIBLE);
                spotMe.setVisibility(View.VISIBLE);
                heatMapDB = new HeatMapDB(HeatMap.this);
                heatMapDB.truncate();
                generateMap();
            }
        });

        /*
        Method findsafePlacesButton.setOnClickListener
        invokes safePlacesActivity
         */

        findSafePlacesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner = (ProgressBar) findViewById(R.id.progressBar1);
                spinner.setVisibility(View.GONE);
                googleServer=new GoogleServer(HeatMap.this);
                Intent safePlacesActivity = new Intent(HeatMap.this,SafePlacesList.class);
                safePlacesActivity.putExtra("ListJson", heatMapDB.getAll(currentLocation)
                        .toString());
                startActivity(safePlacesActivity);
            }
        });

        /*
        Method spotMe.setOnClickListener
        focuses on the current location
         */

        spotMe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CameraPosition cameraPosition =
                        new CameraPosition.Builder().target(currentLocation).zoom(15).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

        /*
        Method initializeObjects
        initialize all the views
         */

    public void initializeObjects()
    {
        findSafePlacesButton = (ImageButton) findViewById(R.id.imageButton);
        findSafePlacesButton.setBackgroundColor(Color.TRANSPARENT);
        findSafePlacesButton.setVisibility(View.INVISIBLE);
        spotMe= (ImageButton) findViewById(R.id.spotMe);
        launchButton = (ImageButton) findViewById(R.id.adButton);
        spotMe.setVisibility(View.INVISIBLE);
        progress= new ProgressDialog(this);
        progress.setTitle("Velosafe");
        progress.setMessage("Fetching Data From Server..");

    }

    /*
    Method zoomLocation
    zoom in map to current location
     */
    public void zoomLocation(LatLng location)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /*
    Method generateMap
    generates map, get region bins and plot markers on it
 */

    public void generateMap() {
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            currentLocation = new LatLng(latitude, longitude);
            locationFromSafePlaces=new LatLng(currentLocation.latitude,currentLocation.longitude);
            c = new MarkerOptions().position(currentLocation).title("You are here!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(c);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation)
                    .zoom(11).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            ServerCommunication communication = new ServerCommunication(HeatMap.this);
            jsonArray = communication.ContactServer();
            progress.dismiss();
            PlotMarkers(jsonArray);
        }

        else {
            gpsTracker.showSettingsAlert();
        }
    }

    /*
        Method PlotMarkers
        creates markers and region colors around it
     */

    public void PlotMarkers(JSONArray jsonArray) {
        final String RED = "RED";
        final String GREEN = "GREEN";
        final String ORANGE = "ORANGE";
        int i;
        mMap.clear();
        updatePosition();

        for (i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                switch (jsonObject.getString("region_isSafe")) {
                    case RED:
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .title(jsonObject.getString("region_name"))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable
                                        .parking_red)));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .radius(1000)
                                .fillColor(0x20ff0000)
                                .strokeColor(Color.RED)
                                .strokeWidth(5));


                        break;
                    case GREEN:
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .title(jsonObject.getString("region_name"))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable
                                        .parking_green)));

                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .radius(1000)
                                .fillColor(0x2000ff00)
                                .strokeColor(Color.GREEN)
                                .strokeWidth(5));
                        heatMapDB.insertRecord(jsonObject.getString("region_name"),
                                String.valueOf(jsonObject.getDouble("region_cord_x")),
                                String.valueOf(jsonObject.getDouble("region_cord_y")),
                                jsonObject.getString("region_isSafe"),currentLocation);


                        break;
                    case ORANGE:
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .title(jsonObject.getString("region_name"))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable
                                        .parking_yellow)));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .radius(1000)
                                .fillColor(0x40ffff00)
                                .strokeColor(Color.YELLOW)
                                .strokeWidth(5));
                        heatMapDB.insertRecord(jsonObject.getString("region_name"),
                                String.valueOf(jsonObject.getDouble("region_cord_x")),
                                String.valueOf(jsonObject.getDouble("region_cord_y")),
                                jsonObject.getString("region_isSafe"),currentLocation);


                        break;

                    default:
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .title(jsonObject.getString("region_name"))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                        .HUE_RED)));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(jsonObject.getDouble("region_cord_x"),
                                        jsonObject.getDouble("region_cord_y")))
                                .radius(1000)
                                .fillColor(Color.RED));
                        break;

                }
            } catch (Exception e) {
                Log.d("Error :: ", "Plot Marker Method..");
            }
        }
    }


    /*
    Method updatePosition
    get current location from gpsTracker and adds marker on that location
     */
    public void updatePosition()
    {

        if(gpsTracker.canGetLocation()){

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            currentLocation=new LatLng(latitude,longitude);
            c=new MarkerOptions().position(currentLocation).title("You are here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cycling));
            mMap.addMarker(c);

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    /*
        Method: killApp
        kills the application
     */
    public void killApp()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

        /*
        Method onMapReady
        Map initializer
         adds flag to keep screen on
         */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

        /*
        Method onResume
        defines what to do when application comes to foreground from the background
         */

    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.zoomLocation(locationFromSafePlaces);
    }
}