package alarma.example.com.alarmaph;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joshua on 2/27/2018.
 */

public class events_list extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> eventtitle;
    private final ArrayList<String> eventwhat;
    private final ArrayList<String> eventwhen;
    private final ArrayList<String> eventwehere;
    private final ArrayList<String> eventwho;
    private final ArrayList<String> eventdesc;

    public events_list(Activity context,
                       ArrayList<String> eventtitle, ArrayList<String> eventwhat, ArrayList<String> eventwhen, ArrayList<String> eventwehere, ArrayList<String> eventwho, ArrayList<String> eventdesc) {
        super(context,R.layout.list_events, eventtitle);
        this.context = context;
        this.eventtitle = eventtitle;
        this.eventwhat = eventwhat;
        this.eventwhen = eventwhen;
        this.eventwehere = eventwehere;
        this.eventwho  =  eventwho;
        this.eventdesc = eventdesc;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_events, null, true);


        TextView namee = (TextView) rowView.findViewById(R.id.etitle);
        TextView whate = (TextView) rowView.findViewById(R.id.ewhat);
        TextView whene = (TextView) rowView.findViewById(R.id.ewhen);
        TextView wheree = (TextView) rowView.findViewById(R.id.ewhere);
        TextView whoe = (TextView) rowView.findViewById(R.id.ewho);
        TextView desce = (TextView) rowView.findViewById(R.id.edesc);

        namee.setText(eventtitle.get(position));
        whate.setText(eventwhat.get(position));
        whene.setText(eventwhen.get(position));
        wheree.setText(eventwehere.get(position));
        whoe.setText(eventwho.get(position));
        desce.setText(eventdesc.get(position));

        return rowView;
    }
}

