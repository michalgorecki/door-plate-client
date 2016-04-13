package com.thesis.guras.doorplate;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SaveLocationActivity extends AppCompatActivity {
    private ArrayList<ScanResult> currentWifiList = new ArrayList<ScanResult>();
    private MDBHandler mDbHandler = new MDBHandler(this);
    Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult firstResult, ScanResult secondResult) {
            return (firstResult.level > secondResult.level ? -1 :
                    (firstResult.level==secondResult.level ? 0 : 1));
        }
    };
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);
        WifiManager myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //check if wifi is enabled
        if (!myWifiManager.isWifiEnabled()) {
            Toast.makeText(SaveLocationActivity.this, "Please enable Wifi to enable scanning...", Toast.LENGTH_SHORT).show();
        } else {
            //enter the scan results to a list and sort them using the comparator defined before
            currentWifiList = (ArrayList<ScanResult>) myWifiManager.getScanResults();
            Collections.sort(currentWifiList,comparator);
        }
        //create variables corresponding to GUI elements
        final EditText editText = (EditText) findViewById(R.id.locationNameEditText);
        final Button button = (Button) findViewById(R.id.saveLocationButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String locationName = editText.getText().toString();

                DatabaseDataModel ddm = null;
                if(locationName != "") {
                    mDbHandler.open();
                    //This switch prevents from nullpointerexception when there are less than 5 detected WiFis
                    switch (currentWifiList.size()) {
                        case 0:
                            Toast.makeText(SaveLocationActivity.this, "No wifi networks detected!", Toast.LENGTH_LONG);
                        case 1:
                            ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, "null", 0, "null", 0, "null", 0, "null", 0);
                        case 2:
                            ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, "null", 0, "null", 0, "null", 0);
                        case 3:
                            ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, currentWifiList.get(2).SSID, currentWifiList.get(2).level, "null", 0, "null", 0);
                        case 4:
                            ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, currentWifiList.get(2).SSID, currentWifiList.get(2).level, currentWifiList.get(3).SSID, currentWifiList.get(3).level, "null", 0);
                        default:
                            ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, currentWifiList.get(2).SSID, currentWifiList.get(2).level, currentWifiList.get(3).SSID, currentWifiList.get(3).level, currentWifiList.get(4).SSID, currentWifiList.get(4).level);
                    }
                    if (ddm != null) {

                        Log.d("DatabaseDataModel", ddm.getRecordData());
                        Log.d("Inserted record no.", String.valueOf(mDbHandler.insertPattern(ddm)));

                    }
                }
                mDbHandler.close();
            }
        });
    }


    @Override
    protected void onDestroy(){
        mDbHandler.close();
        super.onDestroy();
    }


}
