//
//  onPoint
//
//  Created by Giuliano Gottini on 2014/09/10.
//  Copyright (c) 2014 Giuliano Gottini. All rights reserved.
//

package gottini.giuliano.onpoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

public class TripDetails extends Activity {


    private final String TAG = "TripDetails";
    //private ParseObject rideToMonitor = new ParseObject("Available_Rides");
    String text;

    private ProgressDialog progressDialog;

    TextView status, update, des, lu;
    LatLng TutorialsPoint = new LatLng(0, 0);
    private GoogleMap googleMap;
    Marker TP;

    //List<String> su = new ArrayList<String>();
    //ArrayAdapter<String> adpr;
    //String[] fsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);



        Intent i = getIntent();
        text = i.getStringExtra("value1");

        initialise();
        Parse.initialize(this, "elYCvSEyym245e2K6I65GZXth5FpOOvphwQTmGj9", "cObfrsmYAsrzeIx5iAxgJ0A2DDTAmZHkABLgbQfu");



        new parseData().execute();

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

            //TP = googleMap.addMarker(new MarkerOptions().
            //        position(TutorialsPoint));

        } catch (Exception e) {
            Log.e(TAG, "error: " + e);
        }

      //  List<String> su = new ArrayList<String>();

        /*adpr = new ArrayAdapter<String>(this, R.layout.activity_trip_details, R.id.list_item, su);

        ListView list = (ListView) findViewById(R.id.listg);
        list.setAdapter(adpr);*/

    }

    private void initialise() {
        lu = (TextView) findViewById(R.id.lu);
        des = (TextView) findViewById(R.id.des);
        status = (TextView) findViewById(R.id.status);
        update = (TextView) findViewById(R.id.up);

    }

    private void loadAndUpdate() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Available_Rides");
        query.whereEqualTo("TripID", text);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {

                if (e == null) {

                  /*  if (object.get(0).getBoolean("Arrived")){

                        Log.d(TAG, "Arrividvvjdbvjdbv");
                    }

                    else if (object.get(0).getBoolean("Departed")){
                        //create notification
                        Log.d(TAG, "DDDDDDArrividvvjdbvjdbv");
                    }

*/
                        if (object.get(0).getString("status") != null) {

                            status.setText("Not Yet Departed");
                        } else {

                            String stat = object.get(0).getString("Status");

                            Log.d(TAG, "stat: " + stat);
                            status.setText(stat);
                        }


                    String origin = object.get(0).getString("Origin");
                    String destination = object.get(0).getString("Destination");
                    String dest = String.format("%s - %s", origin, destination);

                    des.setText(dest);


                   /* Geocoder geocoder = new Geocoder(TripDetails.this, Locale.getDefault());
                    Geocoder geocoder1 = new Geocoder(TripDetails.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(origin, 1);
                        List<Address> addresses1 = geocoder1.getFromLocationName(dest, 1);

                        Log.d(TAG, "or " + addresses + " " + addresses1);

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }*/

                    Log.d(TAG, "origin: " + origin);



                    List status = object.get(0).getList("tripStatusUpdates");
                    List statusTimes = object.get(0).getList("tripStatusUpdatesTimes");




                    Log.d(TAG, "s   " + status);
                    Log.d(TAG, "t    " + statusTimes);

                    if (status != null) {



                        for (int i = 0; i < status.size(); i++) {

                            String time = statusTimes.get(0).toString().substring(11, 16);
                            String item = String.format("%s @ %s", status.get(i).toString(), time);

                            update.setText(item);

                            Log.d(TAG, "i   " + item);

                            //su.add(item);
                            //su.add(i, item);

                            //set tripUpdates = " ";
                        //update text view with statuses and statustimes
                    }


                    }

                    if (object.get(0).getParseGeoPoint("location") != null) {

                        ParseGeoPoint geoPoint = object.get(0).getParseGeoPoint("location");
                        TutorialsPoint = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                        TP = googleMap.addMarker(new MarkerOptions().
                                       position(TutorialsPoint));

                        TP.setPosition(TutorialsPoint);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                TutorialsPoint, 15);
                        googleMap.animateCamera(cameraUpdate);
                        progressDialog.dismiss();
                    }

                    //update map view with geoPoint

                    Date date = object.get(0).getDate("DepartTime");
                    Log.d(TAG, "d: " +  date.getTime());
                    //update text with formatted date

                    Date updateDate = object.get(0).getDate("LastUpdated");

                    String upd = updateDate.toString().substring(11,16);

                    java.text.DateFormat dateFormat =
                            android.text.format.DateFormat.getDateFormat(getApplicationContext());
                    lu.setText( String.format("Last Updated: " + dateFormat.format(updateDate) + " @ %s", upd));
                    //String.format("Last Updated: " + dateFormat.format(updateDate) + "@ %s", updateDate.toString().indexOf(11, 16));

                    Log.d(TAG, "dd: " + updateDate);
                    //update text with formatted date


                } else {

                    Log.d(TAG, "Wtf");
                    // something went wrong

                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_details, menu);
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

    public class parseData extends AsyncTask<Void, Integer, Void>{


        //Before running code in separate thread
        @Override
        protected void onPreExecute()

        {


            //Create a new progress dialog
            progressDialog = new ProgressDialog(TripDetails.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("Loading...");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Loading trip location, please wait...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(false);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            //The maximum number of items is 100
            progressDialog.setMax(100);
            //Set the current progress to zero
            progressDialog.setProgress(0);
            //Display the progress dialog
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {


            loadAndUpdate();
            try
            {
                //Get the current thread's token


                synchronized (this)
                {
                    //Initialize an integer (that will act as a counter) to zero
                    int counter = 0;
                    //While the counter is smaller than four
                    while(counter <= 4)
                    {
                        //Wait 850 milliseconds
                        this.wait(1200);
                        //Increment the counter
                        counter++;
                        //Set the current progress.
                        //This value is going to be passed to the onProgressUpdate() method.
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)


        {
            //close the progress dialog
           // progressDialog.dismiss();
            //initialize the View
            //setContentView(R.layout.activity_trip_details);
        }
    }

}
