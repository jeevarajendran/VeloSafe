package com.techgenie.velosafe;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
    Class: CustomisedAdapterForSafePlacesList
    Purpose: Creating  adapter for the safe places list view

 */

public class CustomisedAdapterForSafePlacesList extends BaseAdapter {
    private static ArrayList<ListDefinitions> list;
    private LayoutInflater mInflater;
    public CustomisedAdapterForSafePlacesList(Context context, ArrayList<ListDefinitions> set) {
        list = set;
        mInflater = LayoutInflater.from(context);
    }

    /*
    Method getcount: returns list
 */

    public int getCount() {
        return list.size();
    }

    /*
    Method getItem: returns item at position
 */

    public Object getItem(int position) {
        return list.get(position);
    }


    /*
    Method getItemId: returns id of an item
     */

    public long getItemId(int position) {
        return position;
    }

    /*
Method getView: adds color to the
 */

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LinearLayout rowLayout = null;
        if (convertView == null) {
            rowLayout = (LinearLayout) mInflater.inflate(
                    R.layout.content_safe_places_list, parent, false);
            holder = new ViewHolder();
            holder.Places = (TextView) rowLayout.findViewById(R.id.Places);
            holder.Distance = (TextView) rowLayout
                    .findViewById(R.id.Distance);
            holder.Places.setTextColor(Color.BLUE);
            holder.Distance.setTextColor(Color.GRAY);
            AlphaAnimation alpha = new AlphaAnimation(0.1F,0.6F);
            alpha.setDuration(2000);
            alpha.setFillAfter(true);
            rowLayout.startAnimation(alpha);
        }
        else {
            rowLayout= (LinearLayout) convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        holder.Places.setText(list.get(position).getPlaces());
        holder.Distance.setText(list.get(position).getDistance());

        if(list.get(position).getColour().equals("ORANGE"))
        {
            rowLayout.setBackgroundColor(Color.YELLOW);
        }
        else if(list.get(position).getColour().equals("GREEN")){
            rowLayout.setBackgroundColor(Color.GREEN);        }
        else
        {
            rowLayout.setBackgroundColor(Color.RED);

        }
        rowLayout.setTag(holder);
        return rowLayout;
    }

    static class ViewHolder {
        TextView Places;
        TextView Distance;
    }
}
