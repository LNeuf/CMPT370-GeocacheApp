package com.cmpt370_geocacheapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListItem> items;
    private Context context;

    public ListViewAdapter(Context context,ArrayList<ListItem> items){
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // inflate the layout for each list row
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview, parent, false);

            holder.image = convertView.findViewById(R.id.image_item);
            holder.nameText = convertView.findViewById(R.id.item_name);
            holder.difficultyText = convertView.findViewById(R.id.item_difficulty);
            holder.distanceText = convertView.findViewById(R.id.item_distance);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // get current item to be displayed
        ListItem currentItem = items.get(position);

        // returns the view for the current row
        return convertView;
    }

    class ViewHolder{
        TextView nameText;
        TextView difficultyText;
        TextView distanceText;
        ImageView image;
    }
}


