package alarma.example.com.alarmaph;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class call_others_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView cothers;

    ArrayList<String> oname = new ArrayList<>();
    ArrayList<Integer> oimage= new ArrayList<>();
    ArrayList<String> a_cont = new ArrayList<>();
    /*String[] oname = {
            "Carlo Sbaeron",
            "Joshua Del Mar",
            "Christine Carubio"
    };
    Integer[] oimage = {
            R.drawable.ic_call_black_48dp,
            R.drawable.ic_call_black_48dp,
            R.drawable.ic_call_black_48dp
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_others_activity);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference("hotlines/crime");

        mDatabase.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String uid= (String) dataSnapshot.getKey();
                String dispName = (String) dataSnapshot.child("agencyName").getValue(String.class);
                String landLine = (String) dataSnapshot.child("agencyLandline").getValue(String.class);
                String cp = (String) dataSnapshot.child("agencyCellPhone").getValue(String.class);
                oname.add(dispName);
                oimage.add(R.drawable.ic_call_black_48dp);
                a_cont.add(landLine);
                a_cont.add(cp);

                call_policelist adapter = new call_policelist(call_others_activity.this, oname, oimage);
                cothers = (ListView) findViewById(R.id.list_call_police);
                cothers.setAdapter(adapter);

                cothers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //Toast.makeText(call_police_activity.this,""+a_agency[+position], Toast.LENGTH_SHORT).show();
                        callPhone(a_cont.get(+position));
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

        call_otherlist adapter = new call_otherlist(call_others_activity.this, oname, oimage);
        cothers = (ListView) findViewById(R.id.list_call_others);
        cothers.setAdapter(adapter);
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
    public void callPhone(String contact){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+contact));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        startActivity(callIntent);
    }
}
