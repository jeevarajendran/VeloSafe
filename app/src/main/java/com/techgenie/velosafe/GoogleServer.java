package com.techgenie.velosafe;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


/*
Class GoogleServer
Purpose: Provide distance information between 2 locations
 */
public class GoogleServer {
    Context HeatMapMainActivity;

    /*
    Method: ContactServer
    returns distance between two coordinates
     */
    GoogleServer(Context HeatMapActivity) {

        HeatMapMainActivity=HeatMapActivity;
    }

    public double ContactServer(LatLng originCor,LatLng destinationCor) {
        double distance = SphericalUtil.computeDistanceBetween(originCor, destinationCor);
        return distance/1000;
    }

}