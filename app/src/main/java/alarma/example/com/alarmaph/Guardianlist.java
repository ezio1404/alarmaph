package alarma.example.com.alarmaph;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Carlo on 12/31/2017.
 */

public class Guardianlist extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> gname;
    private final ArrayList<String> gage;
    private final ArrayList<String> gemer;
    private final ArrayList<Integer> gimage;
    Button delg;
    public Guardianlist(Activity context, ArrayList<String> gname, ArrayList<String> gage, ArrayList<String> gemer, ArrayList<Integer> gimage) {
        super(context, R.layout.list_guardian, gname);

        this.context = context;
        this.gname = gname;
        this.gage = gage;
        this.gemer = gemer;
        this.gimage = gimage;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(gemer.get(position).equals("")){
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.list_guardian, null, true);

            TextView txtgName = (TextView) rowView.findViewById(R.id.gn);
            TextView txtgAge = (TextView) rowView.findViewById(R.id.gg);
            TextView name = (TextView) rowView.findViewById(R.id.thisName);
            TextView age = (TextView) rowView.findViewById(R.id.thisAge);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.guard);
            delg =  (Button) rowView.findViewById(R.id.delguardian);
            delg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"DELETE", Toast.LENGTH_SHORT).show();
                }
            });

            txtgName.setText(gname.get(position));
            txtgAge.setText(gage.get(position));
            name.setText("Address: ");
            age.setText("Mark: ");

            TableRow tr2 = (TableRow) rowView.findViewById(R.id.row2);

            tr2.setVisibility(View.GONE);

            imageView.setImageResource(gimage.get(position));
            return rowView;
        }else{
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.list_guardian, null, true);

            TextView txtgName = (TextView) rowView.findViewById(R.id.gn);
            TextView txtgAge = (TextView) rowView.findViewById(R.id.gg);
            TextView txtgEmer = (TextView) rowView.findViewById(R.id.ge);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.guard);
            delg =  (Button) rowView.findViewById(R.id.delguardian);
            delg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"DELETE", Toast.LENGTH_SHORT).show();
                }
            });


            txtgName.setText(gname.get(position));
            txtgAge.setText(gage.get(position));
            txtgEmer.setText(gemer.get(position));

            imageView.setImageResource(gimage.get(position));
            return rowView;
        }



    }


}
