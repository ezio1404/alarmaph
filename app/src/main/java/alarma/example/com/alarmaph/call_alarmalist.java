package alarma.example.com.alarmaph;

/**
 * Created by Carlo on 2/14/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class call_alarmalist extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> agencyname;
    private final ArrayList<String> imageId;
    private final ArrayList<String> uid;
    private final Context c;
    TextView a_info,alarm,alarm2;

    public call_alarmalist(Activity context,
                           ArrayList<String> agencyname, ArrayList<String> imageId, ArrayList<String> uid) {
        super(context, R.layout.list_browse, agencyname);
        this.c  = context;
        this.context = context;
        this.agencyname = agencyname;
        this.imageId = imageId;
        this.uid = uid;

    }



    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_browse, null, true);


        TextView txtAgency = (TextView) rowView.findViewById(R.id.a_agency);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.a_image);
        a_info = (TextView) rowView.findViewById(R.id.a_info);
        a_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Info "+position, Toast.LENGTH_SHORT).show();


            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.zalarma_calls);


            final WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            lp1.copyFrom(dialog.getWindow().getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp1.gravity = Gravity.CENTER;

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("userAccounts/"+uid.get(position));

                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String key = dataSnapshot.getKey();

                        if(key.equals("contact")){
                            dialog.getWindow().setAttributes(lp1);
                            dialog.setTitle("HOTLINES");
                            final String ll =  dataSnapshot.child("landlineCont").getValue(String.class);
                            final String cp =  dataSnapshot.child("mobileCont").getValue(String.class);
                            alarm = (TextView) dialog.findViewById(R.id.c_alarm);
                            alarm.setText(ll);
                            alarm2 = (TextView) dialog.findViewById(R.id.c_alarm_2);
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

            }

        });


        Bitmap imageThis = null;
        try {
            imageThis = new sync_task().execute(imageId.get(position)).get();
            imageView.setImageBitmap(imageThis);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        txtAgency.setText(agencyname.get(position));

        return rowView;
    }
    public void callPhone(String contact){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+contact));
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        c.startActivity(callIntent);
    }
}