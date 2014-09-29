//
//  onPoint
//
//  Created by Giuliano Gottini on 2014/09/10.
//  Copyright (c) 2014 Giuliano Gottini. All rights reserved.
//

package gottini.giuliano.onpoint;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class CreatedTripsActivity extends ListActivity implements AdapterView.OnItemClickListener {

    public String[] pArray;
    String[] oArray;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new load().execute();
        //setContentView(R.layout.activity_created_trips);



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Available_Rides");
        if (ParseUser.getCurrentUser().getString("OrganisationID").length()>1){
            query.whereEqualTo("Organisation", ParseUser.getCurrentUser().getString("OrganisationID"));
            query.orderByAscending("TripID");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                    String[] myKeys = new String[parseObjects.size()];
                    pArray = new String[parseObjects.size()];

                    Log.d("CreatedTripsActivity", "s: " + parseObjects.size());

                    if(e == null){

                        Log.d("CreatedTripsActivity", "he: " + parseObjects + "  ");
                        for (int i = 0; i < parseObjects.size(); i++) {



                            myKeys[i] = parseObjects.get(i).getString("TripID").toString();
                            pArray[i] = parseObjects.get(i).getString("TripID").toString();
                            //oArray[i] = parseObjects.get(i).getObjectId();

                            Log.d("CreatedTripsActivity", "id " + myKeys[i]);






                        }
                        Log.d("CreatedTripsActivity", "k: " + myKeys );

                        //ListView listview = (ListView) findViewById(R.id.list3);
                        //listview.setOnItemClickListener(CreatedTripsActivity.this);

                        setListAdapter(new ArrayAdapter<String>(CreatedTripsActivity.this, android.R.layout.simple_list_item_1, myKeys));
                        ListView lv = getListView();
                        lv.setOnItemClickListener(CreatedTripsActivity.this);



                                progressDialog.dismiss();
                    }
                }
            });

        }

        //final String[] myKeys = new String[count()];



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.created_trips, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, ListItemDetail.class);
        intent.putExtra("position", position);
        intent.putExtra("array", pArray);
        //intent.putExtra("orray", oArray);
        //intent.putExtra("objID", oArray[position]);

        // Or / And
        intent.putExtra("id", id);

        startActivity(intent);
    }


    public class load extends AsyncTask<Void, Integer, Void> {


        //Before running code in separate thread
        @Override
        protected void onPreExecute()

        {


            //Create a new progress dialog
            progressDialog = new ProgressDialog(CreatedTripsActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("onPoint");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Loading Created Trips...");
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
