package alarma.example.com.alarmaph;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class registration extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    Button register;
    EditText first, last;
    EditText username, pass, firstname,lastname,email,phone;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "^(09|\\+639)\\d{9}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.editText1);
        pass = (EditText) findViewById(R.id.editText2);
        firstname = (EditText) findViewById(R.id.editText3);
        lastname = (EditText) findViewById(R.id.editText4);
        email = (EditText) findViewById(R.id.editText5);
        phone = (EditText) findViewById(R.id.editText6);


        register = (Button) findViewById(R.id.button);
        register.setOnClickListener(this);

    }
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, browseactivity.class);
            boolean emailVerified = user.isEmailVerified();
            startActivity(intent);
        }
    }

    @IgnoreExtraProperties
    public static class postData {

        public String username;
        public String firstName;
        public String lastName;
        public String userEmail;
        public String phoneNumber;
        public String points;
        public String homeNumber;
        public String address;
        public String relFields;

        public String agencyName;
        public String PURL;

        public double lat;
        public double lng;
        public String formAdd;
        public String remarks;

        public String gender;
        public String age;

        public postData() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
        public postData(String PURL, String agencyName){
            this.agencyName  = agencyName;
            this.PURL = PURL;

        }
        public postData(double lat, double lng, String formAdd, String remarks){
            this.lat = lat;
            this.lng = lng;
            this.formAdd = formAdd;
            this.remarks = remarks;
        }
        public postData(String usernameF, String firstnameF, String lastnameF, String emailF, String phoneF) {
            this.username = usernameF;
            this.firstName = firstnameF;
            this.lastName = lastnameF;
            this.userEmail = emailF;
            this.phoneNumber = phoneF;
            this.points = "100";
            this.address = "";
            this.PURL = "";
            this.homeNumber = "";
            this.relFields = "";
        }
        public postData(String firstName, String lastName, String age, String gender, String phoneF, String value) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
            this.phoneNumber = phoneF;
        }

    }
    public void showMsg(String key){
        Toast.makeText(this,key, Toast.LENGTH_LONG).show();

    }
    @Override
    public void onClick(View v) {
        final String _username = username.getText().toString();
        String _pass = pass.getText().toString();
        final String _firstname = firstname.getText().toString();
        final String _lastname = lastname.getText().toString();
        final String _email = email.getText().toString();
        final String _phone = phone.getText().toString();
        if(v.getId() == R.id.button){
            if (TextUtils.isEmpty(_username)) {
                Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(_pass)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (_pass.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(_firstname)) {
                Toast.makeText(getApplicationContext(), "Enter first name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(_lastname)) {
                Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(_email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!_email.matches(emailPattern)) {
                Toast.makeText(getApplicationContext(), "Please insert a valid email", Toast.LENGTH_SHORT).show();
                return;

            }
            if (TextUtils.isEmpty(_phone)) {
                Toast.makeText(getApplicationContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!_phone.matches(mobilePattern)) {
                Toast.makeText(getApplicationContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(_email, _pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference adminUsersRef = database.getReference("userAccounts");
                                DatabaseReference mobileUsersRef = database.getReference("mobileUsers");

                                final DatabaseReference followsRef = database.getReference("mobileUsers/"+uid+"/follows");

                                postData mobileUser = new postData(_username , _firstname , _lastname , _email , _phone );

                                mobileUsersRef.child(uid).setValue(mobileUser);

                                adminUsersRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        String uids = dataSnapshot.getKey();
                                        uids = uids.toString();
                                        String PURL = dataSnapshot.child("logoURL").getValue(String.class);
                                        String agencyName = dataSnapshot.child("aliasName").getValue(String.class);
                                        final postData followAdmin = new postData(PURL,agencyName);
                                        followsRef.child(uids).setValue(followAdmin);

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

                                showMsg("Success!");
                            }else{
                                showMsg("Failed!");
                            }
                        }
                    });

        }

    }
}
