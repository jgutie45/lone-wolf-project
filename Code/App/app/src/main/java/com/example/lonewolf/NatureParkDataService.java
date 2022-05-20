package com.example.lonewolf;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NatureParkDataService {

    public static final String QUERY_FOR_PARK_ID = "https://developer.nps.gov/api/v1/parks?id=";
    public static final String QUERY_PARK_STATE = "https://developer.nps.gov/api/v1/parks?id=";
    public static final String API_KEY = "&api_key=R3mKHa4tYhdbNUeu2LouWyEgNMQxxeoBQidQit1W";
    Context context;
    String parkIDs = "";
    String parkNames = "";

    public NatureParkDataService(Context context) {
        this.context = context;
    }

    public interface  VolleyResponseListener{
        void onError(String message);

        void onResponse(String parkIDs);
    }

    public void getParkID(String parkName, VolleyResponseListener volleyResponseListener){
      String  url = "https://developer.nps.gov/api/v1/parks?fullName=" + parkName + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            parkIDs = "";
            parkNames="";

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject parkID = jsonArray.getJSONObject(i);
                    //parkNames = parkID.getString("fullName");

                    if(parkName == parkID.getString("id")){
                        parkIDs = parkID.getString("id");
                    }
                    //Toast.makeText(activity_json_list.this, "Park Name: " + parkNames, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            //
            //Toast.makeText(context, "ParkID: " + parkIDs, Toast.LENGTH_SHORT).show();
            volleyResponseListener.onResponse(parkIDs);
        }, error -> volleyResponseListener.onResponse("Something is wrong"));

        MySingleton.getInstance(context).addToRequestQueue(request);

        //return parkIDs;
    }

    public interface  ParkByIDResponse{
        void onError(String message);

        void onResponse(List<ParkReportModel> parkReportModels);
    }

    public void getParkByName(String parkName, ParkByIDResponse parkByIDResponse){
        List<ParkReportModel> parkNameModel = new ArrayList<>();

        String url = QUERY_PARK_STATE + parkName +"&api_key=R3mKHa4tYhdbNUeu2LouWyEgNMQxxeoBQidQit1W";
        //Log.i("URL 3rdbtn", url);
        //Toast.makeText(context, "URL IS " + url, Toast.LENGTH_LONG).show();
        // grab the json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            //parkIDs = "";
            try {
                JSONArray jsonArray = response.getJSONArray("data");


                for (int i = 0; i < jsonArray.length(); i++) {
                    // Get the first item in the array
                    ParkReportModel One_Park = new ParkReportModel();

                    JSONObject first_data_from_api = (JSONObject) jsonArray.get(i);
                    One_Park.setId(first_data_from_api.getString("id"));
                    One_Park.setFullName(first_data_from_api.getString("fullName"));
                    One_Park.setParkCode(first_data_from_api.getString("parkCode"));
                    One_Park.setDescription(first_data_from_api.getString("description"));
                    One_Park.setLatitude(first_data_from_api.getDouble("latitude"));
                    One_Park.setLongitude(first_data_from_api.getDouble("longitude"));
                    One_Park.setLatLong(first_data_from_api.getString("latLong"));
                    One_Park.setStates(first_data_from_api.getString("states"));
                    One_Park.setDirectionsInfo(first_data_from_api.getString("directionsInfo"));

                    parkNameModel.add(One_Park);
                }
                parkByIDResponse.onResponse(parkNameModel);

            } catch (JSONException e) {
                Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }, error -> {

        });

            // grab the "data" array in the json object

        // get each item in the array

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface  GetParkByNameID{
        void onError(String message);
        void onResponse(List<ParkReportModel> parkReportModels);
    }

    public void getParkByLangLong(String parkName, GetParkByNameID getParkByNameID){
        // fetch park id given the name
        getParkID(parkName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String parkIDs) {
                getParkByName(parkIDs, new ParkByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<ParkReportModel> parkReportModels) {
                        //We have the report now for the park!
                        getParkByNameID.onResponse(parkReportModels);
                    }
                });
            }
        });

        // fetch the name id given park name
    }

}
