package com.thesis.guras.doorplate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SendMessageActivity extends AppCompatActivity {
    public Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult firstResult, ScanResult secondResult) {
            return (firstResult.level > secondResult.level ? -1 :
                    (firstResult.level == secondResult.level ? 0 : 1));
        }
    };
    private String currentMessage="";
    private ArrayList<ScanResult> currentWifiList = new ArrayList<ScanResult>();
    private String DEBUG_TAG = "SendMessageActivity";
    //MDBHandler mdbHandler = new MDBHandler(this);
    EditText editText;
    Button chooseLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        editText = (EditText) findViewById(R.id.sendToLocationEditText);
        chooseLocationButton = (Button) findViewById(R.id.chooseLocationButton);
        if(savedInstanceState != null){
            Log.d(DEBUG_TAG,"Restoring previous instance state");
            currentMessage = savedInstanceState.getString("CurrentMessage");
            editText.setText(currentMessage);
        }

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            Log.d(DEBUG_TAG, "Intent extras was not null");
            String selectedLocationExtra = intentExtras.getString("LocationName");
            String previousMessage = intentExtras.getString("PreviousMessage");
            editText.setText(previousMessage+" "+selectedLocationExtra);
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {   Log.d(DEBUG_TAG,"onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        currentMessage = savedInstanceState.getString("CurrentMessage"); //retrieve your data using its corresponding key
        Log.d(DEBUG_TAG,"CurrentMessage restored from savedInstanceState: "+savedInstanceState.getString("CurrentMessage"));
        Log.d(DEBUG_TAG,"onRestoreInstanceState()");
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        Log.d(DEBUG_TAG,"onSaveInstanceState()");
        String savedString = editText.getText().toString();
        savedInstanceState.putString("CurrentMessage",savedString);

        super.onSaveInstanceState(savedInstanceState);
        Log.d(DEBUG_TAG,"onSaveInstanceState()");
    }
    public void goToLocationsOnClick(View v) {
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        String message = editText.getText().toString();
        if(message != null){
            intent.putExtra("CurrentMessage",message);
        }

        SendMessageActivity.this.startActivity(intent);
    }
    public void sendMessageOnClick(View v){
        Log.d(DEBUG_TAG,"sendMessageOnClick");
        String message = editText.getText().toString();
        if(message != null){
            Log.d(DEBUG_TAG,"Message to be sent: "+message);
        }
        editText.setText("");
        Log.d(DEBUG_TAG,"sendMessageOnClick");
    }
}


