package com.thesis.guras.doorplate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by guras on 06.01.16.
 */

public class MDBHandler {
    private static final int DATABASE_VERSION = 1;
    private static final String DEBUG_TAG = "MyAppLogger";
    private static final String DATABASE_NAME = "locationTemplatesDatabase.sqlite";
    private static final int MIN_NUMBER_OF_MATCHING_PATTERNS = 2;
    private static final double MAX_DEVIATION_FROM_RSSI_TOTAL = 0.2;
    private static final String PATTERNS_TABLE_NAME = "Patterns";
    private static final String PATTERNS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + PATTERNS_TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, LocationName NOT NULL, " +
                    "SSID1 NOT NULL,RSSI1 NOT NULL,"+
                    "SSID2 NOT NULL,RSSI2 NOT NULL,"+
                    "SSID3 NOT NULL,RSSI3 NOT NULL,"+
                    "SSID4,RSSI4,"+
                    "SSID5,RSSI5,"+
                    "REC_SUCCESS,REC_FAILURE,RSSI_TOTAL);";
    private static final String DROP_SSID_TABLE = "DROP TABLE IF EXISTS "+ PATTERNS_TABLE_NAME;
    private SQLiteDatabase db;
    private Context context;
    private MDBOpenHelper myDbOpenHelper;

    /**
     *
     * @param context
     */
    MDBHandler(Context context) {
        this.context = context;
    }

    public MDBHandler open(){
        myDbOpenHelper = new MDBOpenHelper(context, PATTERNS_TABLE_NAME, null, DATABASE_VERSION);
        db = myDbOpenHelper.getWritableDatabase();
        return this;
    }

    /**
     *
     * @param dbm
     * @return Id assigned to the record
     */
    public long insertPattern(DatabaseDataModel dbm){
        Log.d(DEBUG_TAG, "insertPattern()");
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("LocationName",dbm.getLocationName());
        mContentValues.put("SSID1",dbm.getSSID(1));
        mContentValues.put("RSSI1",dbm.getRSSI(1));
        mContentValues.put("SSID2",dbm.getSSID(2));
        mContentValues.put("RSSI2",dbm.getRSSI(2));
        mContentValues.put("SSID3",dbm.getSSID(3));
        mContentValues.put("RSSI3",dbm.getRSSI(3));
        mContentValues.put("SSID4",dbm.getSSID(4));
        mContentValues.put("RSSI4",dbm.getRSSI(4));
        mContentValues.put("SSID5",dbm.getSSID(5));
        mContentValues.put("RSSI5",dbm.getRSSI(5));
        mContentValues.put("REC_SUCCESS",dbm.getREC_SUCCESS());
        mContentValues.put("REC_FAILURE", dbm.getREC_FAILURE());
        mContentValues.put("RSSI_TOTAL", dbm.getRSSITotal());
        Log.d(DEBUG_TAG, db.toString());
        Log.d(DEBUG_TAG,"insertPattern()");
        return db.insert(PATTERNS_TABLE_NAME,null,mContentValues);
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deletePattern(long id){
        String where = "ID =" + id;
        return db.delete(PATTERNS_TABLE_NAME, where, null) > 0;
    }


    public Cursor getAllPatterns() {
        Log.d(DEBUG_TAG,"getAllPatterns()");
        String selectQuery = "SELECT * FROM "+PATTERNS_TABLE_NAME;
        if(db.isOpen()){
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount()== 0){
                Log.d(DEBUG_TAG,"Cursor count was 0!");
                Log.d(DEBUG_TAG,"getAllPatterns()");
                return null;
            }
            Log.d(DEBUG_TAG,"Cursor count was: "+String.valueOf(cursor.getCount()));
            Log.d(DEBUG_TAG,"getAllPatterns()");
            return cursor;

        }else{
            Log.d(DEBUG_TAG,"DB was not opened!");
            Log.d(DEBUG_TAG,"getAllPatterns()");
            return null;
        }
    }

    /**
     *Method used to check number of records
     * @param db
     * @return int
     */
    public int getNumberOfPatterns(SQLiteDatabase db){
        Log.d(DEBUG_TAG,"getNumberOfPatterns()");
        String countQuery = "SELECT *  FROM " + PATTERNS_TABLE_NAME;
        Log.d(DEBUG_TAG, "Query : " + countQuery);
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.d(DEBUG_TAG, "getNumberOfPatterns()");
        return cursor.getCount();
    }

