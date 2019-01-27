package alarma.example.com.alarmaph;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyService extends Service {

    ArrayList<String> firstname = new ArrayList<>();
    ArrayList<String> lastname = new ArrayList<>();
    ArrayList<String> age = new ArrayList<>();
    ArrayList<String> gender = new ArrayList<>();
    ArrayList<String> contactNo = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startServiceWithNotification();
        }
        FirebaseAuth.AuthStateListener authStateListener;
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    startServiceWithNotification();
                }
            }
        };


        /*View v;
        */
        return START_STICKY;
    }

    public void PushNotification(String title, String content) {
        NotificationManager nm = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        Intent notificationIntent = new Intent(this, browseactivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        //set
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.emergency_icon);
        builder.setContentText(content);
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        nm.notify((int)System.currentTimeMillis(),notification);
    }
    void startServiceWithNotification(){
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        if(currentUser !=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference followsRef = database.getReference("mobileUsers/"+uid+"/incidentReports");
            followsRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key = dataSnapshot.getKey();
                    String seen= dataSnapshot.child("seen").getValue(String.class);
                    if(seen.equals("false")){
                        String type = dataSnapshot.child("reportType").getValue(String.class);
                        switch(type){
                            case "Medical":{
                                String condition = dataSnapshot.child("condition").getValue(String.class);
                                String name = dataSnapshot.child("name").getValue(String.class);

                                if(condition.equals("Injury") || condition.equals("Trauma")){
                                    PushNotification("Emergency Report","There is a need of medical help with your Guardian List: "+name+", Complain: "+condition);
                                }else{
                                    PushNotification("Emergency Report","Your Guardian List: "+name+" is involved in an accident and is labeled: "+condition);
                                }
                                break;
                            }
                            case "Crime Emergency":{
                                String name = dataSnapshot.child("name").getValue(String.class);
                                String condition = dataSnapshot.child("condition").getValue(String.class);
                                PushNotification("Crime Incident","Your Guardian Lisit : "+name+" is involved in a crime incident "+condition);
                                break;
                            }
                            case "Crime":{
                                String remarks = dataSnapshot.child("remarks").getValue(String.class);
                                PushNotification("Crime Incident","There is a crime incident reported near your Guardian Lisit Location: "+remarks);
                                break;
                            }
                            case "Fire":{
                                String remarks = dataSnapshot.child("remarks").getValue(String.class);
                                PushNotification("Fire Incident","There is a fire incident reported near your Guardian Lisit Location: "+remarks);
                                break;
                            }
                            case "Guardian":{
                                String name = dataSnapshot.child("nameGuard").getValue(String.class);
                                String types = dataSnapshot.child("reportedInci").getValue(String.class);
                                PushNotification("Guardian Notification",name+" might be in trouble for he/she called for help with "+types);
                                break;
                            }
                        }
                        followsRef.child(key).child("seen").setValue("true");
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
    }

    void showMsg(String key){
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
    }

}
