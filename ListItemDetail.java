//
//  onPoint
//
//  Created by Giuliano Gottini on 2014/09/10.
//  Copyright (c) 2014 Giuliano Gottini. All rights reserved.
//
package gottini.giuliano.onpoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ListItemDetail extends Activity implements View.OnClickListener {

    private GoogleMap googleMap;

    private Button arr, dep, stat;
    private EditText s;
    TextView myTextView;
    String objID;
    ParseObject currentRide;
    Double lati, longi;

    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_detail);
        initialize();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


         //if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) { Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ); startActivity(myIntent); }


        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

            //TP = googleMap.addMarker(new MarkerOptions().
            //        position(TutorialsPoint));

        } catch (Exception e) {
            // Log.e(TAG, "error: " + e);
        }



        // Here we turn your string.xml in an array
        final String[] myKeys = intent.getStringArrayExtra("array");
        //objID = intent.getStringExtra("objID");


        myTextView = (TextView) findViewById(R.id.dd);
        myTextView.setText("Trip " + myKeys[position]);

        Intent i = getIntent();
        int p = i.getIntExtra("position", 0);
        String[] mKeys = i.getStringArrayExtra("array");

        final String h = mKeys[p];

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Available_Rides");
        query.whereEqualTo("TripID", h);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> rideList, ParseException e) {
                if (e == null) {
                    objID = rideList.get(0).getObjectId();
                    currentRide = rideList.get(0);

                    GPSTracker mGPS = new GPSTracker(ListItemDetail.this);

                    if(mGPS.canGetLocation ){
                        mGPS.getLocation();
                        lati = mGPS.getLatitude();
                        longi = mGPS.getLongitude();
                        ParseGeoPoint placi = new ParseGeoPoint(lati, longi);
                        currentRide.put("location", placi);
                        currentRide.saveInBackground();

                    }else{
                        System.out.println("Unable");
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                Log.d("ListItemDetail", "hehe");

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Available_Rides");
                query.whereEqualTo("TripID", h);
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> rideList, ParseException e) {
                        if (e == null) {
                            objID = rideList.get(0).getObjectId();
                            currentRide = rideList.get(0);

                            GPSTracker mGPS = new GPSTracker(ListItemDetail.this);

                            if(mGPS.canGetLocation ){
                                mGPS.getLocation();
                                lati = mGPS.getLatitude();
                                longi = mGPS.getLongitude();
                                ParseGeoPoint placi = new ParseGeoPoint(lati, longi);
                                currentRide.put("location", placi);
                                currentRide.saveInBackground();

                            }else{
                                System.out.println("Unable");
                            }

                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });

            }
        }, 120000);







    }

    private void initialize() {
        dep = (Button) findViewById(R.id.button);
        arr = (Button) findViewById(R.id.button2);
        stat = (Button) findViewById(R.id.button3);
        dep.setOnClickListener(this);
        arr.setOnClickListener(this);
        stat.setOnClickListener(this);
        s = (EditText) findViewById(R.id.editText2);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:

                Log.d("ListItemDetail", "but");




                        if (currentRide != null) {
                            // object will be your game score

                            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                                alert.setTitle("GPS Tracking");
                                alert.setMessage("Please Turn on GPS for Tracking...");
                                alert.show();
                                //Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                                //startActivity(myIntent);

                            }

                            currentRide.put("Departed", true);
                            currentRide.put("Arrived", false);
                            currentRide.saveInBackground();
                        } else {
                            // something went wrong
                        }


                break;
            case R.id.button2:
                if (currentRide != null) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(this);


                    // object will be your game score
                    currentRide.put("Departed", true);
                    currentRide.put("Arrived", true);
                    currentRide.saveInBackground();


                    alert.setTitle("GPS Tracking");
                    alert.setMessage("Status Updated, Turn Off GPS Now...");
                    alert.show();
                } else {
                    // something went wrong
                }

                break;
            case R.id.button3:


                final String status = s.getText().toString();
                if (currentRide != null) {
                    // object will be your game score
                    //currentRide.put("tripStatusUpdates", status);
                    //currentRide.put("tripStatusUpdatesTimes", new Date());
                    currentRide.saveInBackground();
                    currentRide.add("tripStatusUpdates", status);
                    currentRide.add("tripStatusUpdatesTimes", new Date());
                    currentRide.saveInBackground();


                    Toast.makeText(this, "Status Updated Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    // something went wrong
                }

                break;
        }

    }
}
