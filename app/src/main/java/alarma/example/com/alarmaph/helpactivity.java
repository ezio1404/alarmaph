package alarma.example.com.alarmaph;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class helpactivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button forecast, crime, medical, fire, help;

    private GoogleApiClient mGoogleApiClient;

    String[] contLandlineFire = new String[6];
    String[] contCpFire = new String[6];
    String[] agencyNameFire = new String[6];
    String[] regFire = new String[6];
    String[] statusFire = new String[6];
    double[] distBetFire = new double[6];

    String[] contLandlineCrime = new String[6];
    String[] contCpCrime = new String[6];
    String[] agencyNameCrime = new String[6];
    String[] regCrime = new String[6];
    String[] statusCrime = new String[6];
    double[] distBetCrime = new double[6];

    String[] contLandlineMed = new String[6];
    String[] contCpMed = new String[6];
    String[] agencyNameMed = new String[6];
    String[] regMed = new String[6];
    String[] statusMed = new String[6];
    double[] distBetMed = new double[6];

    final Context context = this;

    LinearLayout medical1, medical2, medical3, medical4, medical5, medical6;
    LinearLayout fire1, fire2, fire3, fire4, fire5, fire6;
    LinearLayout police1, police2, police3, police4, police5, police6;

    TextView policeRanked1, policeRanked2, policeRanked3, policeRanked4, policeRanked5, policeRanked6;
    TextView fireRanked1, fireRanked2, fireRanked3, fireRanked4, fireRanked5, fireRanked6;
    TextView medicalRanked1, medicalRanked2, medicalRanked3, medicalRanked4, medicalRanked5, medicalRanked6;

    TextView c_number;

    String formAddress;
    String repContNumber;
    String reportPURL;
    String reportName;
    static String UID;
    String nearestFA;

    String myName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpactivity);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();

        final DatabaseReference mDatabase;
        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers");

        mDatabase.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key = (String)dataSnapshot.getKey();
                if(key.equals(UID)){
                    String fname = dataSnapshot.child("firstName").getValue(String.class);
                    String lname = dataSnapshot.child("lastName").getValue(String.class);
                    String PURL = dataSnapshot.child("PURL").getValue(String.class);
                    String cnum = dataSnapshot.child("contNum").getValue(String.class);
                    repContNumber = cnum;
                    reportPURL = PURL;
                    reportName = fname+" "+lname;
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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

        getAllHotline("crime");
        getAllHotline("fire");
        getAllHotline("medical");
        getNearestFirstAider();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        forecast = (Button) findViewById(R.id.buttonforecast);
        forecast.setOnClickListener(this);

        crime = (Button) findViewById(R.id.policeHelp);
        crime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog0 = new Dialog(context);
                dialog0.setTitle("POLICE  ASSISTANCE");
                dialog0.setContentView(R.layout.zdialog_police);


                WindowManager.LayoutParams lp0 = new WindowManager.LayoutParams();
                lp0.copyFrom(dialog0.getWindow().getAttributes());
                lp0.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp0.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp0.gravity = Gravity.CENTER;

                dialog0.getWindow().setAttributes(lp0);

                police1 = (LinearLayout) dialog0.findViewById(R.id.police1);
                police2 = (LinearLayout) dialog0.findViewById(R.id.police2);
                police3 = (LinearLayout) dialog0.findViewById(R.id.police3);
                police4 = (LinearLayout) dialog0.findViewById(R.id.police4);
                police5 = (LinearLayout) dialog0.findViewById(R.id.police5);
                police6 = (LinearLayout) dialog0.findViewById(R.id.police6);

                policeRanked1 = (TextView) dialog0.findViewById(R.id.policeRank1);
                policeRanked2 = (TextView) dialog0.findViewById(R.id.policeRank2);
                policeRanked3 = (TextView) dialog0.findViewById(R.id.policeRank3);
                policeRanked4 = (TextView) dialog0.findViewById(R.id.policeRank4);
                policeRanked5 = (TextView) dialog0.findViewById(R.id.policeRank5);
                policeRanked6 = (TextView) dialog0.findViewById(R.id.policeA);

                policeRanked1.setText(agencyNameCrime[0] + "" + statusCrime[0]);
                policeRanked2.setText(agencyNameCrime[1] + "" + statusCrime[1]);
                policeRanked3.setText(agencyNameCrime[2] + "" + statusCrime[2]);
                policeRanked4.setText(agencyNameCrime[3] + "" + statusCrime[3]);
                policeRanked5.setText(agencyNameCrime[4] + "" + statusCrime[4]);
                policeRanked6.setText(agencyNameCrime[5]);


                police1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[0].toString(), contCpCrime[0].toString(), regCrime[0], "Crime");

                    }
                });

                police2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[1].toString(), contCpCrime[1].toString(), regCrime[1], "Crime");

                    }
                });

                police3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[2].toString(), contCpCrime[2].toString(), regCrime[2], "Crime");

                    }
                });

                police4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[3].toString(), contCpCrime[3].toString(), regCrime[3], "Crime");

                    }
                });

                police5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[4].toString(), contCpCrime[4].toString(), regCrime[4], "Crime");

                    }
                });

                police6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[5].toString(), contCpCrime[5].toString(), "eoiTtKU8PrOYVR8BtoyvXBRr8fr1", "Crime");
                    }
                });

                dialog0.show();
            }
        });

        fire = (Button) findViewById(R.id.fireHelp);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showMsg("Im clicked! fire!");
                //Toast.makeText(helpactivity.this, "IM CLICKED! FIRE", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(context);
                dialog.setTitle("FIRE  ASSISTANCE");
                dialog.setContentView(R.layout.zdialog_fire);

                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog.getWindow().getAttributes());
                lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp1.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp1);

                fire1 = (LinearLayout) dialog.findViewById(R.id.fire1);
                fire2 = (LinearLayout) dialog.findViewById(R.id.fire2);
                fire3 = (LinearLayout) dialog.findViewById(R.id.fire3);
                fire4 = (LinearLayout) dialog.findViewById(R.id.fire4);
                fire5 = (LinearLayout) dialog.findViewById(R.id.fire5);
                fire6 = (LinearLayout) dialog.findViewById(R.id.fire6);

                fireRanked1 = (TextView) dialog.findViewById(R.id.fireRank1);
                fireRanked2 = (TextView) dialog.findViewById(R.id.fireRank2);
                fireRanked3 = (TextView) dialog.findViewById(R.id.fireRank3);
                fireRanked4 = (TextView) dialog.findViewById(R.id.fireRank4);
                fireRanked5 = (TextView) dialog.findViewById(R.id.fireRank5);
                fireRanked6 = (TextView) dialog.findViewById(R.id.fireA);

                fireRanked1.setText(agencyNameFire[0] + "" + statusFire[0]);
                fireRanked2.setText(agencyNameFire[1] + "" + statusFire[1]);
                fireRanked3.setText(agencyNameFire[2] + "" + statusFire[2]);
                fireRanked4.setText(agencyNameFire[3] + "" + statusFire[3]);
                fireRanked5.setText(agencyNameFire[4] + "" + statusFire[4]);
                fireRanked6.setText(agencyNameFire[5]);

                fire1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineFire[0].toString(), contCpFire[0].toString(), regFire[0], "Fire");

                    }
                });

                fire2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineFire[1].toString(), contCpFire[1].toString(), regFire[1], "Fire");

                    }
                });

                fire3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineFire[2].toString(), contCpFire[2].toString(), regFire[2], "Fire");

                    }
                });

                fire4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineFire[3].toString(), contCpFire[3].toString(), regFire[3], "Fire");

                    }
                });

                fire5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineFire[4].toString(), contCpFire[4].toString(), regFire[4], "Fire");

                    }
                });
                fire6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[5].toString(), contCpCrime[5].toString(), "L0LucU9RMgOtV1MwVCGUR6oyhy62", "Fire");

                    }
                });

                // set the custom dialog components - text, image and button

                dialog.show();

            }
        });


        medical = (Button) findViewById(R.id.medicalHelp);
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(context);
                dialog1.setTitle("MEDICAL ASSISTANCE");
                dialog1.setContentView(R.layout.zdialog_medical);

                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog1.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp2.gravity = Gravity.CENTER;

                dialog1.getWindow().setAttributes(lp2);

                medical1 = (LinearLayout) dialog1.findViewById(R.id.medical1);
                medical2 = (LinearLayout) dialog1.findViewById(R.id.medical2);
                medical3 = (LinearLayout) dialog1.findViewById(R.id.medical3);
                medical4 = (LinearLayout) dialog1.findViewById(R.id.medical4);
                medical5 = (LinearLayout) dialog1.findViewById(R.id.medical5);
                medical6 = (LinearLayout) dialog1.findViewById(R.id.medical6);

                medicalRanked1 = (TextView) dialog1.findViewById(R.id.medicalRank1);
                medicalRanked2 = (TextView) dialog1.findViewById(R.id.medicalRank2);
                medicalRanked3 = (TextView) dialog1.findViewById(R.id.medicalRank3);
                medicalRanked4 = (TextView) dialog1.findViewById(R.id.medicalRank4);
                medicalRanked5 = (TextView) dialog1.findViewById(R.id.medicalRank5);
                medicalRanked6 = (TextView) dialog1.findViewById(R.id.medicalA);

                medicalRanked1.setText(agencyNameMed[0] + "" + statusMed[0]);
                medicalRanked2.setText(agencyNameMed[1] + "" + statusMed[1]);
                medicalRanked3.setText(agencyNameMed[2] + "" + statusMed[2]);
                medicalRanked4.setText(agencyNameMed[3] + "" + statusMed[3]);
                medicalRanked5.setText(agencyNameMed[4] + "" + statusMed[4]);
                medicalRanked6.setText(agencyNameMed[5]);

                medical1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineMed[0].toString(), contCpMed[0].toString(), regMed[0], "Medical");
                    }
                });

                medical2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineMed[1].toString(), contCpMed[1].toString(), regMed[1], "Medical");
                    }
                });

                medical3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineMed[2].toString(), contCpMed[2].toString(), regMed[2], "Medical");
                    }
                });

                medical4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineMed[3].toString(), contCpMed[3].toString(), regMed[3], "Medical");
                    }
                });

                medical5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineMed[4].toString(), contCpMed[4].toString(), regMed[4], "Medical");
                    }
                });

                medical6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callDialog(contLandlineCrime[5].toString(), contCpCrime[5].toString(), "NFXLfsVQyZfCzSuz7n5PELNCvYl1", "Medical");
                    }
                });
                // set the custom dialog components - text, image and button

                dialog1.show();

            }
        });


        help = (Button) findViewById(R.id.firstAidHelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("alarma");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild("firstAiders")) {
                            if(nearestFA.equals("Failed")){
                                Toast.makeText(helpactivity.this,"Available First Aiders are too far from your location.",Toast.LENGTH_SHORT).show();
                                Toast.makeText(helpactivity.this,"We suggest you to call the nearest Ambulance.",Toast.LENGTH_SHORT).show();
                            }else{
                                callPhone(nearestFA);
                            }
                        }else{
                            Toast.makeText(helpactivity.this,"There are no current First Aiders Available",Toast.LENGTH_SHORT).show();
                            Toast.makeText(helpactivity.this,"Please contact the nearest Ambulance if things get worst.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
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
    public void getNearestFirstAider(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("alarma/firstAiders");

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();
        final int[] c = {1};
        final double[] distBet = {0};
        nearestFA = "Failed";
        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(!key.equals(UID)) {
                    String cpNo = dataSnapshot.child("faCNum").getValue(String.class);
                    double lat = dataSnapshot.child("faLat").getValue(double.class);
                    double lng = dataSnapshot.child("faLng").getValue(double.class);

                    // 10.2974593 123.8961415
                    double distBetween = calculateDistance(lat, lng, latitude, longitude);
                    if (distBetween < 0.25) {
                        if (c[0] == 1) {
                            distBet[0] = distBetween;
                            c[0]++;
                            nearestFA = cpNo;
                        } else {
                            if (distBetween < distBet[0]) {
                                distBet[0] = distBetween;
                                nearestFA = cpNo;
                            }
                        }

                    }
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
    public void getAllHotline(final String key) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("hotlines/" + key);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();

        formAddress = getFormattedAddress(latitude,longitude);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String cpNo = dataSnapshot.child("agencyCellPhone").getValue(String.class);
                String landline = dataSnapshot.child("agencyLandline").getValue(String.class);
                String agencyNames = dataSnapshot.child("agencyName").getValue(String.class);
                String lat = dataSnapshot.child("agencyLat").getValue(String.class);
                String lng = dataSnapshot.child("agencyLng").getValue(String.class);
                String alarmaReg = dataSnapshot.child("alarmaReg").getValue(String.class);
                String uids = dataSnapshot.getKey();

                double agencyLat = Double.parseDouble(lat);
                double agencyLng = Double.parseDouble(lng);
                // 10.2974593 123.8961415
                double distBetween = calculateDistance(agencyLat, agencyLng, latitude, longitude);

                switch (key) {
                    case "crime": {
                        if (uids.equals("eoiTtKU8PrOYVR8BtoyvXBRr8fr1")) {
                            contLandlineCrime[5] = landline;
                            contCpCrime[5] = cpNo;
                            agencyNameCrime[5] = agencyNames;
                        }
                        for(int i = 0; i!=5; i++){
                            if(agencyNameCrime[i] == null){
                                contLandlineCrime[i] = landline;
                                contCpCrime[i] = cpNo;
                                agencyNameCrime[i] = agencyNames;
                                distBetCrime[i] = distBetween;
                                if (alarmaReg.equals("true")) {
                                    regCrime[i] = uids;
                                    statusCrime[i] = "\n Alarma Registered!";
                                } else {
                                    regCrime[i] = "false";
                                    statusCrime[i] = "";
                                }
                                break;
                            }else if(distBetCrime[i] > distBetween){
                                if(i!=4) {
                                    for (int j = 3; j != i-1; j--) {
                                        if (agencyNameCrime[j] != null) {
                                            contLandlineCrime[j+1] = contLandlineCrime[j];
                                            contCpCrime[j+1] = contCpCrime[j];
                                            agencyNameCrime[j+1] = agencyNameCrime[j];
                                            distBetCrime[j+1] = distBetCrime[j];
                                            regCrime[j+1] = regCrime[j];
                                            statusCrime[j+1] = statusCrime[j];

                                        }
                                    }
                                }
                                contLandlineCrime[i] = landline;
                                contCpCrime[i] = cpNo;
                                agencyNameCrime[i] = agencyNames;
                                distBetCrime[i] = distBetween;
                                if (alarmaReg.equals("true")) {
                                    regCrime[i] = uids;
                                    statusCrime[i] = "\n Alarma Registered!";
                                } else {
                                    regCrime[i] = "false";
                                    statusCrime[i] = "";
                                }
                                break;
                            }
                        }

                        break;
                    }
                    case "fire": {
                        if (uids.equals("L0LucU9RMgOtV1MwVCGUR6oyhy62")) {
                            contLandlineFire[5] = landline;
                            contCpFire[5] = cpNo;
                            agencyNameFire[5] = agencyNames;
                        }
                        for(int i = 0; i!=5; i++){
                            if(agencyNameFire[i] == null){
                                contLandlineFire[i] = landline;
                                contCpFire[i] = cpNo;
                                agencyNameFire[i] = agencyNames;
                                distBetFire[i] = distBetween;
                                if (alarmaReg.equals("true")) {
                                    regFire[i] = uids;
                                    statusFire[i] = "\n Alarma Registered!";
                                } else {
                                    regFire[i] = "false";
                                    statusFire[i] = "";
                                }
                                break;
                            }else if(distBetFire[i] > distBetween){
                                if(i!=4) {
                                    for (int j = 3; j != i-1; j--) {
                                        if (agencyNameFire[j] != null) {
                                            contLandlineFire[j+1] = contLandlineFire[j];
                                            contCpFire[j+1] = contCpFire[j];
                                            agencyNameFire[j+1] = agencyNameFire[j];
                                            distBetFire[j+1] = distBetFire[j];
                                            regFire[j+1] = regFire[j];
                                            statusFire[j+1] = statusFire[j];

                                        }
                                    }
                                }
                                contLandlineFire[i] = landline;
                                contCpFire[i] = cpNo;
                                agencyNameFire[i] = agencyNames;
                                distBetFire[i] = distBetween;
                                if (alarmaReg.equals("true")) {
                                    regFire[i] = uids;
                                    statusFire[i] = "\n Alarma Registered!";
                                } else {
                                    regFire[i] = "false";
                                    statusFire[i] = "";
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case "medical": {
                        if (uids.equals("NFXLfsVQyZfCzSuz7n5PELNCvYl1")) {
                            contLandlineMed[5] = landline;
                            contCpMed[5] = cpNo;
                            agencyNameMed[5] = agencyNames;
                        }
                        for(int i = 0; i!=5; i++){
                            if(agencyNameMed[i] == null){
                                contLandlineMed[i] = landline;
                                contCpMed[i] = cpNo;
                                agencyNameMed[i] = agencyNames;
                                distBetMed[i] = distBetween;
                                if (alarmaReg.equals("true")) {
                                    regMed[i] = uids;
                                    statusMed[i] = "\n Alarma Registered!";
                                } else {
                                    regMed[i] = "false";
                                    statusMed[i] = "";
                                }
                                break;
                            }else if(distBetMed[i] > distBetween){
                                if(i!=4) {
                                    for (int j = 3; j != i-1; j--) {
                                        if (agencyNameMed[j] != null) {
                                            contLandlineMed[j+1] = contLandlineMed[j];
                                            contCpMed[j+1] = contCpMed[j];
                                            agencyNameMed[j+1] = agencyNameMed[j];
                                            distBetMed[j+1] = distBetMed[j];
                                            regMed[j+1] = regMed[j];
                                            statusMed[j+1] = statusMed[j];

                                        }
                                    }
                                }
                                contLandlineMed[i] = landline;
                                contCpMed[i] = cpNo;
                                agencyNameMed[i] = agencyNames;
                                distBetMed[i] = distBetween;
                                if (alarmaReg.equals("true")) {
                                    regMed[i] = uids;
                                    statusMed[i] = "\n Alarma Registered!";
                                } else {
                                    regMed[i] = "false";
                                    statusMed[i] = "";
                                }
                                break;
                            }
                        }
                        break;
                    }
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


    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int earthRadius = 6371;
        double dLat = (float) Math.toRadians(lat2 - lat1);
        double dLon = (float) Math.toRadians(lon2 - lon1);
        double a =
                (double) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        double c = (double) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        double d = earthRadius * c;
        return d;
    }

    public void callDialog(final String cpNos, final String ll, final String reg, final String type) {

        final Dialog dialog2 = new Dialog(context);
        dialog2.setTitle("HOTLINE  NUMBERS");
        dialog2.setContentView(R.layout.zdialog_medical_numbers);

        WindowManager.LayoutParams lp4 = new WindowManager.LayoutParams();
        lp4.copyFrom(dialog2.getWindow().getAttributes());
        lp4.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp4.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp4.gravity = Gravity.CENTER;
        dialog2.getWindow().setAttributes(lp4);

        c_number = (TextView) dialog2.findViewById(R.id.med_1);
        c_number.setText(cpNos);
        c_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reg.equals("false")) {
                    callPhone(cpNos);
                } else {
                    sendDistress(reg, type);
                    callPhone(cpNos);
                }
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference keyRef = database.getReference("mobileUsers/"+UID+"/guardian/myGuardian");
                keyRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("UID")){
                            String thisUID  = dataSnapshot.child("UID").getValue(String.class);
                            notifyGuardian notifyNow = new notifyGuardian(reportName,type);
                            DatabaseReference notifyRef = database.getReference("mobileUsers/"+thisUID+"/incidentReports");
                            notifyRef.push().setValue(notifyNow);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        dialog2.show();
    }

    public void sendDistress(String key, String type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference addDistress = database.getReference("userAccounts/" + key);
        String mGroupId = addDistress.push().getKey();
        final DatabaseReference addHistory = database.getReference("userAccounts/" + key + "/reports/" + mGroupId);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        DateFormat dateForm = new SimpleDateFormat(" M/d/yyyy");
        DateFormat timeForm = new SimpleDateFormat("h:mm:s a");

        String now = timeForm.format(Calendar.getInstance().getTime());
        String today = dateForm.format(Calendar.getInstance().getTime());

        postData sendDistressCall = new postData(formAddress,repContNumber,latitude,longitude,reportName,reportPURL,now,type);
        postData updateHist = new postData(now,today);

        addDistress.child("reports").child(mGroupId).setValue(sendDistressCall);
        addHistory.child("reportHist").push().setValue(updateHist);
    }
    public void showMsg(String key){
        Toast.makeText(this,key,Toast.LENGTH_SHORT);

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

        } else if (id == R.id.nav_manual) {

            Intent imanual = new Intent(this,manualactivity.class);
            startActivity(imanual);

        } else if (id == R.id.nav_help) {

            Intent iguard = new Intent(this,guardianactivity.class);
            startActivity(iguard);

        } else if (id == R.id.nav_setting) {
            Intent isetting = new Intent(this, settingactivity.class);
            startActivity(isetting);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @IgnoreExtraProperties
    public static class postData {

        public String formattedAddress;
        public String reportCNumber;
        public String reportCallStatus;
        public double reportLat;
        public double reportLng;
        public String reportName;
        public String reportPURL;
        public String reportResp;
        public String reportTime;
        public String reportType;

        public String time;
        public String date;
        public String repStatus;
        public String dispatcherID;
        public postData() {

        }
        public postData(String formattedAddress, String reportCNumber,
                        double reportLat, double reportLng, String reportName, String reportPURL,
                        String reportTime,String reportType) {
            this.formattedAddress = formattedAddress;
            this.reportCNumber = reportCNumber;
            this.reportLat = reportLat;
            this.reportLng = reportLng;
            this.reportName = reportName;
            this.reportPURL = reportPURL;
            this.reportTime = reportTime;
            this.reportType = reportType;
            this.reportResp = "No Response Yet";
            this.reportCallStatus = "On Going";
            this.reportName = reportName;
            this.dispatcherID = UID;
        }
        public postData(String time, String date) {
            this.time = time;
            this.date = date;
            this.repStatus = "Alarm was recieved.";

        }


    }
    @IgnoreExtraProperties
    class notifyGuardian{
        public String nameGuard;
        public String seen;
        public String reportType;
        public String reportedInci;

        public notifyGuardian(){

        }

        public notifyGuardian(String name, String type){
            this.nameGuard = name;
            this.seen = "false";
            this.reportType = "Guardian";
            this.reportedInci = type;
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonforecast){
            Toast.makeText(this, "NAAY BAGYO HELP", Toast.LENGTH_SHORT).show();
            Intent iforecast = new Intent(this, forecastactivity.class);
            startActivity(iforecast);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
