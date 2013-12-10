package edu.berkeley.cs160.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.berkeley.cs160.tagit.R;
import edu.berkeley.cs160.tagit.util.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 11/24/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoxArrayAdapter extends ArrayAdapter<Box> {
    private ArrayList<Box> objects;
    private Context context;

    public BoxArrayAdapter(Context context, int resource, ArrayList<Box> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.box_list_item, parent, false);

        ImageView openBox  = (ImageView)row.findViewById(R.id.contents_image);
        TextView  location = (TextView) row.findViewById(R.id.location_text);
        TextView  contents = (TextView) row.findViewById(R.id.contents_text);

        Box box = objects.get(position);
        openBox.setImageBitmap(box.getContentsBitmap(60,60));
        location.setText("Location: " + box.getLocation());
        contents.setText("Contents: " + box.getContentsString());

        return row;
    }
}
