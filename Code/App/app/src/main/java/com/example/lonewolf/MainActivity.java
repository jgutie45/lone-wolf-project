package com.example.lonewolf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

/*
    The main event, the activity that the user will access all other activities within
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private View layout;
    public boolean hasPermission = false;
    private LocationManager lmngr;

    //overridden to check if location services are enabled and then ask for enabling when opening
    //the app
    @Override
    protected void onStart() {
        super.onStart();

        lmngr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final boolean isGPSEnabled = lmngr.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.constLayout);

        //authentication object and database object
        auth = FirebaseAuth.getInstance();

        //set the sign out button in the upper right hand corner of the screen
        TextView signOutUser = findViewById(R.id.signOutButton);
        signOutUser.setOnClickListener(v -> signOut());


        //set up the major buttons the user will be interacting with
        Button routeButton = findViewById(R.id.RouteButton);
        Button infoButton = findViewById(R.id.InformationButton);
        Button helpButton = findViewById(R.id.HelpButton);
        Button jsonListButton = findViewById(R.id.ListButton);
        Button uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        routeButton.setOnClickListener(mainListener1);
        infoButton.setOnClickListener(mainListener2);
        helpButton.setOnClickListener(mainListener3);
        jsonListButton.setOnClickListener(mainListener4);
        uploadPhotoButton.setOnClickListener(mainListener5);

        //get the permissions, if we don't have them already
        requestLocPerms();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            hasPermission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Log.i("Perms", String.valueOf(hasPermission));
        }
    }

    public View.OnClickListener mainListener1 = v -> findRouteButton();

    public View.OnClickListener mainListener2 = v -> informationFinderButton();

    public View.OnClickListener mainListener3 = v -> helpButton();

    public  View.OnClickListener mainListener4 = v -> listViews();

    public  View.OnClickListener mainListener5 = v -> uploadPicture();

    //The Route Finding Button; Allows the user to find a path through the national park they
    //are currently in and navigate through it
    public void findRouteButton() {
        Intent intent1 = new Intent(getApplicationContext(), RouteMaps.class);
        startActivity(intent1);
    }

    //The Encyclopedia Button; Allows the user to search up facts/information of the park they are
    //currently in.
    public void informationFinderButton() {
        Intent intent2 = new Intent(getApplicationContext(), EncycloActivity.class);
        startActivity(intent2);
    }

    //Button to allow user to upload photo.
    public void uploadPicture(){
        Intent intentUpload = new Intent(getApplicationContext(), add_pictures.class);
        startActivity(intentUpload);
    }

    //The button the user presses to request help from the nearest park ranger
    //REQUIRES: Access to user location services
    //  TODO: Figure out how to set the last known location from nothing
    public void helpButton() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        if (hasPermission) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                return;
            }
            Location l = new Location(LocationManager.GPS_PROVIDER);
            client.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                if (l != null) {
                    DialogFragment dF = new ConfirmHelpRequest(l.toString());
                    dF.show(getSupportFragmentManager(), "halp");
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Location Access Denied", Toast.LENGTH_SHORT).show();
        }

    }

    //request the location perms on first opening.
    //This does not ask the user to turn on their GPS, just to get perms to use the GPS service
    //on their device.
    private void requestLocPerms() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, "Need Location for you to get help",
                    Snackbar.LENGTH_INDEFINITE).setAction("Give Permission?",
                    v -> ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            1)).show();
        } else {
            Snackbar.make(layout, "Give Location Access?", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    private void listViews(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, activity_json_list.class);
        startActivity(intent);

    }

    //signs out the user and sends them back to the login screen
    private void signOut() {
        auth.signOut();
        finish();
    }

}