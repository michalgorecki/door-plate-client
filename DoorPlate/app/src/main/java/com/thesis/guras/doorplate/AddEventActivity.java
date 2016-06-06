package com.thesis.guras.doorplate;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {
    private Button addEventButton;
    private Spinner dayOfTheWeekSpinner;
    private EditText eventName;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private MDBHandler mdbHandler = new MDBHandler(this);
    private String DEBUG_TAG = "AddEventActivity";
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }

        addEventButton = (Button) findViewById(R.id.addEventButton);
        eventName = (EditText) findViewById(R.id.eventNameEdiText);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

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

    public void onClickAddEvent(View v){
        Log.d(DEBUG_TAG,"onClickAddEvent()");
        String mName = eventName.getText().toString();

        int mHour = timePicker.getCurrentHour();
        int mMinutes = timePicker.getCurrentMinute();
        int mDay = datePicker.getDayOfMonth();
        int mMonth = datePicker.getMonth();
        int mYear = datePicker.getYear();

        if(mName != null && mName !=""){
            mdbHandler.open();
            Log.d(DEBUG_TAG,"Inserted record with id: "+mdbHandler.insertEvent(mName,mDay,mMonth,mYear,mHour,mMinutes));
            mdbHandler.close();
        }
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.clear();
        mCalendar.set(Calendar.MONTH,mMonth);
        mCalendar.set(Calendar.YEAR,mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH,mDay);
        mCalendar.set(Calendar.HOUR,mHour-1);
        mCalendar.set(Calendar.MINUTE,mMinutes+30);
        mCalendar.set(Calendar.SECOND,0);
        mCalendar.set(Calendar.MILLISECOND,0);

        makeNotification(mCalendar,mName,"You have an upcoming event!");

        eventName.setText("");
        Intent intent = new Intent(this,ManageEventsActivity.class);
        Log.d(DEBUG_TAG,"onClickAddEvent()");
        AddEventActivity.this.startActivity(intent);

    }
    public void makeNotification (Calendar calendar, String title, String message)
    {   Log.d(DEBUG_TAG,"makeNotification()");
        Intent myIntent = new Intent(this, MyBroadcastReceiver.class);
        myIntent.putExtra("Message",message);
        myIntent.putExtra("Title",title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1253, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Log.d(DEBUG_TAG,String.valueOf(calendar.getTimeInMillis()));
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d(DEBUG_TAG,"makeNotification()");
    }
}
