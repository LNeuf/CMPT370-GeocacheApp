package com.cmpt370_geocacheapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmpt370_geocacheapp.R;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {

    private final ArrayList<CommentListItem> items;
    private final Context context;

    public CommentListViewAdapter(Context context, ArrayList<CommentListItem> items) {
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
                    inflate(R.layout.rating_listview, parent, false);

            holder.ratingRating = convertView.findViewById(R.id.rating_rating);
            holder.ratingContents = convertView.findViewById(R.id.rating_contents);
            holder.ratingAuthor = convertView.findViewById(R.id.rating_author);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // get current item to be displayed
        CommentListItem currentItem = items.get(position);

        // assign item data to view
        String ratingString = "Cache Rating: " + currentItem.getRating() + " / 5";
        holder.ratingRating.setText(ratingString);
        holder.ratingContents.setText(currentItem.getContents());
        holder.ratingAuthor.setText(currentItem.getAuthor());


        // returns the view for the current row
        return convertView;
    }

    class ViewHolder {
        TextView ratingRating;
        TextView ratingContents;
        TextView ratingAuthor;
    }
}


