package com.example.coffee2_app.Admin_ui.browseEvents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.coffee2_app.Event;
import com.example.coffee2_app.R;

import java.util.List;

public class BrowseEventsAdapter extends BaseAdapter {

    private final Context context;
    private final List<Event> eventList;

    public BrowseEventsAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.event_image);
        Event event = eventList.get(position);

        // Load event image using Glide
        Glide.with(context).load(event.getImageUrl()).into(imageView);

        return convertView;
    }
}
