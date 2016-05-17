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

    private ArrayList<ScanResult> currentWifiList = new ArrayList<ScanResult>();
    private String DEBUG_TAG = "SendMessageActivity";
    MDBHandler mdbHandler = new MDBHandler(this);
    EditText editText;
    Button chooseLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        editText = (EditText) findViewById(R.id.sendToLocationEditText);
        chooseLocationButton = (Button) findViewById(R.id.chooseLocationButton);

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            Log.d(DEBUG_TAG, "Intent extras was not null");
            String selectedLocationExtra = intentExtras.getString("LocationName");
            editText.setText(selectedLocationExtra);
        }
    }

    public void goToLocationsOnClick(View v) {
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        SendMessageActivity.this.startActivity(intent);
    }
}