    /**
     *
     * @param cursor
     * @return
     */
    public ArrayList<DatabaseDataModel> getPatternsList(Cursor cursor){
        Log.d(DEBUG_TAG,"getPatternsList()");
        ArrayList<DatabaseDataModel> patternList = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                DatabaseDataModel databaseRecord = new DatabaseDataModel(cursor.getString(1),cursor.getString(2),cursor.getFloat(3),
                        cursor.getString(4),cursor.getFloat(5),
                        cursor.getString(6),cursor.getFloat(7),
                        cursor.getString(8),cursor.getFloat(9),
                        cursor.getString(10),cursor.getFloat(11));
                patternList.add(databaseRecord);
            }
            Log.d(DEBUG_TAG,"getPatternsList()");
            return patternList;
        }
        Log.d(DEBUG_TAG,"getPatternsList()");
        return null;


    }

    public Cursor getSimilarPatterns(DatabaseDataModel ddm, String locationName){
        Log.d(DEBUG_TAG,"getSimilarPatterns()");

        //Calculate range of RSSI_TOTAL which will further match
        double lowerRSSIBound = ddm.getRSSITotal()*(1 - MAX_DEVIATION_FROM_RSSI_TOTAL);
        double upperRSSIBound = ddm.getRSSITotal()*(1 + MAX_DEVIATION_FROM_RSSI_TOTAL);

        //Split the query into pieces to manipulate the search criteria
        String longQuery = "SELECT * FROM "+PATTERNS_TABLE_NAME+" WHERE RSSI_TOTAL BETWEEN "+lowerRSSIBound+" AND "+upperRSSIBound+" AND SSID1='"+ddm.getSSID(1)+"' :AND SSID2='"+ddm.getSSID(2)+"' :AND SSID3='"+ddm.getSSID(3)+"' :AND SSID4='"+ddm.getSSID(4)+"' :AND SSID5='"+ddm.getSSID(5)+"'";
        String [] queryBuilderArray = longQuery.split(":");

        //initially, the query asks for patterns with all SSIDs matching
        String initialQuery = queryBuilderArray[0]+queryBuilderArray[1]+queryBuilderArray[2]+queryBuilderArray[3]+queryBuilderArray[4];
        Cursor similarPatternsCursor = db.rawQuery(initialQuery,null);

        int counter = 4;
        while(similarPatternsCursor.getCount() < MIN_NUMBER_OF_MATCHING_PATTERNS && counter > 0){
            String mQuery = "";
            for(int i = 0; i < counter; i++){
                mQuery += queryBuilderArray[i];
            }

            Log.d(DEBUG_TAG,mQuery);
            similarPatternsCursor = db.rawQuery(mQuery,null);
            counter -= 1;
        }
        if(similarPatternsCursor.getCount() < 1){
            Log.d(DEBUG_TAG,"No similar patterns - cursor count was 0");
            Log.d(DEBUG_TAG,"getSimilarPatterns()");
            return null;
        }

        Log.d(DEBUG_TAG,"getSimilarPatterns()");
        //ArrayList<DatabaseDataModel> similarPatternsList = getPatternsList(similarPatternsCursor);
        return similarPatternsCursor;
    }

    public void close() {
        myDbOpenHelper.close();
    }

    /**
     * This method returns a DatabaseDataModel object with values ready to be inserted to database
     * @param currentWifiList
     * @param locationName
     * @return
     */
    public DatabaseDataModel setupInsertContent(ArrayList<ScanResult> currentWifiList, String locationName){
        DatabaseDataModel ddm = null;

        //This switch prevents from nullpointerexception when there are less than 5 detected WiFis
        switch (currentWifiList.size()) {
            case 0:
                Log.d(DEBUG_TAG,"No wifi networks have been detected");
                break;
            case 1:
                ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, "null", 0, "null", 0, "null", 0, "null", 0);
                break;
            case 2:
                ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, "null", 0, "null", 0, "null", 0);
                 break;
            case 3:
                ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, currentWifiList.get(2).SSID, currentWifiList.get(2).level, "null", 0, "null", 0);
                break;
            case 4:
                ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, currentWifiList.get(2).SSID, currentWifiList.get(2).level, currentWifiList.get(3).SSID, currentWifiList.get(3).level, "null", 0);
                break;
            default:
                ddm = new DatabaseDataModel(locationName, currentWifiList.get(0).SSID, currentWifiList.get(0).level, currentWifiList.get(1).SSID, currentWifiList.get(1).level, currentWifiList.get(2).SSID, currentWifiList.get(2).level, currentWifiList.get(3).SSID, currentWifiList.get(3).level, currentWifiList.get(4).SSID, currentWifiList.get(4).level);
        }
        return ddm;
    }
    /*
        This private class extends SQL helper to provide basic SQL interface
     */
    private static class MDBOpenHelper extends SQLiteOpenHelper {
        public MDBOpenHelper(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PATTERNS_TABLE_CREATE);
            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + PATTERNS_TABLE_CREATE + " ver." + DATABASE_VERSION + " created");
            Log.d(DEBUG_TAG,"Database path is: "+db.getPath());

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_SSID_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + PATTERNS_TABLE_NAME + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");
            onCreate(db);
        }
    }

}
;