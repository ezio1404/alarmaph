package alarma.example.com.alarmaph;

/**
 * Created by Carlo on 12/24/2017.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class emergencylist extends ArrayAdapter<String>{

private final Activity context;
private final ArrayList<Integer> imageId;
private final ArrayList<String> agencyname;
private final ArrayList<String> ewhat;
private final ArrayList<String> ewhere;
private final ArrayList<String> ewhen;
private final ArrayList<String> reporter;
private final ArrayList<String> report;
private final ArrayList<String> edesc;

public emergencylist(Activity context,
                     ArrayList<String> agencyname, ArrayList<String> ewhat, ArrayList<String> ewhere, ArrayList<String> ewhen, ArrayList<String> edesc, ArrayList<Integer> imageId, ArrayList<String> reporterId, ArrayList<String> reportId) {
        super(context, R.layout.list_emergency, agencyname);
        this.context = context;
        this.agencyname = agencyname;
        this.ewhat = ewhat;
        this.ewhen = ewhen;
        this.ewhere = ewhere;
        this.edesc = edesc;
        this.imageId = imageId;
        this.report = reportId;
        this.reporter = reporterId;
        }
@Override
public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_emergency, null, true);


        TextView txtAgency = (TextView) rowView.findViewById(R.id.txtagency);
        TextView txtWhat = (TextView) rowView.findViewById(R.id.what);
        TextView txtWhen = (TextView) rowView.findViewById(R.id.when);
        TextView txtWhere = (TextView) rowView.findViewById(R.id.where);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.desc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_agency);

        txtAgency.setText(agencyname.get(position));
        txtWhat.setText(ewhat.get(position));
        txtWhen.setText(ewhen.get(position));
        txtWhere.setText(ewhere.get(position));
        txtDesc.setText(edesc.get(position));
        imageView.setImageResource(imageId.get(position));
        return rowView;
        }
}
