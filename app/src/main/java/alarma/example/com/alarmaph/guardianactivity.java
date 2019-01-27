package alarma.example.com.alarmaph;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class guardianactivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Button forecast, addp, addl;
    Context context = this;

    ListView lguardian;

    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private ImageButton btn;

    ArrayList<String> gname = new ArrayList<>();
    ArrayList<String> gage = new ArrayList<>();
    ArrayList<String> gaddr = new ArrayList<>();
    ArrayList<String> gemer = new ArrayList<>();
    ArrayList<Integer> imageId = new ArrayList<>();

    int counter = 0;
    int maxInput = 0;
    double longitude,latitude;
    String formAdd;
    String mobilePattern = "^(09|\\+639)\\d{9}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guardianactivity);

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
        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid+"/guardian/person");
        mDatabase.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String personKey = (String)dataSnapshot.getKey();
                String age = dataSnapshot.child("age").getValue(String.class);
                String fname = dataSnapshot.child("firstName").getValue(String.class);
                String lname = dataSnapshot.child("lastName").getValue(String.class);
                String phonenumber = dataSnapshot.child("phoneNumber").getValue(String.class);

                gname.add(fname+" "+lname);
                gage.add(age);
                gemer.add(phonenumber);
                imageId.add(R.drawable.user);
                counter++;
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

        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid+"/guardian/location");
        mDatabase.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String personKey = (String)dataSnapshot.getKey();
                String formAdd = dataSnapshot.child("formAdd").getValue(String.class);
                String remarks= dataSnapshot.child("remarks").getValue(String.class);

                gname.add(formAdd);
                gage.add(remarks);
                gemer.add("");
                imageId.add(R.drawable.loc_user);
                counter++;
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
        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid+"/points");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String points = (String) dataSnapshot.getValue();
                maxInput = parseInt(points) / 20;

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

        getLocation();

        final FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();

        forecast = (Button) findViewById(R.id.buttonforecast);
        forecast.setOnClickListener(this);

        addp = (Button) findViewById(R.id.addp);
        addl = (Button) findViewById(R.id.addLoc);

        addl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // custom dialog
                if(maxInput <= counter){
                    showMsg("You have reached the maximum number of Guardian List");
                    showMsg("Gain more points to add more Guardian Lists");
                }else{
                    final Dialog dialog2 = new Dialog(context);
                    dialog2.setContentView(R.layout.zdialog_addlocation);
                    // dialog2.setTitle("Add Location");

                    WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                    lp1.copyFrom(dialog2.getWindow().getAttributes());
                    lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp1.gravity = Gravity.TOP;
                    dialog2.getWindow().setAttributes(lp1);

                    final EditText formAddress = (EditText) dialog2.findViewById(R.id.editText7);
                    final EditText remarks = (EditText) dialog2.findViewById(R.id.editText3);

                    formAddress.setText(formAdd);

                    Button dialogSubmit = (Button) dialog2.findViewById(R.id.dialogButtonOKl);
                    Button dialogCancel = (Button) dialog2.findViewById(R.id.dialogButtonCancell);

                    // if button is clicked, close the custom dialog
                    dialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            String formAddPut = formAddress.getText().toString();
                            String remarksPut = remarks.getText().toString();
                            if(formAddress.getText().toString() !=null ){
                                //double lat = getCurrentLocation("lat");
                                //double lng = getCurrentLocation("lng");

                                if (TextUtils.isEmpty(formAddPut)) {
                                    Toast.makeText(getApplicationContext(), "Enter formatted address", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(remarksPut)) {
                                    Toast.makeText(getApplicationContext(), "Enter location remarks!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registration.postData addLocationToGuardian = new registration.postData(latitude,longitude,formAddPut,remarksPut);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                FirebaseUser user = mAuth.getCurrentUser();
                                String currentUID = user.getUid();

                                DatabaseReference addLocationRef = database.getReference("mobileUsers/"+currentUID+"/guardian/");

                                addLocationRef.child("location").push().setValue(addLocationToGuardian);
                                Toast.makeText(context, "ADDED TO GUARDIAN LIST", Toast.LENGTH_SHORT).show();
                            }else{
                                showMsg("Turn on your GPS to get current Location.");
                                dialog2.dismiss();
                            }
                        }
                    });

                    // if button is clicked, close the custom dialog
                    dialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    dialog2.show();
                }

            }
        });

        addp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            if(maxInput <= counter){
                showMsg("You have reached the maximum number of Guardian List");
                showMsg("Gain more points to add more Guardian Lists");
            }else{
                // custom dialog
                final Dialog dialog2 = new Dialog(context);
                dialog2.setContentView(R.layout.zdialog_addperson);
                dialog2.setTitle("Add Person");

                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog2.getWindow().getAttributes());
                lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp1.gravity = Gravity.TOP;
                dialog2.getWindow().setAttributes(lp1);

                imageview = (ImageView) dialog2.findViewById(R.id.addimgs);

                final EditText txtfirstName = (EditText) dialog2.findViewById(R.id.editText7);
                final EditText txtlastName = (EditText) dialog2.findViewById(R.id.editText3);
                final EditText txtage = (EditText) dialog2.findViewById(R.id.editText9);
                final EditText txtcontact = (EditText) dialog2.findViewById(R.id.editText5);
                final RadioButton rbGender = (RadioButton) dialog2.findViewById(R.id.male123);

                Button dialogSubmit = (Button) dialog2.findViewById(R.id.dialogButtonOKp);
                Button dialogCancel = (Button) dialog2.findViewById(R.id.dialogButtonCancelp);

                // if button is clicked, close the custom dialog
                dialogSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstName;
                        String lastName;
                        String age;
                        String contact;
                        String genderVal;
                        if(rbGender.isChecked()){
                            genderVal = "Male";
                        }else{
                            genderVal = "Female";
                        }


                        firstName = txtfirstName.getText().toString();
                        lastName = txtlastName.getText().toString();
                        age = txtage.getText().toString();
                        contact = txtcontact.getText().toString();

                        if (TextUtils.isEmpty(firstName)) {
                            Toast.makeText(getApplicationContext(), "Enter first name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(lastName)) {
                            Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(age)) {
                            Toast.makeText(getApplicationContext(), "Enter age!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(contact)) {
                            Toast.makeText(getApplicationContext(), "Enter phone number in case of emergency.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!contact.matches(mobilePattern)) {
                            Toast.makeText(getApplicationContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        registration.postData addPersonGuardian = new registration.postData(firstName,lastName,age,genderVal,contact,"addPersonGuardian");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        FirebaseUser user = mAuth.getCurrentUser();
                        String currentUID = user.getUid();

                        DatabaseReference addLocationRef = database.getReference("mobileUsers/"+currentUID+"/guardian/");

                        addLocationRef.child("person").push().setValue(addPersonGuardian);
                        dialog2.dismiss();
                        Toast.makeText(context, "ADDED TO GUARDIAN LIST", Toast.LENGTH_SHORT).show();

                    }
                });

                // if button is clicked, close the custom dialog
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

                dialog2.show();
            }

            }
        });

        Guardianlist adapter = new Guardianlist(guardianactivity.this, gname, gage, gemer, imageId);
        lguardian = (ListView) findViewById(R.id.lguardian);
        lguardian.setAdapter(adapter);
/*
        lguardian.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(guardianactivity.this, "" + gaddr.get(+position), Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    private String getFormattedAddress(double lat, double lng){
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
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    public void getLocation(){
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
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        formAdd = getFormattedAddress(latitude,longitude);
    }
    public double getCurrentLocation(String key){
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                if(key.equals("lat")){
                    return mLastLocation.getLatitude();
                }else if(key.equals("lng")){
                    return mLastLocation.getLongitude();
                }
            }
        } catch (SecurityException e) {
        }
        return 0;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }
    @Override
    public void onConnectionSuspended(int i) {
        showMsg("connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showMsg("connection failed");
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
            Toast.makeText(this, "NAAY BAGYO GUARDIAN", Toast.LENGTH_SHORT).show();
            Intent iforecast = new Intent(this, forecastactivity.class);
            startActivity(iforecast);
        }

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    public void showMsg(String key){
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bundle extras = data.getExtras();
            Double longitude = extras.getDouble("Longitude");
            Double latitude = extras.getDouble("Latitude");
            showMsg(longitude+" "+latitude);
        }
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(guardianactivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(guardianactivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(guardianactivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}

