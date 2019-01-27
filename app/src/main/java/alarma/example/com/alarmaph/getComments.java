package alarma.example.com.alarmaph;

import android.os.AsyncTask;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by joshua on 2/25/2018.
 */

public class getComments extends AsyncTask<String, Void, ArrayList<String>[]> {
    @Override
    protected ArrayList<String>[] doInBackground(String... strings) {
        String adminKey = strings[1];
        String postKey = strings[0];
        String cLogo = strings[4];
        String cName = strings[2];
        String cDesc = strings[3];

        final ArrayList<String> commentorPURL = new ArrayList<>();
        final ArrayList<String> commentorName= new ArrayList<>();
        final ArrayList<String> commentorComment = new ArrayList<>();
        final ArrayList[] collection = new ArrayList[3];
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference("userAccounts/"+adminKey+"/posts/"+postKey+"/comments");

        mDatabase.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid= (String) dataSnapshot.child("UID").getValue(String.class);

                String key = (String) dataSnapshot.getKey();
                String cName = (String) dataSnapshot.child("commentAlias").getValue(String.class);
                String cDesc = (String) dataSnapshot.child("commentDesc").getValue(String.class);
                String cLogo = (String) dataSnapshot.child("commentLogo").getValue(String.class);
                commentorPURL.add(cLogo);
                commentorName.add(cName);
                commentorComment.add(cDesc);
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
        collection[0] = commentorPURL;
        collection[1] = commentorName;
        collection[2] = commentorComment;

        return collection;
    }
}
