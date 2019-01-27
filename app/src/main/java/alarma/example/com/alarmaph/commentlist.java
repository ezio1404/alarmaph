package alarma.example.com.alarmaph;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by joshua on 2/14/2018.
 */

public class commentlist extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> pname;
    private final ArrayList<String> imagePURL;

    public commentlist(Activity context, ArrayList<String> pname, ArrayList<String> imagePURL) {
        super(context, R.layout.image_value_list, pname);

        this.context = context;
        this.pname = pname;
        this.imagePURL = imagePURL;

    }

    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.list_posts, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imagePostDisp);

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
