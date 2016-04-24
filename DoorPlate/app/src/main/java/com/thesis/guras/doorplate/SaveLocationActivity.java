package com.thesis.guras.doorplate;

import android.content.Context;
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

public class SaveLocationActivity extends AppCompatActivity {
    private ArrayList<ScanResult> currentWifiList = new ArrayList<ScanResult>();
    //create variables corresponding to GUI elements
    private EditText editText;
    private Button button;
    private ListView listView;
    private String DEBUG_TAG = "SaveLocationActivity";
    private MDBHandler mDbHandler = new MDBHandler(this);
    Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult firstResult, ScanResult secondResult) {
            return (firstResult.level > secondResult.level ? -1 :
                    (firstResult.level==secondResult.level ? 0 : 1));
        }
    };


    private void populateListViewWithPatterns(){
        mDbHandler.open();
        Cursor cursor = mDbHandler.getAllPatterns();
        final PatternCursorAdapter mAdapter = new PatternCursorAdapter(this,cursor,0);
        listView.setAdapter(mAdapter);
        mDbHandler.close();
    }
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
        editText = (EditText) findViewById(R.id.locationNameEditText);
        button = (Button) findViewById(R.id.saveLocationButton);
        listView = (ListView) findViewById(R.id.listView);


       populateListViewWithPatterns();

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String locationName = editText.getText().toString();
                if(!locationName.equals("") && locationName != null) {
                    mDbHandler.open();
                    DatabaseDataModel ddm = mDbHandler.setupInsertContent(currentWifiList, locationName);
                    if (ddm != null) {
                        Log.d(DEBUG_TAG, "DatabaseDataModel: " + ddm.getRecordData());
                        Log.d(DEBUG_TAG,"Inserted record no. "+String.valueOf(mDbHandler.insertPattern(ddm)));
                        editText.setText("");
                        populateListViewWithPatterns();
                    }
                    mDbHandler.close();
                }
            }
        });
    }


    @Override
    protected void onDestroy(){
        mDbHandler.close();
        super.onDestroy();
    }


}
