package com.techgenie.velosafe;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name : ServerCommunication
 * Purpose : This class describes the communication with the server and response from from the server
 */

public class ServerCommunication {
    JSONArray jsonArray;
    JSONObject jsonObject;
    public String url = "http://192.168.0.15:8080/MainHandler/ServerHandler/MainHandler";
    HttpClient velosafeClient;
    HttpPost httpPostRequest;
    ServerCommunication(Context HeatMapActivity) {
    }
        /* method:ContactServer
         * purpose:method to contacting with the server.Error will be displayed if its failed
         *
         * */

    public JSONArray ContactServer() {
        jsonArray = new JSONArray();
        jsonObject = new JSONObject();
        int i;
        try {
            jsonObject.put("name", "test");
            jsonArray = SendDataGetResponse(jsonObject);
        } catch (Exception e) {
            Log.d("Contacting to the server is failed", e.toString());
        }
        return  jsonArray;
    }
        /* method:SendDataGetResponse
         * purpose:This method shows the rsponse from the server
         *
         * */

    public JSONArray SendDataGetResponse(JSONObject jsonObject) {
        velosafeClient = new DefaultHttpClient();
        httpPostRequest = new HttpPost(url);
        JSONArray jsonResponseObjects;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("reg_details", jsonObject.toString()));
        try {
            String res="[{\"region_name\":\"DALKEY\",\"region_cord_y\":-6.105844," +
                    "\"region_cord_x\":53.277911,\"region_isSafe\":\"GREEN\",\"region_weight\":3}," +
                    "{\"region_name\":\"GLASNEVIN CEMETERY\",\"region_cord_y\":-6.28329449," +
                    "\"region_cord_x\":53.374966422,\"region_isSafe\":\"ORANGE\"," +
                    "\"region_weight\":4}, {\"region_name\":\"CASTLEKNOCK\"," +
                    "\"region_cord_y\":-6.35642224541,\"region_cord_x\":53.3755808716," +
                    "\"region_isSafe\":\"GREEN\",\"region_weight\":3}, {\"region_name\":\"ARTANE\"," +
                    "\"region_cord_y\":-6.2103384136,\"region_cord_x\":53.385206089," +
                    "\"region_isSafe\":\"RED\",\"region_weight\":7}, {\"region_name\":\"IKEA\"," +
                    "\"region_cord_y\":-6.27951794853,\"region_cord_x\":53.40956659," +
                    "\"region_isSafe\":\"ORANGE\",\"region_weight\":4}, " +
                    "{\"region_name\":\"RATHGAR\",\"region_cord_y\":-6.2692182659," +
                    "\"region_cord_x\":53.320552528,\"region_isSafe\":\"RED\",\"region_weight\":4}," +
                    " {\"region_name\":\"SMITHFIELD\",\"region_cord_y\":-6.2762563823," +
                    "\"region_cord_x\":53.352020631,\"region_isSafe\":\"ORANGE\",\"region_weight\":4}," +
                    "{\"region_name\":\"TRINITY\",\"region_cord_y\":-6.2576899," +
                    "\"region_cord_x\":53.3454782,\"region_isSafe\":\"RED\",\"region_weight\":5}," +
                    "{\"region_name\":\"FINGLAS\",\"region_cord_y\":-6.29517351," +
                    "\"region_cord_x\":53.39242822,\"region_isSafe\":\"GREEN\"," +
                    "\"region_weight\":3},{\"region_name\":\"MALAHADE\"," +
                    "\"region_cord_y\":-6.150977953,\"region_cord_x\":53.45421298," +
                    "\"region_isSafe\":\"RED\",\"region_weight\":7},{\"region_name\":\"DUNDRUM\"," +
                    "\"region_cord_y\":-6.243264,\"region_cord_x\":53.289132," +
                    "\"region_isSafe\":\"ORANGE\",\"region_weight\":4}," +
                    "{\"region_name\":\"DALKEY\",\"region_cord_y\":-6.105844," +
                    "\"region_cord_x\":53.277911,\"region_isSafe\":\"GREEN\",\"region_weight\":3}," +
                    "{\"region_name\":\"DRUMCONDRA\",\"region_cord_y\":-6.2537649," +
                    "\"region_cord_x\":53.3706206,\"region_isSafe\":\"ORANGE\"," +
                    "\"region_weight\":4},{\"region_name\":\"MARINO\",\"region_cord_y\":-6.230762375," +
                    "\"region_cord_x\":53.3703133,\"region_isSafe\":\"GREEN\",\"region_weight\":3}," +
                    " {\"region_name\":\"WHITEHALL\",\"region_cord_y\":-6.244323624," +
                    "\"region_cord_x\":53.3834211,\"region_isSafe\":\"RED\",\"region_weight\":7}," +
                    "{\"region_name\":\"SANTRY\",\"region_cord_y\":-6.25376499," +
                    "\"region_cord_x\":53.401437695,\"region_isSafe\":\"ORANGE\"," +
                    "\"region_weight\":4},{\"region_name\":\"BALLSBRIDGE\"," +
                    "\"region_cord_y\":-6.230523500000004,\"region_cord_x\":53.3288697," +
                    "\"region_isSafe\":\"RED\",\"region_weight\":7}," +
                    "{\"region_name\":\"DOLPHINES BARN\",\"region_cord_y\":-6.291460400000005," +
                    "\"region_cord_x\":53.3335324,\"region_isSafe\":\"ORANGE\",\"region_weight\":4}," +
                    "{\"region_name\":\"BALLYFERMOT\",\"region_cord_y\":-6.353559799999971," +
                    "\"region_cord_x\":53.3422507,\"region_isSafe\":\"ORANGE\",\"region_weight\":4}," +
                    "{\"region_name\":\"RAHENY\",\"region_cord_y\":-6.175094299999955," +
                    "\"region_cord_x\":53.3804065,\"region_isSafe\":\"ORANGE\",\"region_weight\":4}]";
            Log.d("response", res);
            jsonResponseObjects = new JSONArray(res);
            return jsonResponseObjects;
        } catch (Exception e) {
            Log.d("Response is failed",e.toString());
        }
        return null;
    }
}
