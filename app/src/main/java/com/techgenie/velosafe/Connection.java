package com.techgenie.velosafe;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/*
    Class Connection
    purpose: provide connectivity to the server
 */
public class Connection {

    public HttpURLConnection connect() {
        HttpURLConnection connection=null;
        try{
            URL url = new URL("http://192.168.0.15:8080/MainHandler/MainHandler");
             connection = (HttpURLConnection) url.openConnection();
        }
        catch(Exception e){
            Log.d("Error","unable t connect...");
        }
        return connection;
    }
}
