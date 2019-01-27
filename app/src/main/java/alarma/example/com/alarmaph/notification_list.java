package alarma.example.com.alarmaph;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by joshua on 2/27/2018.
 */

public class notification_list  extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] infodesc;
    private final Integer[] infoimg;

    public notification_list(Activity context,
                        String[] infodesc, Integer[] infoimg) {
        super(context,R.layout.list_notification, infodesc);
        this.context = context;
        this.infodesc = infodesc;
        this.infoimg = infoimg;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_notification, null, true);


        TextView notinfop = (TextView) rowView.findViewById(R.id.notinfo);
        ImageView  notimgp = (ImageView) rowView.findViewById(R.id.notimg);

        notinfop.setText(infodesc[position]);
        notimgp.setImageResource(infoimg[position]);
        return rowView;
    }
}

