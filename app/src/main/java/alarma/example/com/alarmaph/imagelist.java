package alarma.example.com.alarmaph;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by joshua on 2/14/2018.
 */

public class imagelist  extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> pname;
    private final ArrayList<String> imagePURL;
    private final ArrayList<String> commentDesc;

    public imagelist(Activity context, ArrayList<String> imagePURL, ArrayList<String> pname, ArrayList<String> commentDesc) {
        super(context, R.layout.list_posts, pname);

        this.context = context;
        this.pname = pname;
        this.imagePURL = imagePURL;
        this.commentDesc = commentDesc;
    }

    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.list_posts, null, true);

        TextView txtPname= (TextView) rowView.findViewById(R.id.pname);
        TextView txtPdesc = (TextView) rowView.findViewById(R.id.pdesc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_post);

        txtPname.setText(pname.get(position));
        txtPdesc.setText(commentDesc.get(position));
        try {
            Bitmap thisImage = new sync_task().execute(imagePURL.get(position)).get();
            imageView.setImageBitmap(thisImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return rowView;
    }

}
