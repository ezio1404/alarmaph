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

import java.util.ArrayList;

public class manualactivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    Button forecast;

    ListView lmanual;

    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> author= new ArrayList<>();
    ArrayList<Integer> imageId = new ArrayList<>();
    ArrayList<String> link = new ArrayList<>();
    /*
    String[] title = {
            "Learning from Disasters"
            "Designing Resilience: Preparing for Extreme Events",
            "Who Survives When Disaster Strikes ",
            "Disasters by Design",
            "Introduction to Emergency Management",
            "Public Health Management of Disasters",
            "The Medical Detectives",
            "Designing Resilience: Preparing for Extreme Events",
            "Who Survives When Disaster Strikes ",
            "Disasters by Design",
            "Introduction to Emergency Management",
            "Public Health Management of Disasters"
    };

    String[] author = {
            "Brian Toft"
            "Louise K. Comfort",
            "Amanda Ripley",
            "Dennis S. Mileti",
            "George D. Haddow",
            "Linda Y. Landesman",
            "Berton Roueché",
            "Amanda Ripley",
            "Dennis S. Mileti",
            "George D. Haddow",
            "Linda Y. Landesman",
            "Berton Roueché"


    };

    Integer[] imageId = {
            R.drawable.manual
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual,
            R.drawable.manual

    };
*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualactivity);

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
        mDatabase = FirebaseDatabase.getInstance().getReference("manuals");
        mDatabase.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String commentKey = (String)dataSnapshot.getKey();
                String name = dataSnapshot.child("name").getValue(String.class);
                String authors = dataSnapshot.child("author").getValue(String.class);
                String links = dataSnapshot.child("link").getValue(String.class);
                title.add(name);
                author.add(authors);
                link.add(links);
                imageId.add(R.drawable.manual);

                Manuallist adapter = new Manuallist(manualactivity.this, title, author, imageId,link);
                lmanual=(ListView)findViewById(R.id.lmanual);
                lmanual.setAdapter(adapter);

                lmanual.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(manualactivity.this,""+ title.get(+position) +" by "+ author.get(position), Toast.LENGTH_SHORT).show();
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

        /*if (id == R.id.nav_alarma) {

            Intent ihome = new Intent(this, homeactivity.class);
            startActivity(ihome);


        } else if (id == R.id.nav_bfp) {

            Intent ibfp = new Intent(this,bfpactivity.class);
            startActivity(ibfp);

        }else */if (id == R.id.nav_emergency) {

            Intent imer = new Intent(this,emergencyactivity.class);
            startActivity(imer);

        } else if (id == R.id.nav_guardian) {

            Intent iguard = new Intent(this,guardianactivity.class);
            startActivity(iguard);


        } else if (id == R.id.nav_manual) {


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
            Toast.makeText(this, "NAAY BAGYO MANUAL", Toast.LENGTH_SHORT).show();
            Intent iforecast = new Intent(this, forecastactivity.class);
            startActivity(iforecast);
        }

    }
}

