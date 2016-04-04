package com.techgenie.velosafe;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.json.JSONArray;
import org.json.JSONObject;


/*
Class SafePlacesList
purpose: generates listview from the data received from the server.

 */

public class SafePlacesList extends AppCompatActivity {
    String dataFromHeatMap;
    JSONArray listJson;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_places_list);
        final Intent intent = getIntent();
        dataFromHeatMap= intent.getStringExtra("ListJson");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);

        try{
            listJson = new JSONArray(dataFromHeatMap);
            Log.d("list Json", dataFromHeatMap);
        }

        catch (Exception e){
            Log.d("Error :: "," Retriving data from HeatMap Activity...");
        }


        final ArrayList<ListDefinitions> listDefinitions = PutDataInArrayList(dataFromHeatMap);
        final ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CustomisedAdapterForSafePlacesList(this, listDefinitions));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {

                try {

                    String SelectedItem = listDefinitions.get(position).getPlaces();
                    Log.d("selected", SelectedItem);
                    HeatMap heatMap = new HeatMap();
                    CameraPosition cameraPosition = new CameraPosition.Builder().target
                            (getLocationOfSelectedItem(SelectedItem)).zoom(15).build();
                    heatMap.mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (cameraPosition));
                    SafePlacesList.this.finish();

                } catch (Exception e) {
                    Log.d("Error :: ", " Item Listener...");
                }
            }
        });

    }

    private ArrayList<ListDefinitions> PutDataInArrayList(String data){
        ArrayList<ListDefinitions> set = new ArrayList<ListDefinitions>();
        JSONArray jsonArray;
        JSONObject jsonObject;
        ListDefinitions ld;
        try {
            jsonArray = new JSONArray(data);
            int i;
            for(i=0;i<jsonArray.length();i++)
            {
                ld=new ListDefinitions();
                jsonObject=jsonArray.getJSONObject(i);
                Log.d("asdasda",jsonObject.toString());
                ld.setPlaces(jsonObject.getString("Region"));
                ld.setDistance(jsonObject.getString("Distance") + " Km");
                ld.setColour(jsonObject.getString("Colour"));
                set.add(ld);
            }
        }
        catch(Exception e){}

        return set;
    }

    public LatLng getLocationOfSelectedItem(String selectedItem) {
        int i;
        LatLng selectedLocation;
        JSONObject jsonObject = new JSONObject();
        for (i = 0; i < listJson.length(); i++) {
            try {
                jsonObject = listJson.getJSONObject(i);
                if (jsonObject.getString("Region").equals(selectedItem)) {
                    selectedLocation = new LatLng(Double.parseDouble(jsonObject
                            .getString("Latitude")), Double.parseDouble(jsonObject
                            .getString("Longitude")));
                    Log.d(String.valueOf(selectedLocation.latitude),String
                            .valueOf(selectedLocation.longitude));
                    return selectedLocation;

                }
            } catch (Exception e) {
                Log.d("Error :: "," Get Location of Selectedd Item.. ");
            }
        }

        return null;
    }

}