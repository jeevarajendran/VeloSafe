package com.techgenie.velosafe;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*Class Server Communication
purpose: connecting to the server and exchanging data
 */

public class ServerCommunication {
    JSONArray jsonArray;
    JSONObject jsonObject;
    String url;
    Context heatmap;
    ServerCommunication(Context HeatMapActivity) {
        heatmap=HeatMapActivity;
    }

    /*
    Method: Contact Server
    connects to the server.
     */

    public JSONArray ContactServer() {
        jsonArray = new JSONArray();
        jsonObject = new JSONObject();
        int i;
        try {
            url = "http://10.6.62.30:8080/MainHandler/MainHandler";
            jsonObject.put("name", "abhishek");
            ProgressDialog progress;
            progress = ProgressDialog.show(heatmap, "Velosafe",
                    "Retrieving information from server...", true);
            ServerCon serverCon = new ServerCon();
            String res = serverCon.execute().get();
            progress.dismiss();
            System.out.println("################" + res);
            jsonArray = new JSONArray(res);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return jsonArray;
    }
}


/*
    Class ServerCon
    purpose: connecting to the server and exchanging data in the background
 */
class ServerCon extends AsyncTask<String, Void, String> {
    static InputStream is = null;
    public ServerCon() {

    }

    @Override
    protected String doInBackground(String... params) {

        try
        {
            System.out.println("In doInBackgrund");
            String returnString="";
            HttpURLConnection connection=new Connection().connect();
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream ());
            out.writeBytes("page=heat_map");
            out.close();
            System.out.println("****************1\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection
                    .getInputStream()));
            StringBuilder sb=new StringBuilder();
            while ((returnString = in.readLine()) != null)
            {
                System.out.println(returnString);
                sb.append(returnString);
            }
            in.close();
            String jsonString = sb.toString();
            System.out.println("JSON String retrieved from server : " + jsonString);
            Log.d("response= ", jsonString);
            System.out.println("****************1\n");
            return jsonString;

        } catch (Exception e) {
            System.out.println("Exception in Get Data ...." + e);
        }
        return null;


    }
    protected void onPostExecute(String page)
    {
        //onPostExecute
    }
}
