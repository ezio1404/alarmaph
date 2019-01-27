package alarma.example.com.alarmaph;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class citizen_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView citlist;


    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;


    Button addpost;
    final  Context context = this;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> imgpost = new ArrayList<>();
  /*
    String[] name= {
            "Carlo Saberon",
            "Joshua Del Mar",
            "Christine Jhoy Carubio"
    };

    String[] desc= {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
    };

    Integer[] img= {
            R.drawable.carlo,
            R.drawable.carlo,
            R.drawable.carlo

    };

    Integer[] imgpost= {
            R.drawable.bfp,
            R.drawable.linepar,
            R.drawable.bfp



    };
*/
    ImageView posimg;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_activity);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("userAccounts/CitizenReport/posts");
        mDatabase.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String names = dataSnapshot.child("postAgencyName").getValue(String.class);
                String descs = dataSnapshot.child("postDesc").getValue(String.class);
                String purls= dataSnapshot.child("postLogo").getValue(String.class);
                String imgs = dataSnapshot.child("downloadURL").getValue(String.class);
                //String imgs = "";
                Collections.reverse(name);
                name.add(names);
                Collections.reverse(name);

                Collections.reverse(desc);
                desc.add(descs);
                Collections.reverse(desc);

                Collections.reverse(img);
                img.add(purls);
                Collections.reverse(img);

                Collections.reverse(imgpost);
                imgpost.add(imgs);
                Collections.reverse(imgpost);

                Toast.makeText(citizen_activity.this,"Loading data",Toast.LENGTH_SHORT).show();
                citizen_list adapter = new citizen_list(citizen_activity.this, name,desc,img, imgpost);
                citlist=(ListView)findViewById(R.id.listcitizen);
                citlist.setAdapter(adapter);
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

        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        UID = currentFirebaseUser.getUid().toString();



        addpost = (Button) findViewById(R.id.citaddpost);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog2 = new Dialog(context);
                dialog2.setTitle("POST");
                dialog2.setContentView(R.layout.zdialog_citaddpost);

                WindowManager.LayoutParams lp4 = new WindowManager.LayoutParams();
                lp4.copyFrom(dialog2.getWindow().getAttributes());
                lp4.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp4.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp4.gravity = Gravity.CENTER;
                dialog2.getWindow().setAttributes(lp4);

                final EditText thisComment = (EditText) dialog2.findViewById(R.id.editText7c);
                Button sub,can;
                posimg = (ImageView) dialog2.findViewById(R.id.citaddimg);

                can = (Button) dialog2.findViewById(R.id.postcancel);
                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });


                posimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPictureDialog();
                    }
                });

                sub = (Button) dialog2.findViewById(R.id.postsubmit);

                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String commentVal = thisComment.getText().toString();

                        if(TextUtils.isEmpty(commentVal)){
                            Toast.makeText(citizen_activity.this,"Please input post description",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/");
                        mDatabase.addChildEventListener(new ChildEventListener(){
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String key = dataSnapshot.getKey();
                                if(key.equals(UID)){
                                    String dlImage = "";
                                    String PURL = dataSnapshot.child("PURL").getValue(String.class);
                                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                                    String fullName = firstName+" "+lastName;

                                    postData postComment = new postData(UID,fullName,commentVal,PURL);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference postRef = database.getReference("userAccounts/CitizenReport/");


                                    postRef.child("posts").push().setValue(postComment);
                                    /*if(mDatabase.child("temp").getRoot() != null) {
                                        dlImage = dataSnapshot.child("temp").getValue(String.class);
                                        postData imageComment = new postData(dlImage);
                                        postRef.child("posts").child(pushkey).child("photos").setValue(imageComment);
                                        postRef.child("posts").child(pushkey).child("downloadURL").setValue(dlImage);
                                        mDatabase.child(UID+"/temp").removeValue();
                                    }*/
                                    //String UID, String postAgencyName, String postDesc,String postLogo
                                }
                                Toast.makeText(citizen_activity.this,"Post Successful",Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
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
                dialog2.show();
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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showPictureDialog(){
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
                    Toast.makeText(citizen_activity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    posimg.setImageBitmap(bitmap);
                    String text = new uploadImages().execute(bitmap).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(citizen_activity.this, "Failed!", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            posimg.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            try {
                String text = new uploadImages().execute(thumbnail).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Toast.makeText(citizen_activity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
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
    class postData{
        public String UID;
        public String postAgencyName;
        public String postAlias;
        public String postDesc;
        public String postDate;
        public int postLikes;
        public String postLogo;
        public String postTime;

        public String downloadURL;
        public String imageName;
        public postData(){

        }
        public postData(String UID, String postAgencyName, String postDesc
                    ,String postLogo){
            this.UID = UID;
            this.postAgencyName = postAgencyName;
            this.postAlias = "Alarma User";
            this.postDesc = postDesc;
            this.postDate = "";
            this.postLikes = 0;
            this.postLogo = postLogo;
            this.postTime = "";
            this.downloadURL = "";
        }

    }
}
class uploadImages extends AsyncTask<Bitmap, Bitmap, String> {
    String url = "";
    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();


        Bitmap myBitmap = bitmaps[0];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReferenceFromUrl("gs://sample");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference("userAccounts/CitizenReport");
        String pushkey = postRef.push().getKey();

        StorageReference storageRef =  storage.getReference();
        StorageReference profRef = storageRef.child("mobileUser/"+uid+"/"+pushkey+"/temp.jpg");

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

                putTemp(downloadUrl);
            }
        });
        return url;
    }
    void putTemp(Uri key){
        FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentFirebaseUser.getUid().toString();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("mobileUsers/"+uid);
        mDatabase.child("temp").setValue(key.toString());
    }
}
