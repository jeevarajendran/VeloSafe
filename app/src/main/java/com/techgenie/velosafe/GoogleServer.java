package com.techgenie.velosafe;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


public class GoogleServer {
    private ProgressDialog dialog;
    HeatMap heatMap;
    Context HeatMapMainActivity;

    GoogleServer(Context HeatMapActivity) {
        dialog = new ProgressDialog(HeatMapActivity);
        HeatMapMainActivity=HeatMapActivity;
    }

    public double ContactServer(LatLng originCor,LatLng destinationCor) {
            double distance = SphericalUtil.computeDistanceBetween(originCor, destinationCor);
            return distance/1000;
    }

}