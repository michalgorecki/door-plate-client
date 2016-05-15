package com.thesis.guras.doorplate;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    ListView suggestedLocationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        editText = (EditText) findViewById(R.id.sendToLocationEditText);
        final WifiManager myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        suggestedLocationsListView = (ListView) findViewById(R.id.suggestionsListView);

        //check if wifi is enabled, proceed if it is
        if (!myWifiManager.isWifiEnabled()) {
            Toast.makeText(SendMessageActivity.this, "Please enable Wifi to enable scanning...", Toast.LENGTH_SHORT).show();
        } else {
            currentWifiList = (ArrayList<ScanResult>) myWifiManager.getScanResults();
            Collections.sort(currentWifiList, comparator);
            mdbHandler.open();
            DatabaseDataModel mCurrentDataModel = mdbHandler.setupInsertContent(currentWifiList, "current");
            final Cursor suggestedPatterns = mdbHandler.getSimilarPatterns(mCurrentDataModel, "none");
            populateListViewWithSuggestedLocations(suggestedPatterns, suggestedLocationsListView);
            mdbHandler.close();

            Bundle intentExtras = getIntent().getExtras();
            if (intentExtras != null) {
                Log.d(DEBUG_TAG, "Intent extras was not null");
                String selectedLocationExtra = intentExtras.getString("LocationName");
                editText.setText(selectedLocationExtra);
            }
        }

    }

    private void populateListViewWithSuggestedLocations(Cursor suggestedPatternsCursor, ListView listView) {
        if (suggestedPatternsCursor != null && suggestedPatternsCursor.getCount() > 0) {
            final PatternCursorAdapter mAdapter = new PatternCursorAdapter(this, suggestedPatternsCursor, 0);
            listView.setAdapter(mAdapter);
        }

    }
}
