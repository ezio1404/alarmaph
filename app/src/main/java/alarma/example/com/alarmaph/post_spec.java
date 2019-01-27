package alarma.example.com.alarmaph;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by joshua on 2/13/2018.
 */



public class post_spec extends Activity {

    String postKey;
    String adminKey;
    String adminName;
    String postDescs;
    String imgPURL;

    ListView commentList;
    int c = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_spec);

        Bundle bundle = getIntent().getExtras();
        postKey = bundle.getString("key");
        adminKey = bundle.getString("adminKey");
        adminName = bundle.getString("adminName");
        postDescs = bundle.getString("postDesc");
        imgPURL = bundle.getString("imgPURL");



        TextView postName= (TextView) findViewById(R.id.postSpecName);
        TextView postDesc = (TextView) findViewById(R.id.postSpecDesc);
        ImageView adminImg = (ImageView) findViewById(R.id.imgPost);

        final ImageButton like = (ImageButton) findViewById(R.id.likeButton);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c==1) {
                    like.setImageResource(R.drawable.liked);
                    c = 0;
                }
                else{
                    like.setImageResource(R.drawable.like);
                    c=1;
                }
            }
        });

        postName.setText(adminName);
        postDesc.setText(postDescs);
        try {
            Bitmap thisImage = new sync_task().execute(imgPURL).get();
            adminImg.setImageBitmap(thisImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

/*
        like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            String likeAction = (String) like.getText();

            if(likeAction.equals("Like")){
                showMsg("like");
                like.setText("Unlike");
            }else{
                showMsg("unlike");
                like.setText("Like");
            }

            }
        });
*/
        try {
            ArrayList[] collection = new getComments().execute(postKey,adminKey,adminName,postDescs,imgPURL).get();
            imagelist adapter = new imagelist(post_spec.this, collection[0], collection[1], collection[2]);
            commentList=(ListView)findViewById(R.id.commentViewList);
            commentList.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }


}