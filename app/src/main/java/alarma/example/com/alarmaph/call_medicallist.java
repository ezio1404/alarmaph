package alarma.example.com.alarmaph;

/**
 * Created by joshua on 2/18/2018.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class call_medicallist extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> agencyname;
    private final ArrayList<Integer> imageId;
    TextView mcall;


    public call_medicallist(Activity context,
                            ArrayList<String> agencyname, ArrayList<Integer> imageId) {
        super(context, R.layout.list_browse, agencyname);
        this.context = context;
        this.agencyname = agencyname;

        this.imageId = imageId;

    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_browse_others, null, true);


        TextView txtAgency = (TextView) rowView.findViewById(R.id.a_agency);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.a_image);
        mcall = (TextView) rowView.findViewById(R.id.m_call);
        mcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Call "+position, Toast.LENGTH_SHORT).show();
            }
        });


        txtAgency.setText(agencyname.get(position));
        imageView.setImageResource(imageId.get(position));
        return rowView;
    }
}