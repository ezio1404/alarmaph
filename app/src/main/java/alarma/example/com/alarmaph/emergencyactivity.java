package alarma.example.com.alarmaph;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class emergencyactivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    Button forecast;
    ListView emer;
    ArrayList<String> agency = new ArrayList<>();
    ArrayList<String> what = new ArrayList<>();
    ArrayList<String> where = new ArrayList<>();
    ArrayList<String> when = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyactivity);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseReference mDatabase;

        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference("alarma/reports");
        mDatabase.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = (String)dataSnapshot.getKey();
                double lat = dataSnapshot.child("lat").getValue(double.class);
                double lng = dataSnapshot.child("lng").getValue(double.class);
                String type = dataSnapshot.child("type").getValue(String.class);
                String reporter = dataSnapshot.child("uid").getValue(String.class);
                String date = dataSnapshot.child("date").getValue(String.class);
                String time = dataSnapshot.child("time").getValue(String.class);
                String description = dataSnapshot.child("desc").getValue(String.class);
                String formAdd = getFormattedAddress(lat,lng);


                imageId.add(R.drawable.emergency_icon);

                Collections.reverse(agency);
                agency.add(type+" Report");
                Collections.reverse(agency);

                Collections.reverse(what);
                what.add(type+" Incident");
                Collections.reverse(what);

                Collections.reverse(where);
                where.add(formAdd);
                Collections.reverse(where);

                Collections.reverse(when);
                when.add(date+" "+time);
                Collections.reverse(when);

                Collections.reverse(desc);
                desc.add(description);
                Collections.reverse(desc);

                emergencylist adapter = new emergencylist(emergencyactivity.this, agency, what, when , where, desc, imageId, reporterId, reportId);
                emer=(ListView)findViewById(R.id.listemer);
                emer.setAdapter(adapter);

                emer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //Toast.makeText(emergencyactivity.this,""+agency[+ position], Toast.LENGTH_SHORT).show();

                    }
                });
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        forecast = (Button) findViewById(R.id.buttonforecast);
        forecast.setOnClickListener(this);

    }
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> reporterId = new ArrayList<>();
    ArrayList<String> reportId = new ArrayList<>();

    ArrayList<Integer> imageId = new ArrayList<>();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private String getFormattedAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getAddressLine(0);

            return address;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
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


        } else if (id == R.id.nav_guardian) {

            Intent iguard = new Intent(this,guardianactivity.class);
            startActivity(iguard);


        } else if (id == R.id.nav_manual) {
            Intent imanual = new Intent(this,manualactivity.class);
            startActivity(imanual);

        } else if (id == R.id.nav_help) {

            Intent ihelp = new Intent(this,helpactivity.class);
            startActivity(ihelp);;

        } else if (id == R.id.nav_setting) {
            Intent isetting = new Intent(this, settingactivity.class);
            startActivity(isetting);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.buttonforecast){
            Toast.makeText(this, "NAAY BAGYO EMERGENCY", Toast.LENGTH_SHORT).show();
            Intent iforecast = new Intent(this, forecastactivity.class);
            startActivity(iforecast);
        }

    }
}
