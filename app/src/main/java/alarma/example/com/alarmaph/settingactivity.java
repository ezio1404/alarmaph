package alarma.example.com.alarmaph;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class settingactivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    Button sfb, terms;
    final Context context = this;
    TextView trms;

    TextView eaddr, econnum, ehomenum, erfield;
    TextView addr,connum,homenum, rfield;

    String mobilePattern = "^(09|\\+639)\\d{9}$";
    String landlinePattern = "^\\d{7}$";
    TextView name,email,points,myguardian;
    String UID;

    Button showKey, addGuardian;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    CircleImageView eimg;



    Switch rf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingactivity);

        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();
        UID = uid;

        //bday = (TextView) findViewById(R.id.bday);
        addr = (TextView) findViewById(R.id.addr);
        connum = (TextView) findViewById(R.id.cnumber);
        homenum = (TextView) findViewById(R.id.hnum);
        rfield = (TextView) findViewById(R.id.rfield);
        myguardian = (TextView) findViewById(R.id.guardianName);
        name = (TextView) findViewById(R.id.n);
        email= (TextView) findViewById(R.id.p);
        points = (TextView) findViewById(R.id.e);

        eimg = (CircleImageView) findViewById(R.id.imguseredit);
        final DatabaseReference mDatabase;

        showKey = (Button) findViewById(R.id.showKey);
        addGuardian = (Button) findViewById(R.id.addGuardian);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference keyRef = database.getReference("mobileUsers/"+UID+"/guardian/myGuardian");
        keyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("UID")){
                    String fname = dataSnapshot.child("fullName").getValue(String.class);
                    myguardian.setText(fname);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.zdialog_showkey);
                dialog.setTitle("Show Key");
                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog.getWindow().getAttributes());
                lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp1.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp1);

                TextView guardian = (TextView) dialog.findViewById(R.id.my_key);
                guardian.setText(UID);
                Button dialogSubmit= (Button) dialog.findViewById(R.id.confirm);

                // if button is clicked, close the custom dialog
                dialogSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        addGuardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.zdialog_add_guardian);
                dialog.setTitle("Add Guardian");
                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog.getWindow().getAttributes());
                lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp1.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp1);

                final EditText gKey = (EditText) dialog.findViewById(R.id.addGuard);
                Button dialogSubmit= (Button) dialog.findViewById(R.id.acceptGuardian);


                // if button is clicked, close the custom dialog
                dialogSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String gKeyInput = gKey.getText().toString();
                        if(TextUtils.isEmpty(gKeyInput)){
                            Toast.makeText(context, "Please fill in the required field", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference keyRef = database.getReference("mobileUsers/"+gKeyInput);
                        keyRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("firstName")){
                                    String fname = dataSnapshot.child("firstName").getValue(String.class);
                                    String lname = dataSnapshot.child("lastName").getValue(String.class);

                                    submitGuardian guardian = new submitGuardian(fname+" "+lname,gKeyInput);


                                    DatabaseReference addGuardianNow = database.getReference("mobileUsers/"+UID+"/guardian/");
                                    myguardian.setText(fname+" "+lname);
                                    addGuardianNow.child("myGuardian").setValue(guardian);

                                    Toast.makeText(context, fname+" "+lname+" assigned as your Guardian", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Invalid Key!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                dialog.show();

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/");

        mDatabase.addChildEventListener(new ChildEventListener(){


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(key.equals(uid)){
                    String fname = dataSnapshot.child("firstName").getValue(String.class);
                    String lname = dataSnapshot.child("lastName").getValue(String.class);
                    String emails = dataSnapshot.child("userEmail").getValue(String.class).toString();
                    String pointss = dataSnapshot.child("points").getValue(String.class);
                    String PURL = dataSnapshot.child("PURL").getValue(String.class);
                    String contNum = dataSnapshot.child("phoneNumber").getValue(String.class).toString();
                    String address = dataSnapshot.child("address").getValue(String.class).toString();
                    String homeNumber = dataSnapshot.child("homeNumber").getValue(String.class).toString();
                    String relFields = dataSnapshot.child("relFields").getValue(String.class).toString();

                    connum.setText(contNum);
                    addr.setText(address);
                    homenum.setText(homeNumber);
                    name.setText(fname+" "+lname);
                    email.setText(emails);
                    points.setText(pointss);
                    rfield.setText(relFields);

                    if(relFields.equals("")){
                        rf.setEnabled(false);
                    }else{
                        rf.setEnabled(true);
                        DatabaseReference refThis = FirebaseDatabase.getInstance().getReference("alarma/firstAiders");
                        if (refThis.child(UID).getRoot() != null) {
                            rf.setChecked(true);
                        }else{
                            rf.setChecked(false);
                        }
                    }

                    try {
                        Bitmap thisImage = new sync_task().execute(PURL).get();
                        eimg.setImageBitmap(thisImage);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
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


        eimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });


        /* EDIT */

        eaddr = (TextView) findViewById(R.id.editaddr);
        econnum = (TextView) findViewById(R.id.editcon);
        ehomenum = (TextView) findViewById(R.id.edithome);
        erfield = (TextView) findViewById(R.id.editrfield);
        rf = (Switch) findViewById(R.id.rfswitch);




        eaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Address", addr.getText().toString());
            }
        });

        econnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Contact number", connum.getText().toString());
            }
        });

        ehomenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Home number", homenum.getText().toString());
            }
        });
        erfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRfield();
            }
        });
        rf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rf.isChecked()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("mobileUsers/"+uid+"/phoneNumber");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String contact = (String) dataSnapshot.getValue();
                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (ActivityCompat.checkSelfPermission(settingactivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(settingactivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("alarma/firstAiders");

                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            final double longitude = location.getLongitude();
                            final double latitude = location.getLatitude();
                            postDatas mobileUser = new postDatas(contact, latitude, longitude);

                            ref.child(UID).setValue(mobileUser);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("alarma/firstAiders");
                    ref.child(UID).removeValue();
                }
            }
        });

       /* END OF EDIT */

        sfb = (Button) findViewById(R.id.buttonsf);
        terms = (Button) findViewById(R.id.buttontc);

        sfb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.zdialog_feedback);

                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog.getWindow().getAttributes());
                lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp1.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp1);


                Button dialogSubmit= (Button) dialog.findViewById(R.id.dialogButtonOK);
                Button dialogCancel= (Button) dialog.findViewById(R.id.dialogButtonCancel);

                // if button is clicked, close the custom dialog
                dialogSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "THANK YOU", Toast.LENGTH_SHORT).show();
                    }
                });

                // if button is clicked, close the custom dialog
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog1 = new Dialog(context);
                dialog1.setContentView(R.layout.zdialog_terms);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog1.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog1.getWindow().setAttributes(lp);

                trms = (TextView) dialog1.findViewById(R.id.trms);
                trms.setMovementMethod(new ScrollingMovementMethod());
                trms.setText("Terms and Conditions (\"Terms\")  \n" +
                        "==============================\n" +
                        "\n" +
                        "Last updated: January 04, 2018\n" +
                        "\n" +
                        "Please read these Terms and Conditions (\"Terms\", \"Terms and Conditions\")\n" +
                        "carefully before using the www.alarmaph.com website (the \"Service\") operated\n" +
                        "by Alarma (\"us\", \"we\", or \"our\").\n" +
                        "\n" +
                        "Your access to and use of the Service is conditioned on your acceptance of and\n" +
                        "compliance with these Terms. These Terms apply to all visitors, users and\n" +
                        "others who access or use the Service.\n" +
                        "\n" +
                        "By accessing or using the Service you agree to be bound by these Terms. If you\n" +
                        "disagree with any part of the terms then you may not access the Service.\n" +
                        "Alarma.\n" +
                        "\n" +
                        "Accounts  \n" +
                        "--------\n" +
                        "\n" +
                        "When you create an account with us, you must provide us information that is\n" +
                        "accurate, complete, and current at all times. Failure to do so constitutes a\n" +
                        "breach of the Terms, which may result in immediate termination of your account\n" +
                        "on our Service.\n" +
                        "\n" +
                        "You are responsible for safeguarding theS password that you use to access the\n" +
                        "Service and for any activities or actions under your password, whether your\n" +
                        "password is with our Service or a third-party service.\n" +
                        "\n" +
                        "You agree not to disclose your password to any third party. You must notify us\n" +
                        "immediately upon becoming aware of any breach of security or unauthorized use\n" +
                        "of your account.\n" +
                        "\n" +
                        "Links To Other Web Sites  \n" +
                        "------------------------\n" +
                        "\n" +
                        "Our Service may contain links to third-party web sites or services that are\n" +
                        "not owned or controlled by Alarma.\n" +
                        "\n" +
                        "Alarma has no control over, and assumes no responsibility for, the content,\n" +
                        "privacy policies, or practices of any third party web sites or services. You\n" +
                        "further acknowledge and agree that Alarma shall not be responsible or liable,\n" +
                        "directly or indirectly, for any damage or loss caused or alleged to be caused\n" +
                        "by or in connection with use of or reliance on any such content, goods or\n" +
                        "services available on or through any such web sites or services.\n" +
                        "\n" +
                        "We strongly advise you to read the terms and conditions and privacy policies\n" +
                        "of any third-party web sites or services that you visit.\n" +
                        "\n" +
                        "Termination  \n" +
                        "-----------\n" +
                        "\n" +
                        "We may terminate or suspend access to our Service immediately, without prior\n" +
                        "notice or liability, for any reason whatsoever, including without limitation\n" +
                        "if you breach the Terms.\n" +
                        "\n" +
                        "All provisions of the Terms which by their nature should survive termination\n" +
                        "shall survive termination, including, without limitation, ownership\n" +
                        "provisions, warranty disclaimers, indemnity and limitations of liability.\n" +
                        "\n" +
                        "We may terminate or suspend your account immediately, without prior notice or\n" +
                        "liability, for any reason whatsoever, including without limitation if you\n" +
                        "breach the Terms.\n" +
                        "\n" +
                        "Upon termination, your right to use the Service will immediately cease. If you\n" +
                        "wish to terminate your account, you may simply discontinue using the Service.\n" +
                        "\n" +
                        "All provisions of the Terms which by their nature should survive termination\n" +
                        "shall survive termination, including, without limitation, ownership\n" +
                        "provisions, warranty disclaimers, indemnity and limitations of liability.\n" +
                        "\n" +
                        "Governing Law  \n" +
                        "-------------\n" +
                        "\n" +
                        "These Terms shall be governed and construed in accordance with the laws of\n" +
                        "Philippines, without regard to its conflict of law provisions.\n" +
                        "\n" +
                        "Our failure to enforce any right or provision of these Terms will not be\n" +
                        "considered a waiver of those rights. If any provision of these Terms is held\n" +
                        "to be invalid or unenforceable by a court, the remaining provisions of these\n" +
                        "Terms will remain in effect. These Terms constitute the entire agreement\n" +
                        "between us regarding our Service, and supersede and replace any prior\n" +
                        "agreements we might have between us regarding the Service.\n" +
                        "\n" +
                        "Changes  \n" +
                        "-------\n" +
                        "\n" +
                        "We reserve the right, at our sole discretion, to modify or replace these Terms\n" +
                        "at any time. If a revision is material we will try to provide at least 30 days\n" +
                        "notice prior to any new terms taking effect. What constitutes a material\n" +
                        "change will be determined at our sole discretion.\n" +
                        "\n" +
                        "By continuing to access or use our Service after those revisions become\n" +
                        "effective, you agree to be bound by the revised terms. If you do not agree to\n" +
                        "the new terms, please stop using the Service.\n" +
                        "\n" +
                        "Contact Us  \n" +
                        "----------\n" +
                        "\n" +
                        "If you have any questions about these Terms, please contact us.\n" +
                        "\n");



                // set the custom dialog components - text, image and button


                Button dialogSubmit= (Button) dialog1.findViewById(R.id.dialogButtonOKs);

                // if button is clicked, close the custom dialog
                dialogSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "THANK YOU", Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                    }
                });



                dialog1.show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

    }

    public void editInfo(final String key, String val) {
        final Dialog dialog5 = new Dialog(context);
        dialog5.setContentView(R.layout.zdialog_edit);
        dialog5.setTitle("Edit " + key);

        WindowManager.LayoutParams lp5 = new WindowManager.LayoutParams();
        lp5.copyFrom(dialog5.getWindow().getAttributes());
        lp5.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp5.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp5.gravity = Gravity.CENTER;


        dialog5.getWindow().setAttributes(lp5);


        final EditText edit = (EditText) dialog5.findViewById(R.id.editinfo);
        Button submitedit = (Button) dialog5.findViewById(R.id.editSubmit);
        edit.setHint(key);
        edit.setText(val);
        if (!key.equals("Address"))
            edit.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        submitedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit.toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference followsRef = database.getReference("mobileUsers/"+UID);
                switch(key){
                    case "Home number":{
                        if (!edit.getText().toString().matches(landlinePattern)) {
                            Toast.makeText(getApplicationContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            followsRef.child("homeNumber").setValue(edit.getText().toString());
                            homenum.setText(edit.getText().toString());
                        }
                        break;
                    }
                    case "Contact number":{
                        if (edit.getText().toString().matches(mobilePattern)) {
                            Toast.makeText(getApplicationContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            followsRef.child("phoneNumber").setValue(edit.getText().toString());
                            connum.setText(edit.getText().toString());
                        }
                        break;
                    }
                    case "Address":{
                        followsRef.child("address").setValue(edit.getText().toString());
                        addr.setText(edit.getText().toString());
                    }
                }
                dialog5.dismiss();
            }
        });

        // if button is clicked, close the custom dialog

        dialog5.show();
    }
    public void editRfield(){
        final Dialog dialog6 = new Dialog(context);
        dialog6.setContentView(R.layout.zdialog_rfieldedit);
        dialog6.setTitle("Edit ");

        WindowManager.LayoutParams lp6 = new WindowManager.LayoutParams();
        lp6.copyFrom(dialog6.getWindow().getAttributes());
        lp6.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp6.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp6.gravity = Gravity.CENTER;

        dialog6.getWindow().setAttributes(lp6);

        RadioGroup rselect = (RadioGroup)  dialog6.findViewById(R.id.rfselect);
        final RadioButton rf1,rf2,rf3,rf4,rf5,rnone;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference followsRef = database.getReference("mobileUsers/"+UID);

        rf1 = (RadioButton) dialog6.findViewById(R.id.rf1);
        rf2 = (RadioButton) dialog6.findViewById(R.id.rf2);
        rf3 = (RadioButton) dialog6.findViewById(R.id.rf3);
        rf4 = (RadioButton) dialog6.findViewById(R.id.rf4);
        rf5 = (RadioButton) dialog6.findViewById(R.id.rf5);
        rnone = (RadioButton) dialog6.findViewById(R.id.rnone);

        Button rfsub = (Button)dialog6.findViewById(R.id.rfsub);
        rfsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rf1.isChecked()){
                    followsRef.child("relFields").setValue("Trained First Aider");
                    rfield.setText("Trained First Aider");
                    rf.setEnabled(true);
                    dialog6.dismiss();
                }else if(rf2.isChecked()){
                    followsRef.child("relFields").setValue("EMT-Basic");
                    rfield.setText("EMT-Basic");
                    rf.setEnabled(true);
                    dialog6.dismiss();
                }else if(rf3.isChecked()){
                    followsRef.child("relFields").setValue("EMT-Intermediate");
                    rfield.setText("EMT-Intermediate");
                    rf.setEnabled(true);
                    dialog6.dismiss();
                }else if(rf4.isChecked()){
                    followsRef.child("relFields").setValue("EMT-Paramedics");
                    rfield.setText("EMT-Paramedics");
                    rf.setEnabled(true);
                    dialog6.dismiss();
                }else if(rf5.isChecked()){
                    followsRef.child("relFields").setValue("Medical Degree Holder");
                    rfield.setText("Medical Degree Holder");
                    rf.setEnabled(true);
                    dialog6.dismiss();
                }else if(rnone.isChecked()){
                    followsRef.child("relFields").setValue("");
                    rfield.setText("");
                    rf.setChecked(false);
                    rf.setEnabled(false);
                    dialog6.dismiss();
                }else{
                    Toast.makeText(settingactivity.this,"Please input Related Fields",Toast.LENGTH_SHORT).show();
                }


            }
        });





        // if button is clicked, close the custom dialog

        dialog6.show();
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
           /* Double longitude = extras.getDouble("Longitude");
            Double latitude = extras.getDouble("Latitude");
            showMsg(longitude+" "+latitude);*/
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
                    Toast.makeText(settingactivity.this, "Image "+bitmap, Toast.LENGTH_SHORT).show();
                    eimg.setImageBitmap(bitmap);
                    if(path != null){
                        String pp = new uploadImage().execute(bitmap).get();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(settingactivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            eimg.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(settingactivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            try {
                String pp = new uploadImage().execute(thumbnail).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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
    @IgnoreExtraProperties
    public static class postDatas{
        public String faCNum;
        public double faLat;
        public double faLng;

        public postDatas(){

        }
        public postDatas(String faCNum, double faLat, double faLng){
            this.faCNum = faCNum;
            this.faLat = faLat;
            this.faLng = faLng;
        }

    }

    @IgnoreExtraProperties
    public static class submitGuardian{
        public String fullName;
        public String UID;

        public submitGuardian(){

        }
        public submitGuardian(String fullName, String key){
            this.fullName = fullName;
            this.UID = key;
        }

    }
}
class uploadImage extends AsyncTask<Bitmap, Bitmap, String>{
    String url = "";
    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();


        Bitmap myBitmap = bitmaps[0];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReferenceFromUrl("gs://sample");
        StorageReference storageRef =  storage.getReference();
        StorageReference profRef = storageRef.child("mobileUser/"+uid+"/profilepic.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profRef .putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                url =  downloadUrl.toString();
                updatePP(downloadUrl);
            }
        });
        return url;
    }
    void updatePP(Uri key){
        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid);
        mDatabase.child("PURL").setValue(key.toString());
    }
}
