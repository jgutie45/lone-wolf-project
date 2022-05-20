package com.example.lonewolf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class activity_json_list extends AppCompatActivity {
    Button btn_parkID;
    Button btn_getLongLangByID;
    Button btn_getLongLangByName;

    EditText et_dataInput;
    ListView lv_parkReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_list);

        // Assigning Values to each control of the layout
        btn_parkID = findViewById(R.id.btn_getParkID);
        btn_getLongLangByID = findViewById(R.id.btn_getLangLongByParkID);
        btn_getLongLangByName = findViewById(R.id.btn_getLangLongByParkName);

        et_dataInput = findViewById(R.id.et_datainput);
        lv_parkReport = findViewById(R.id.lv_parkReports);


        final NatureParkDataService natureParkDataService = new NatureParkDataService(activity_json_list.this);

        // click listeners for each button
        btn_parkID.setOnClickListener(view -> {


            // this did not return anything
            natureParkDataService.getParkID(et_dataInput.getText().toString(), new NatureParkDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(activity_json_list.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String parkIDs) {
                    Toast.makeText(activity_json_list.this, "Park Name is " + et_dataInput.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity_json_list.this, "Returned an ID of " + parkIDs, Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_getLongLangByName.setOnClickListener(view -> {

            natureParkDataService.getParkByName(et_dataInput.getText().toString(), new NatureParkDataService.ParkByIDResponse() {
                @Override
                public void onError(String message) {
                    Toast.makeText(activity_json_list.this, "something is wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<ParkReportModel> parkReportModels) {
                   // Make a list
                    ArrayAdapter arrayAdapter = new ArrayAdapter(activity_json_list.this, android.R.layout.simple_expandable_list_item_1, parkReportModels);
                    lv_parkReport.setAdapter(arrayAdapter);
                }
            });
            //Toast.makeText(activity_json_list.this, "You clicked me 2", Toast.LENGTH_SHORT).show();
        });

        btn_getLongLangByID.setOnClickListener(view -> {
            NatureParkDataService natureParkDataService1 = new NatureParkDataService(activity_json_list.this);

            natureParkDataService.getParkByLangLong(et_dataInput.getText().toString(), new NatureParkDataService.GetParkByNameID() {
                @Override
                public void onError(String message) {
                    Toast.makeText(activity_json_list.this, "something is wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<ParkReportModel> parkReportModels) {
                    // Make a list
                    ArrayAdapter arrayAdapter = new ArrayAdapter(activity_json_list.this, android.R.layout.simple_expandable_list_item_1, parkReportModels);
                    lv_parkReport.setAdapter(arrayAdapter);
                }
            });
            //Toast.makeText(activity_json_list.this, "You typed " + et_dataInput.getText().toString(), Toast.LENGTH_SHORT).show();
        });


    }
}