package alarma.example.com.alarmaph;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class events_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView elist;

    ArrayList<String> etitle = new ArrayList<>();
    ArrayList<String> ewhat = new ArrayList<>();
    ArrayList<String> ewhen = new ArrayList<>();
    ArrayList<String> ewhere = new ArrayList<>();
    ArrayList<String> ewho = new ArrayList<>();
    ArrayList<String> edesc = new ArrayList<>();


    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_activity);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Bundle bundle = getIntent().getExtras();
        final String uid = bundle.getString("adminUID");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("userAccounts/"+uid+"/events");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                    String details = dataSnapshot.child("eventDetails").getValue(String.class);
                    String title = dataSnapshot.child("eventTitle").getValue(String.class);
                    String what = dataSnapshot.child("eventWhat").getValue(String.class);
                    String when = dataSnapshot.child("eventWhen").getValue(String.class);
                    String where = dataSnapshot.child("eventWhere").getValue(String.class);
                    String who  = dataSnapshot.child("eventWho").getValue(String.class);

                    Collections.reverse(etitle);
                    etitle.add(title);
                    Collections.reverse(etitle);

                    Collections.reverse(ewhat);
                    ewhat.add(what);
                    Collections.reverse(ewhat);

                    Collections.reverse(ewhen);
                    ewhen.add(when);
                    Collections.reverse(ewhen);

                    Collections.reverse(ewhere);
                    ewhere.add(where);
                    Collections.reverse(ewhere);

                    Collections.reverse(ewho);
                    ewho.add(who);
                    Collections.reverse(ewho);

                    Collections.reverse(edesc);
                    edesc.add(details);
                    Collections.reverse(edesc);

                    events_list adapter = new events_list(events_activity.this, etitle,ewhat,ewhen,ewhere,ewho, edesc);
                    elist=(ListView)findViewById(R.id.listevents);
                    elist.setAdapter(adapter);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
