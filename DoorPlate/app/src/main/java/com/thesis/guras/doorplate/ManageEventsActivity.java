package com.thesis.guras.doorplate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class ManageEventsActivity extends AppCompatActivity {
    ListView eventsListView;
    Button addEventButton;
    MDBHandler mdbHandler = new MDBHandler(this);
    Cursor eventsCursor;
    EventCursorAdapter eventCursorAdapter;
    String DEBUG_TAG = "ManageEventsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);
        eventsListView = (ListView) findViewById(R.id.displayEventsListView);
        addEventButton = (Button) findViewById(R.id.launchAddEventButton);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        mdbHandler.open();
        eventsCursor = mdbHandler.getAllEvents();
        if(eventsCursor != null){
            eventCursorAdapter = new EventCursorAdapter(this,eventsCursor,0);
            eventsListView.setAdapter(eventCursorAdapter);
        }
        mdbHandler.close();

        //OnClick for listview
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(DEBUG_TAG, "onItemClick()");
                //item is selected from the cursor to get necessary data
                Log.d(DEBUG_TAG, "ListView count: " + eventsListView.getCount());
                Log.d(DEBUG_TAG, "foundPatternsCursor count: " + eventsCursor.getCount());
                if (position >= eventsCursor.getCount()) {
                    Log.d(DEBUG_TAG, "Unable to access element " + position + ", it does not exist in the foundPatternsCursor. Cursor count: " + eventsCursor.getCount());
                }
                eventCursorAdapter.notifyDataSetChanged();
                eventsCursor.moveToPosition(position);
                final String selectedItemName = eventsCursor.getString(1);
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageEventsActivity.this);
                builder.setTitle("Selected event").setMessage("Do you want to delete the event?");
                builder.setMessage(selectedItemName);

                //Use event onClick
                builder.setPositiveButton("Use",new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dlg, int x) {
                       Intent intent = new Intent(ManageEventsActivity.this,SendMessageActivity.class);
                       String eventData = eventsCursor.getString(1)+" "+eventsCursor.getString(2);
                       intent.putExtra("EventData",eventData);
                       ManageEventsActivity.this.startActivity(intent);

                   }
                });
                //Delete event onClick
                builder.setNeutralButton("Delete event", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int x) {
                        mdbHandler.open();
                        Log.d(DEBUG_TAG,"Trying to delete event no."+eventsCursor.getInt(0));
                        if(!mdbHandler.deleteEvent(eventsCursor.getInt(0))){
                            Log.d(DEBUG_TAG,"Failed to delete event no. "+eventsCursor.getInt(0));
                        }
                        mdbHandler.open();
                        eventsCursor = mdbHandler.getAllEvents();
                        if(eventsCursor != null){
                            eventCursorAdapter = new EventCursorAdapter(ManageEventsActivity.this,eventsCursor,0);
                            eventCursorAdapter.notifyDataSetChanged();
                            eventsListView.setAdapter(eventCursorAdapter);
                        }else{
                            eventCursorAdapter = null;
                            eventsListView.setAdapter(eventCursorAdapter);
                        }
                        mdbHandler.close();
                    }
                });

                //Cancel onClick
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int x) {
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onClickGoToAddEvent(View v){
        Intent intent = new Intent(this,AddEventActivity.class);
        ManageEventsActivity.this.startActivity(intent);
    }
}