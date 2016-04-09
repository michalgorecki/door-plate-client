package com.thesis.guras.doorplate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by guras on 06.01.16.
 */

public class MDBHandler {
    private static final int DATABASE_VERSION = 1;
    private static final String DEBUG_TAG = "MyAppLogger";
    private static final String DATABASE_NAME = "locationTemplatesDatabase.sqlite";
    private static final String PATTERNS_TABLE_NAME = "Patterns";
    private static final String PATTERNS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + PATTERNS_TABLE_NAME + " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT, LocationName NOT NULL, " +
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


    MDBHandler(Context context) {
        this.context = context;
    }

    public MDBHandler open(){
        myDbOpenHelper = new MDBOpenHelper(context, PATTERNS_TABLE_NAME, null, DATABASE_VERSION);
        db = myDbOpenHelper.getWritableDatabase();
        return this;
    }


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
        //debugging purpose only
        Cursor cursor = getAllPatterns(db);
        if(cursor != null){
            ArrayList<DatabaseDataModel> patternList = getPatternsList(cursor);
        }

        //end
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

    /**
     *
     * @param db
     * @return
     */
    public Cursor getAllPatterns(SQLiteDatabase db) {
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
     *
     * @param db
     * @return
     */
    public int getPatternCount(SQLiteDatabase db){
        Log.d(DEBUG_TAG,"getPatternCount()");
        String countQuery = "SELECT *  FROM " + PATTERNS_TABLE_NAME;
        Log.d(DEBUG_TAG, "Query : " + countQuery);
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.d(DEBUG_TAG, "getPatternCount()");
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


    public void close() {
        myDbOpenHelper.close();
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