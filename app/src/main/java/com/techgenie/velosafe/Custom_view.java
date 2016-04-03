package com.techgenie.velosafe;
/*import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;*/

/**
 * Created by user on 19-03-2016.
 */
/*public class Custom_view  extends ArrayAdapter{
    private final Context context;
    private String[] values;

    public Custom_view(Context context,ArrayAdapter<String> values) {
        super(context, R.layout.content_safe_places_list, (List) values);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.content_safe_places_list, parent, false);
        ListView tv = (ListView) rowView.findViewById(R.id.listView);
        String item_value = values[position];

        //tv.setText(item_value);

        if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#BCF7F0"));
        }
        return rowView;
    }
}*/
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class Custom_view extends SimpleAdapter {
    private int[] colors = new int[]{0x30FF0000, 0x300000FF};

    public Custom_view(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int colorPos = position % colors.length;
        view.setBackgroundColor(colors[colorPos]);
        return view;

    }
}