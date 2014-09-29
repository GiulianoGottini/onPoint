//
//  WelcomeActivity.java
//  onPoint
//
//  Created by Giuliano Gottini on 2014/09/10.
//  Copyright (c) 2014 Giuliano Gottini. All rights reserved.
//

package gottini.giuliano.onpoint;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.List;


public class WelcomeActivity extends Activity implements View.OnClickListener {

    public final String TAG = "WelcomeActivity";

    ImageButton updates;
    ImageButton admin;
    String value = null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_welcome);
        Parse.initialize(this, "elYCvSEyym245e2K6I65GZXth5FpOOvphwQTmGj9", "cObfrsmYAsrzeIx5iAxgJ0A2DDTAmZHkABLgbQfu");
        PushService.setDefaultPushCallback(this, WelcomeActivity.class);
        ParseAnalytics.trackAppOpened(getIntent());
        initialise();

    }

    private void initialise() {
        updates = (ImageButton) findViewById(R.id.imageButton);
        admin = (ImageButton) findViewById(R.id.imageButton2);
        updates.setOnClickListener(this);
        admin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton:

                getInputDialog();


                break;
            case R.id.imageButton2:

                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);


                break;
        }
    }

    private void getInputDialog() {



        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Find Trip");
        alert.setMessage("Enter the tripID: ");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value = input.getText().toString().toUpperCase();
                new load().execute();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Available_Rides");
                query.whereEqualTo("TripID", value);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {



                        if (e == null){


                            if(parseObjects.size() > 0){



                                Intent tripDetails = new Intent(WelcomeActivity.this, TripDetails.class);
                                tripDetails.putExtra("value1", value);

                                startActivity(tripDetails);
                                progressDialog.dismiss();


                            }

                            //value = null;

                        }

                       /* if (value != null) {
                            Intent tripDetails = new Intent(WelcomeActivity.this, TripDetails.class);
                            tripDetails.putExtra("value1", value);
                            startActivity(tripDetails);
                        }*/

                    }
                });




               /* //if (value.equals(""))
                    value = null;

                if (value != null){
                    Intent tripDetails = new Intent(WelcomeActivity.this, TripDetails.class);
                    tripDetails.putExtra("value1", value);
                    startActivity(tripDetails);
                }
                Log.d(TAG, "value " + value);
                */
            }


        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value = null;
                // Canceled.
            }
        });

        alert.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
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

    public class load extends AsyncTask<Void, Integer, Void> {


        //Before running code in separate thread
        @Override
        protected void onPreExecute()

        {


            //Create a new progress dialog
            progressDialog = new ProgressDialog(WelcomeActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("onPoint");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Validating TripID...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(true);
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


    }



}
