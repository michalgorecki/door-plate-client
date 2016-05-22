package com.thesis.guras.doorplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddEventActivity extends AppCompatActivity {
    private Button addEventButton;
    private Spinner dayOfTheWeekSpinner;
    private EditText eventName;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private MDBHandler mdbHandler = new MDBHandler(this);
    private String DEBUG_TAG = "AddEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        addEventButton = (Button) findViewById(R.id.addEventButton);
        eventName = (EditText) findViewById(R.id.eventNameEdiText);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

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
        eventName.setText("");
        Intent intent = new Intent(this,ManageEventsActivity.class);
        AddEventActivity.this.startActivity(intent);
        Log.d(DEBUG_TAG,"onClickAddEvent()");
    }
}
