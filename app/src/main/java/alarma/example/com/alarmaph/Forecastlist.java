package alarma.example.com.alarmaph;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Forecastlist extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] days;
    private final String[] weather;
    private final String[] degree;
    private final Integer[] imageId;
    public Forecastlist(Activity context,
                      String[] days, String[] weather, String[]degree, Integer[] imageId) {
        super(context, R.layout.list_forecast, days);
        this.context = context;
        this.days = days;
        this.weather = weather;
        this.degree = degree;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_forecast, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtWeather = (TextView) rowView.findViewById(R.id.txt1);
        TextView txtDegree = (TextView) rowView.findViewById(R.id.txt2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtDegree.setText(degree[position]);
        txtWeather.setText(weather[position]);
        txtTitle.setText(days[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
