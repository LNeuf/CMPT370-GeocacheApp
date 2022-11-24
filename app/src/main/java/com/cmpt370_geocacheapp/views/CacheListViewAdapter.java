package com.cmpt370_geocacheapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt370_geocacheapp.R;

import java.util.ArrayList;

public class CacheListViewAdapter extends BaseAdapter {

    private final ArrayList<CacheListItem> items;
    private final Context context;

    public CacheListViewAdapter(Context context, ArrayList<CacheListItem> items) {
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
                    inflate(R.layout.cache_listview, parent, false);

            holder.image = convertView.findViewById(R.id.image_item);
            holder.nameText = convertView.findViewById(R.id.item_name);
            holder.descriptionText = convertView.findViewById(R.id.item_diff_terrain_id_summary);
            holder.distanceText = convertView.findViewById(R.id.item_distance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // get current item to be displayed
        CacheListItem currentItem = items.get(position);

        // assign item data to view
        // TODO: Assign an image
        holder.nameText.setText(currentItem.getCacheName());
        holder.descriptionText.setText(currentItem.getCacheDescription());
        holder.distanceText.setText(currentItem.getCacheDistance());


        // returns the view for the current row
        return convertView;
    }

    class ViewHolder {
        TextView nameText;
        TextView descriptionText;
        TextView distanceText;
        ImageView image;
    }
}


