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
 * Created by joshua on 2/25/2018.
 */

public class citizen_list extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> citname;
    private final ArrayList<String> citimg;
    private final ArrayList<String> citdesc;
    private final ArrayList<String> citpimg;

    public citizen_list(Activity context,
                        ArrayList<String> citname, ArrayList<String> citdesc, ArrayList<String> citimg, ArrayList<String> citpimg) {
        super(context,R.layout.list_citizen, citname);
        this.context = context;
        this.citname = citname;
        this.citpimg = citpimg;
        this.citdesc = citdesc;
        this.citimg = citimg;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_citizen, null, true);


        TextView namecit = (TextView) rowView.findViewById(R.id.citname);
        TextView desccit = (TextView) rowView.findViewById(R.id.citdesc);
        ImageView imgcit = (ImageView) rowView.findViewById(R.id.citimg);
        ImageView  imgpcit = (ImageView) rowView.findViewById(R.id.imgpostcit);

        namecit.setText(citname.get(position));
        desccit.setText(citdesc.get(position));
        try {
            Bitmap userImage = new sync_task().execute(citimg.get(position)).get();
            imgcit.setImageBitmap(userImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(!citpimg.get(position).equals("")){
            try {
                Bitmap postImage = new sync_task().execute(citpimg.get(position)).get();
                imgpcit.setImageBitmap(postImage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }else{
            imgpcit.setImageResource(R.drawable.linepar);
        }



        return rowView;
    }
}
