package alarma.example.com.alarmaph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Manuallist extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> manual;
    private final ArrayList<String> author;
    private final ArrayList<Integer> imageId;
    private final ArrayList<String> link;
    private Context c;
    public Manuallist(Activity context, ArrayList<String> manual, ArrayList<String> author, ArrayList<Integer> imageId,ArrayList<String> link) {
        super(context, R.layout.list_manual, manual);
        c = context;
        this.context = context;
        this.manual= manual;
        this.author = author;
        this.link = link;
        this.imageId = imageId;

    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_manual, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.manualtxt);
        TextView txtAuthor = (TextView) rowView.findViewById(R.id.manualtxt1);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.manualimg);

        txtTitle.setText(manual.get(position));
        txtAuthor.setText(author.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: view + comment + see likes

                Intent intent = new Intent(c, pdf_webview.class);
                //if you want to send data to called activity uncomment next line
                intent.putExtra("title", manual.get(position));
                intent.putExtra("author", author.get(position));
                intent.putExtra("link", link.get(position));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);


            }
        });
        imageView.setImageResource(imageId.get(position));
        return rowView;
    }
    void goTo(){

    }
}
