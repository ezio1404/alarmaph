package alarma.example.com.alarmaph;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class forecastactivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    ListView dweather;

    String[] days = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    String[] weath = {
            "SUNNY",
            "STORMY",
            "RAINY",
            "CLOUDY",
            "HOT",
            "DRY",
            "SUNNY"
    };

    String[] degree = {
            "30°",
            "40°",
            "35°",
            "76°",
            "32°",
            "68°",
            "5°"
    };


    Integer[] imageId = {
            R.drawable.weather,
            R.drawable.weather,
            R.drawable.weather,
            R.drawable.weather,
            R.drawable.weather,
            R.drawable.weather,
            R.drawable.weather

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecastactivity);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get ListView object from xml
        /*dweather = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] days = new String[] { "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
        };


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, days);


        // Assign adapter to ListView
        dweather.setAdapter(adapter);


        // ListView Item Click Listener
        dweather.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) dweather.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_SHORT)
                        .show();

            }

        });*/

        Forecastlist adapter = new Forecastlist(forecastactivity.this, days, weath, degree,imageId);
        dweather=(ListView)findViewById(R.id.list);
        dweather.setAdapter(adapter);

        dweather.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(forecastactivity.this,""+weath[+ position]+" this coming "+days[position], Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homeactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_emergency) {

            Intent imer = new Intent(this,emergencyactivity.class);
            startActivity(imer);

        } else if (id == R.id.nav_guardian) {

            Intent iguard = new Intent(this,guardianactivity.class);
            startActivity(iguard);


        } else if (id == R.id.nav_manual) {

            Intent imanual = new Intent(this,manualactivity.class);
            startActivity(imanual);

        } else if (id == R.id.nav_help) {

            Intent ihelp = new Intent(this,helpactivity.class);
            startActivity(ihelp);

        } else if (id == R.id.nav_setting) {
            Intent isetting = new Intent(this, settingactivity.class);
            startActivity(isetting);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
