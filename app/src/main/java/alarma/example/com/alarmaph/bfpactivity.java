package alarma.example.com.alarmaph;

import android.app.Dialog;
import android.content.Context;
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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class bfpactivity extends  AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    Button forecast;
    Button adminAction;
    ListView plist;


    ArrayList<String> ptitle = new ArrayList<String>();
    ArrayList<String> desc= new ArrayList<String>();
    ArrayList<String> postId =  new ArrayList<>();

    final Context context = this;

    RadioButton r1,r2,r3,r4,r5;
    EditText rate;
    String currentUser;
    String visitedAdmin;
    String adminName;
    String adminPURL;
    Button acceptRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfpactivity);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Bundle bundle = getIntent().getExtras();
        final String key = bundle.getString("key").toString();
        final String dispName = bundle.getString("disp").toString();
        TextView agencyNameText = (TextView) findViewById(R.id.textView4);
        agencyNameText.setText(dispName);

        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference("userAccounts/"+key+"/posts");

        mDatabase.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String uid= (String) dataSnapshot.child("UID").getValue(String.class);
                if(uid.equals(key.toString())) {
                    String key = (String) dataSnapshot.getKey();
                    String title = dataSnapshot.child("postAlias").getValue(String.class);
                    String descs = dataSnapshot.child("postDesc").getValue(String.class);
                    String postLogo = (String) dataSnapshot.child("postLogo").getValue(String.class);

                    adminPURL = postLogo;
                    adminName = title;

                    Collections.reverse(ptitle);
                    ptitle.add(title);
                    Collections.reverse(ptitle);

                    Collections.reverse(desc);
                    desc.add(descs);
                    Collections.reverse(desc);

                    Collections.reverse(postId);
                    postId.add(key);
                    Collections.reverse(postId);

                    postlist adapter = new postlist(bfpactivity.this, ptitle, desc,postLogo,postId,uid);
                    plist=(ListView)findViewById(R.id.listbfps);
                    plist.setAdapter(adapter);
                }


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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = user.getUid();
        visitedAdmin = key;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("mobileUsers/"+currentUser+"/follows");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                showMsg("Loading Admin Data. Please wait..");
                if (snapshot.hasChild(key)) {
                    Button follow = (Button) findViewById(R.id.followButton);
                    follow.setText("Unfollow");
                    showMsg("Loading complete.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showMsg("Error Message: "+databaseError);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        forecast = (Button) findViewById(R.id.buttonforecast);
        forecast.setOnClickListener(this);
        adminAction = (Button) findViewById(R.id.rateButton);
        adminAction.setOnClickListener(this);
        adminAction = (Button) findViewById(R.id.followButton);
        adminAction.setOnClickListener(this);
        adminAction = (Button) findViewById(R.id.callButton);
        adminAction.setOnClickListener(this);
        adminAction = (Button) findViewById(R.id.eventButton);
        adminAction.setOnClickListener(this);


    }

    public void showMsg(String key) {
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
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
        }else if(v.getId() == R.id.followButton){
            Button thisButton = (Button) findViewById(R.id.followButton);
            String action = (String) thisButton.getText();

            if(action.equals("Unfollow")){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid);
                ref.child("follows").child(visitedAdmin).removeValue();
                Toast.makeText(bfpactivity.this,"Reloading followed data",Toast.LENGTH_SHORT).show();
                thisButton.setText("Follow");
            }else{
                //showMsg("follow");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid);

                follows followThis = new follows(adminPURL,adminName);

                ref.child("follows").child(visitedAdmin).setValue(followThis);
                thisButton.setText("Unfollow");
            }
        }else if(v.getId() == R.id.rateButton){
            final Dialog dialog2 = new Dialog(context);
            dialog2.setContentView(R.layout.zdialog_rate);
            // dialog2.setTitle("Add Location");

            r1 = (RadioButton) dialog2.findViewById(R.id.radioButton);
            r2 = (RadioButton) dialog2.findViewById(R.id.radioButton4);
            r3 = (RadioButton) dialog2.findViewById(R.id.radioButton7);
            r4 = (RadioButton) dialog2.findViewById(R.id.radioButton5);
            r5 = (RadioButton) dialog2.findViewById(R.id.radioButton6);

            rate = (EditText) dialog2.findViewById(R.id.editText11);
            final int[] starRate = {0};

            acceptRate = (Button) dialog2.findViewById(R.id.button5);

            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            lp1.copyFrom(dialog2.getWindow().getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp1.gravity = Gravity.CENTER;
            dialog2.getWindow().setAttributes(lp1);

            dialog2.show();
            acceptRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String commentRate = rate.getText().toString();
                    if (TextUtils.isEmpty(commentRate)) {
                        Toast.makeText(getApplicationContext(), "Enter comment!", Toast.LENGTH_SHORT).show();

                    }

                    if(r1.isChecked()){
                        starRate[0] = 1;
                    }else if(r2.isChecked()){
                        starRate[0] = 2;
                    }else if(r3.isChecked()){
                        starRate[0] = 3;
                    }else if(r4.isChecked()){
                        starRate[0] = 4;
                    }else if(r5.isChecked()){
                        starRate[0] = 5;
                    }

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
                    FirebaseUser user = mAuth.getCurrentUser();
                    final String uid = user.getUid();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("mobileUsers/");

                    final int finalStarRate = starRate[0];
                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String key = dataSnapshot.getKey();
                            if(key.equals(uid)){
                                String fname = dataSnapshot.child("firstName").getValue(String.class);
                                String lname = dataSnapshot.child("lastName").getValue(String.class);
                                String PURL = dataSnapshot.child("PURL").getValue(String.class);

                                String fullName = fname+" "+lname;
                                postData passValue = new postData(commentRate,PURL,fullName, finalStarRate);

                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference thisRef = db.getReference("userAccounts/"+visitedAdmin+"/feedbacks/");
                                thisRef.child(uid).setValue(passValue);
                                Toast.makeText(bfpactivity.this,"Thank you for your feedback",Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
                            }
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
                }
            });
        }else if(v.getId() == R.id.callButton){

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.zalarma_calls);


            final WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            lp1.copyFrom(dialog.getWindow().getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp1.gravity = Gravity.CENTER;

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("userAccounts/"+visitedAdmin);

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key = dataSnapshot.getKey();

                    if(key.equals("contact")){
                        dialog.getWindow().setAttributes(lp1);
                        dialog.setTitle("HOTLINES");
                        final String ll =  dataSnapshot.child("landlineCont").getValue(String.class);
                        final String cp =  dataSnapshot.child("mobileCont").getValue(String.class);
                        TextView alarm = (TextView) dialog.findViewById(R.id.c_alarm);
                        alarm.setText(ll);
                        TextView alarm2 = (TextView) dialog.findViewById(R.id.c_alarm_2);
                        alarm2.setText(cp);
                        alarm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                callPhone(ll);

                            }
                        });

                        alarm2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callPhone(cp);
                            }
                        });
                    }
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



            dialog.setCancelable(true);
            dialog.show();


        }else if(v.getId() == R.id.eventButton){
            Intent showevent  = new Intent(this, events_activity.class);
            showevent.putExtra("adminUID",visitedAdmin);
            startActivity(showevent);

        }

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
    class postData {

        public String fbDesc;
        public String fbName;
        public String fbPURL;
        public int fbStars;
        public postData(){

        }

        public postData(String fbDesc, String fbPURL, String fbName, int fbStars){
            this.fbPURL = fbPURL;
            this.fbName = fbName;
            this.fbDesc = fbDesc;
            this.fbStars = fbStars;
        }

    }

}
class follows{
    public String PURL;
    public String agencyName;
    public follows(){

    }
    public follows(String PURL, String agencyName){
        this.PURL = PURL;
        this.agencyName = agencyName;
    }
}