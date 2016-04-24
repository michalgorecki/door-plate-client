package com.thesis.guras.doorplate;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by guras on 19.04.16.
 */
public class PatternCursorAdapter extends CursorAdapter {

    public PatternCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.list_item_pattern,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView nameView = (TextView) view.findViewById(R.id.name_textView);
        TextView ssid1View = (TextView) view.findViewById(R.id.ssid1_textView);
        TextView ssid2View = (TextView) view.findViewById(R.id.ssid2_textView);
        TextView ssid3View = (TextView) view.findViewById(R.id.ssid3_textView);
        TextView ssid4View = (TextView) view.findViewById(R.id.ssid4_textView);
        TextView ssid5View = (TextView) view.findViewById(R.id.ssid5_textView);

        /*if(cursor.moveToFirst()){
            while(cursor.moveToNext()){*/
                nameView.setText(cursor.getString(1));
                ssid1View.setText(cursor.getString(2));
                ssid2View.setText(cursor.getString(4));
                ssid3View.setText(cursor.getString(6));
                ssid4View.setText(cursor.getString(8));
                ssid5View.setText(cursor.getString(10));
            /*}
        }*/
    }
}
