package com.thesis.guras.doorplate;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by guras on 22.05.16.
 */
public class EventCursorAdapter extends CursorAdapter {
    public EventCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = (TextView) view.findViewById(R.id.eventNameTextView);
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);

        nameView.setText(cursor.getString(1));
        dateTextView.setText(cursor.getString(2));
    }
}
