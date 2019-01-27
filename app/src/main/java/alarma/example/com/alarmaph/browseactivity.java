package alarma.example.com.alarmaph;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


public class browseactivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    Button forecast;

    private ViewPager mViewPager;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseactivity);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final ProgressDialog loader = new ProgressDialog(this);
        loader.setTitle("Loading");
        loader.setMessage("Your data are being prepared, please wait.");
        loader.show();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        final DatabaseReference mDatabase;
        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers");

        mDatabase.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String commentKey = (String)dataSnapshot.getKey();
                if(commentKey.equals(uid)){
                    String fname = dataSnapshot.child("firstName").getValue(String.class);
                    String lname = dataSnapshot.child("lastName").getValue(String.class);
                    String email = dataSnapshot.child("userEmail").getValue(String.class);

                    String myUsername="PURL";

                    TextView fullName = (TextView) findViewById(R.id.nameView);
                    TextView eMail = (TextView) findViewById(R.id.emailView);
                    CircleImageView pp= (CircleImageView) findViewById(R.id.profile_image);

                    if (mDatabase.child(myUsername).getRoot() != null) {
                        String purl = dataSnapshot.child("PURL").getValue(String.class);
                        try {
                            Bitmap thisImage = new sync_task().execute(purl).get();
                            pp.setImageBitmap(thisImage);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }


                    fullName.setText(fname+" "+lname);
                    eMail.setText(email);
                    loader.dismiss();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //goTo();
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

        DatabaseReference mDatabases;
        mDatabases = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid+"/follows");

        mDatabases.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String commentKey = dataSnapshot.getKey();
                String agencyName = dataSnapshot.child("agencyName").getValue(String.class);
                String logoURL = dataSnapshot.child("PURL").getValue(String.class).toString();

                MenuItem follows = navigationView.getMenu().getItem(0);
                SubMenu subMenu = follows.getSubMenu();
                try {
                    Bitmap thisBitmap = new sync_task().execute(logoURL).get();

                    subMenu.add(agencyName).setIcon(new BitmapDrawable(thisBitmap)).setTitleCondensed(commentKey).setTitle(agencyName);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //goTo();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //goTo();
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



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }
    public void goTo(){
        Intent iforecast = new Intent(this, browseactivity.class);
        startActivity(iforecast);
    }
    public void showMsg(String key) {
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
    }
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            boolean emailVerified = user.isEmailVerified();
            startActivity(intent);
        }
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

        getMenuInflater().inflate(R.menu.menu_browseactivity, menu);

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
        String agencyname = item.getTitleCondensed().toString();
        String dispName = item.getTitle().toString();

        if (id == R.id.nav_emergency) {

            Intent imer = new Intent(this,emergencyactivity.class);
            startActivity(imer);


        }
        else if(id == R.id.nav_call_alarma_side) {
            Intent caalar = new Intent(this,call_alarma_activity.class);
            startActivity(caalar);


        }else if(id == R.id.nav_call_medical_side) {
            Intent camed = new Intent(this,call_medical_activity.class);
            startActivity(camed);


        } else if(id == R.id.nav_call_fire_side) {
            Intent cafir = new Intent(this,call_fire_activity.class);
            startActivity(cafir);


        } else if(id == R.id.nav_call_police_side) {
            Intent capol = new Intent(this,call_police_activity.class);
            startActivity(capol);


        } /*else if(id  == R.id.nav_notifications){

            Intent notifi = new Intent(this, notificationsactivity.class);
            startActivity(notifi);

        }*/ else if (id == R.id.nav_guardian) {

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

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent isetting = new Intent(this, MainActivity.class);
            startActivity(isetting);

        } else {
            if(agencyname.equals("CitizenReport")){
                Intent visit = new Intent(this,citizen_activity.class);
                visit.putExtra("key",agencyname);
                visit.putExtra("disp",dispName);
                startActivity(visit);
            }else{
                Intent visit = new Intent(this,bfpactivity.class);
                visit.putExtra("key",agencyname);
                visit.putExtra("disp",dispName);
                startActivity(visit);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.buttonforecast){
            Toast.makeText(this, "NAAY BAGYO BROWSE", Toast.LENGTH_SHORT).show();
            Intent iforecast = new Intent(this, forecastactivity.class);
            startActivity(iforecast);
        }

    }
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_browseactivity, container, false);
            ImageView textView = (ImageView) rootView.findViewById(R.id.guideqwe);

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                textView.setImageResource(R.drawable.draginfo);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                textView.setImageResource(R.drawable.info2);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                textView.setImageResource(R.drawable.info1);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 4){
                textView.setImageResource(R.drawable.info3);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 5){
                textView.setImageResource(R.drawable.info4);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 6){
                textView.setImageResource(R.drawable.info5);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 7){
                textView.setImageResource(R.drawable.info6);
            }

            return rootView;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return ""+position;
        }
    }
}