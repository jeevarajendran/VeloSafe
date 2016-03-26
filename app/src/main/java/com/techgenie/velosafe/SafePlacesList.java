package com.techgenie.velosafe;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SafePlacesList extends AppCompatActivity {
    String dataFromHeatMap;
    JSONArray listJson;
    SimpleAdapter sa;
    ArrayList<Map<String, String>> list;
    Context context;
    String SelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_places_list);
       
        final Intent intent = getIntent();
        dataFromHeatMap= intent.getStringExtra("ListJson");
        try {
            listJson = new JSONArray(dataFromHeatMap);
            Log.d("list Json",dataFromHeatMap);
        }

        catch (Exception e){
            Log.d("Error :: "," Retriving data from HeatMap Activity...");
        }
        int i;

        list = buildData();
        final ListView myListView = (ListView) findViewById(R.id.listView);
         sa = new SimpleAdapter(this,list,android.R.layout.simple_list_item_2,new String[] {"Places", "Distance"},new int[] {android.R.id.text1, android.R.id.text2});
         myListView.setAdapter(sa);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                JSONObject jsonObject;
                try {

                    SelectedItem = list.get(position).get("Places").toString();
                    HeatMap heatMap=new HeatMap();
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(getLocationOfSelectedItem(SelectedItem)).zoom(15).build();
                    HeatMap.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    SafePlacesList.this.finish();

                }
                catch (Exception e) {
                    Log.d("Error :: "," Item Listener...");
                }
          }
        });
    }

    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int i;
        JSONObject jsonObject = new JSONObject();
        for(i=0;i<listJson.length();i++)
        {
            try {
                jsonObject = listJson.getJSONObject(i);
                list.add(putData(jsonObject.getString("Region"),jsonObject.getString("Distance") + " Km"));
            }
            catch (Exception e){
                Log.d("Error :: "," Creating Array List for List View...");
            }
        }
        Log.d("Listview", list.toString());
        return list;
    }

    private HashMap<String, String> putData(String name, String purpose) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("Places", name);
        item.put("Distance", purpose);
        return item;
    }

    public LatLng getLocationOfSelectedItem(String selectedItem) {
        int i;
        LatLng selectedLocation;
        JSONObject jsonObject = new JSONObject();
        for (i = 0; i < listJson.length(); i++) {
            try {
                jsonObject = listJson.getJSONObject(i);
                if (jsonObject.getString("Region").equals(selectedItem)) {
                    selectedLocation = new LatLng(Double.parseDouble(jsonObject.getString("Latitude")), Double.parseDouble(jsonObject.getString("Longitude")));
                    Log.d(String.valueOf(selectedLocation.latitude),String.valueOf(selectedLocation.longitude));
                    return selectedLocation;
                }
            } catch (Exception e) {
                Log.d("Error :: "," Get Location of Selectedd Item.. ");
            }
         }

        return null;
        }

}
