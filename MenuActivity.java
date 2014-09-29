//
//  onPoint
//
//  Created by Giuliano Gottini on 2014/09/10.
//  Copyright (c) 2014 Giuliano Gottini. All rights reserved.
//

package gottini.giuliano.onpoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MenuActivity extends Activity implements View.OnClickListener{

    Button one, two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        one = (Button) findViewById(R.id.button);
        one.setOnClickListener(this);
        two = (Button) findViewById(R.id.button2);
        two.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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

        Intent create = new Intent(this, CreateTripActivity.class);
        Intent created = new Intent(this, CreatedTripsActivity.class);


        switch (v.getId()){

            case R.id.button:
                startActivity(create);

                break;
            case R.id.button2:
                startActivity(created);

                break;


        }
    }
}
