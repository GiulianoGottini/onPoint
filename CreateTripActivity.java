//
//  onPoint
//
//  Created by Giuliano Gottini on 2014/09/10.
//  Copyright (c) 2014 Giuliano Gottini. All rights reserved.
//

package gottini.giuliano.onpoint;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateTripActivity extends Activity implements View.OnClickListener {

    EditText from, to;
    DatePicker date;
    TimePicker time;
    Button but;
    Date date1;
    String f, t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        from = (EditText) findViewById(R.id.editText7);
        to = (EditText) findViewById(R.id.editText8);
        date = (DatePicker) findViewById(R.id.datePicker);
        time = (TimePicker) findViewById(R.id.timePicker);



        but = (Button) findViewById(R.id.button);
        but.setOnClickListener(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_trip, menu);
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

        f = from.getText().toString();
        t = to.getText().toString();

        int day  = date.getDayOfMonth();
        int month= date.getMonth();
        int year = date.getYear();

        int hourOfDay = time.getCurrentHour();
        int minute = time.getCurrentMinute();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date1 = cal.getTime();

        Log.d("CreateTripActivity" , "date: " + date1);




        ParseQuery<ParseObject> query = ParseQuery.getQuery("TripStats");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.getInBackground("IRulG9AM8k", new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {

                        String tripID;
                        String string = String.format("%s%s", f.charAt(0), t.charAt(0));
                        object.increment(string);
                        int Tripnum = object.getInt(string);
                        tripID = String.format("%s%d", string, Tripnum);

                        ParseObject trip = new ParseObject("Available_Rides");
                        trip.put("Origin", f);
                        trip.put("Destination", t);
                        trip.put("DepartTime", date1);
                        trip.put("TripID", tripID);
                        trip.saveInBackground();
                        object.saveInBackground();

                    } else {
                        Log.d("score", "Error: " + e.getMessage());




                    }
                }

        });



        Toast.makeText(this, "Trip Created Successfully", Toast.LENGTH_SHORT).show();

    }
}
