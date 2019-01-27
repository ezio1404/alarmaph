package alarma.example.com.alarmaph;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Carlo on 1/1/2018.
 */

public class postlist extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> pname;
    private final ArrayList<String> pdesc;
    private final ArrayList<String> postID;
    private final String admin;
    private final String imgPURL;

    public postlist(Activity context,
                    ArrayList<String> pname, ArrayList<String> pdesc, String imagePURL, ArrayList<String> postKey, String adminKey) {
        super(context, R.layout.list_posts, pname);
        this.context = context;
        this.pname = pname;
        this.pdesc = pdesc;
        this.postID = postKey;
        this.admin = adminKey;
        this.imgPURL = imagePURL;
    }

    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.list_posts, null, true);

        TextView txtPname= (TextView) rowView.findViewById(R.id.pname);
        TextView txtPdesc = (TextView) rowView.findViewById(R.id.pdesc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_post);

        txtPname.setText(pname.get(position));
        txtPdesc.setText(pdesc.get(position));

        try {
            Bitmap thisImage = new sync_task().execute(imgPURL).get();
            imageView.setImageBitmap(thisImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: view + comment + see likes

                Intent intent = new Intent();
                intent.setClass(rowView.getContext(), post_spec.class);
                String key = postID.get(position).toString();
                String pnames = pname.get(position).toString();
                String pdescs = pdesc.get(position).toString();
                intent.putExtra("key",key);
                //intent.putExtra("key",postID.get(position).toString());


                intent.putExtra("adminKey",admin);
                intent.putExtra("adminName",pnames);
                intent.putExtra("postDesc",pdescs);
                intent.putExtra("imgPURL",imgPURL);
                rowView.getContext().startActivity(intent);

            }
        });
        return rowView;
    }
    public void showMsg(String key){
        Toast.makeText(context.getApplicationContext(), key.toString(),Toast.LENGTH_SHORT).show();
    }

}
