package com.thesis.guras.doorplate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.ScanResult;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by guras on 06.01.16.
 */

public class MDBHandler {
    private static final int DATABASE_VERSION = 1;
    private static final String DEBUG_TAG = "MDBHandler";
    private static final String DATABASE_NAME = "DoorPlateDatabase.sqlite";
    //location recognition parameters
    private static final int MIN_NUMBER_OF_MATCHING_PATTERNS = 2;
    private static final double MAX_DEVIATION_FROM_RSSI_TOTAL = 0.18;

    //table names and SQL queries
    private static final String PATTERNS_TABLE_NAME = "Patterns";
    private static final String MESSAGES_TABLE_NAME = "Messages";
    private static final String EVENTS_TABLE_NAME = "Events";

    private static final String PATTERNS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + PATTERNS_TABLE_NAME + " (" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "LocationName NOT NULL, " +
                    "SSID1 NOT NULL,RSSI1 NOT NULL," +
                    "SSID2 NOT NULL,RSSI2 NOT NULL," +
                    "SSID3 NOT NULL,RSSI3 NOT NULL," +
                    "SSID4 NOT NULL,RSSI4 NOT NULL," +
                    "SSID5 NOT NULL,RSSI5 NOT NULL," +
                    "RSSI_TOTAL NOT NULL," +
                    "INSERT_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");";
    private static final String MESSAGES_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + MESSAGES_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, MESSAGE NOT NULL)";
    private static final String EVENTS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME NOT NULL, EVENTDATE DATETIME NOT NULL)";
    private static final String DROP_SSID_TABLE = "DROP TABLE IF EXISTS " + PATTERNS_TABLE_NAME;

    private SQLiteDatabase db;
    private Context context;
    private MDBOpenHelper myDbOpenHelper;


    MDBHandler(Context context) {
        this.context = context;
    }

    public MDBHandler open() {
        myDbOpenHelper = new MDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = myDbOpenHelper.getWritableDatabase();
        return this;
    }

    /**
     * @param dbm
     * @return Id assigned to the record or null
     */
    public long insertPattern(DatabaseDataModel dbm) {
        Log.d(DEBUG_TAG, "insertPattern()");
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("LocationName", dbm.getLocationName());
        mContentValues.put("SSID1", dbm.getSSID(1));
        mContentValues.put("RSSI1", dbm.getRSSI(1));
        mContentValues.put("SSID2", dbm.getSSID(2));
        mContentValues.put("RSSI2", dbm.getRSSI(2));
        mContentValues.put("SSID3", dbm.getSSID(3));
        mContentValues.put("RSSI3", dbm.getRSSI(3));
        mContentValues.put("SSID4", dbm.getSSID(4));
        mContentValues.put("RSSI4", dbm.getRSSI(4));
        mContentValues.put("SSID5", dbm.getSSID(5));
        mContentValues.put("RSSI5", dbm.getRSSI(5));
        mContentValues.put("RSSI_TOTAL", dbm.getRSSITotal());
        Log.d(DEBUG_TAG, db.toString());
        Log.d(DEBUG_TAG, "insertPattern()");
        return db.insert(PATTERNS_TABLE_NAME, null, mContentValues);
    }

    /**
     * This method is used to insert message template to Messages table
     *
     * @param message
     * @return id of inserted message or null
     */
    public long insertMessageTemplate(String message) {
        Log.d(DEBUG_TAG, "insertMessageTemplate()");
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("MESSAGE", message);
        Log.d(DEBUG_TAG, db.toString());
        Log.d(DEBUG_TAG, "insertMessageTemplate()");
        return db.insert(MESSAGES_TABLE_NAME, null, mContentValues);
    }

    /**
     * This method is used to insert events to the database
     *
     * @param name
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @return id of the  inserted pattern or null
     */
    public long insertEvent(String name, int day, int month, int year, int hour, int minute) {
        Log.d(DEBUG_TAG, "insertEvent()");
        String dayString = String.valueOf(day);
        if (dayString.length() == 1) {
            dayString = "0" + String.valueOf(day);
        }
        String monthString = String.valueOf(month);
        if (monthString.length() == 1) {
            monthString = "0" + String.valueOf(month);
        }
        String hourString = String.valueOf(hour);
        if (hourString.length() == 1) {
            hourString = "0" + String.valueOf(hour);
        }
        String minuteString = String.valueOf(minute);
        if (minuteString.length() == 1) {
            minuteString = "0" + String.valueOf(minute);
        }
        String time = String.valueOf(year) + "-" + monthString + "-" + dayString + " " + hourString + "-" + minuteString;
        Log.d(DEBUG_TAG, "EventTime: " + time);
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("NAME", name);
        mContentValues.put("EVENTDATE", time);
        Log.d(DEBUG_TAG, db.toString());
        Log.d(DEBUG_TAG, "insertEvent()");
        return db.insert(EVENTS_TABLE_NAME, null, mContentValues);
    }


    public boolean deletePattern(long id) {
        String where = "_id =" + id;
        return db.delete(PATTERNS_TABLE_NAME, where, null) > 0;
    }

    public boolean deleteEvent(long id) {
        String where = "_id =" + id;
        return db.delete(EVENTS_TABLE_NAME, where, null) > 0;
    }

    public boolean deleteMessageTemplate(long id) {
        String where = "_id =" + id;
        return db.delete(MESSAGES_TABLE_NAME, where, null) > 0;
    }

    public Cursor getAllPatterns() {
        Log.d(DEBUG_TAG, "getAllPatterns()");
        String selectQuery = "SELECT * FROM " + PATTERNS_TABLE_NAME;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0) {
                Log.d(DEBUG_TAG, "Cursor count was 0!");
                Log.d(DEBUG_TAG, "getAllPatterns()");
                return null;
            }
            Log.d(DEBUG_TAG, "Cursor count was: " + String.valueOf(cursor.getCount()));
            Log.d(DEBUG_TAG, "getAllPatterns()");
            return cursor;

        } else {
            Log.d(DEBUG_TAG, "DB was not opened!");
            Log.d(DEBUG_TAG, "getAllPatterns()");
            return null;
        }
    }

    public Cursor getAllMessages() {
        Log.d(DEBUG_TAG, "getAllMessages()");
        String selectQuery = "SELECT * FROM " + MESSAGES_TABLE_NAME;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0) {
                Log.d(DEBUG_TAG, "Cursor count was 0!");
                Log.d(DEBUG_TAG, "getAllMessages()");
                return null;
            }
            Log.d(DEBUG_TAG, "Cursor count was: " + String.valueOf(cursor.getCount()));
            Log.d(DEBUG_TAG, "getAllMessages()");
            return cursor;

        } else {
            Log.d(DEBUG_TAG, "DB was not opened!");
            Log.d(DEBUG_TAG, "getAllMessages()");
            return null;
        }
    }

    public Cursor getAllEvents() {
        Log.d(DEBUG_TAG, "getAllEvents()");
        String selectQuery = "SELECT * FROM " + EVENTS_TABLE_NAME + " ORDER BY EVENTDATE ASC";
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0) {
                Log.d(DEBUG_TAG, "Cursor count was 0!");
                Log.d(DEBUG_TAG, "getAllEvents()");
                return null;
            }
            Log.d(DEBUG_TAG, "Cursor count was: " + String.valueOf(cursor.getCount()));
            Log.d(DEBUG_TAG, "getAllEvents()");
            return cursor;
        } else {
            Log.d(DEBUG_TAG, "DB was not opened!");
            Log.d(DEBUG_TAG, "getAllEvents()");
            return null;
        }
    }



    //Command for pulling db file from device through adb
    //adb -s 5ba25094 pull /storage/emulated/legacy/DoorPlateDatabase.sqlite

    public void exportDatabaseToFile() {
        Log.d(DEBUG_TAG, "exportDatabaseToFile()");
        try {
            File externalStorage = Environment.getExternalStorageDirectory();

            String backupDBFileName = DATABASE_NAME;
            File backupDB = new File(externalStorage, backupDBFileName);
            backupDB.createNewFile();

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                Log.d(DEBUG_TAG, "Can read and write the media");
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
                Log.d(DEBUG_TAG, "Can read only");
            } else {
                Log.d(DEBUG_TAG, "Can't read or write!");
            }


            Log.d(DEBUG_TAG, "Checking if backup database file exists: " + String.valueOf(backupDB.exists()));
            Log.d(DEBUG_TAG, "BackupDB path: " + backupDB.getPath());
            Log.d(DEBUG_TAG, "Current DB path: " + db.getPath());

            File sourceDB = new File(Environment.getDataDirectory() + context.getDatabasePath(DATABASE_NAME).getAbsolutePath());
            File data = Environment.getDataDirectory();

            Log.d(DEBUG_TAG,"Checking if the data directory exists... : "+String.valueOf(sourceDB.exists()));

            if(backupDB.canWrite()){
                Log.d(DEBUG_TAG,"BackupDB file is writable");
                File currentDb = new File(context.getDatabasePath(DATABASE_NAME).getAbsolutePath());

                FileChannel src = new FileInputStream(currentDb).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
            }else{
                Log.d(DEBUG_TAG,"Unable to write to external storage!");
            }
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught during DB export.");
            e.printStackTrace();
            Log.d(DEBUG_TAG, "exportDatabaseToFile()");
        }
        Log.d(DEBUG_TAG, "exportDatabaseToFile()");
    }




    /**
     * @param cursor
     * @return
     */
    public ArrayList<DatabaseDataModel> getPatternsList(Cursor cursor) {
        Log.d(DEBUG_TAG, "getPatternsList()");
        ArrayList<DatabaseDataModel> patternList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                DatabaseDataModel databaseRecord = new DatabaseDataModel(cursor.getString(1), cursor.getString(2), cursor.getFloat(3),
                        cursor.getString(4), cursor.getFloat(5),
                        cursor.getString(6), cursor.getFloat(7),
                        cursor.getString(8), cursor.getFloat(9),
                        cursor.getString(10), cursor.getFloat(11));
                patternList.add(databaseRecord);
            }
            Log.d(DEBUG_TAG, "getPatternsList()");
            return patternList;
        }
        Log.d(DEBUG_TAG, "getPatternsList()");
        return null;
    }

    /**
     * This method is used to remove the oldest pattern and replace it with a new one
     * for the current location once that location is successfully recognized
     *
     * @param locationName
     */
    public void removeOldestSimilarPattern(String locationName) {
        Log.d(DEBUG_TAG, "removeOldestSimilarPattern()");
        locationName.toLowerCase();
        String digitsOnly = locationName.replaceAll("[^0-9]", "");
        String query = "SELECT * FROM " + PATTERNS_TABLE_NAME + " WHERE LocationName='" + locationName + "' OR LocationName LIKE '%" + digitsOnly + "%' ORDER BY INSERT_TIMESTAMP ASC";
        Cursor similarLocationsCursor = db.rawQuery(query, null);
        similarLocationsCursor.moveToFirst();
        if (similarLocationsCursor.getCount() > 5) {
            Log.d(DEBUG_TAG, "There were at least 5 records matching current location. ");
            int deletedRecordId = similarLocationsCursor.getInt(0);
            if (!deletePattern(deletedRecordId)) {
                Log.d(DEBUG_TAG, "Failed to remove record");
                Log.d(DEBUG_TAG, "removeOldestSimilarPattern()");
            } else {
                Log.d(DEBUG_TAG, "Record "+String.valueOf(deletedRecordId)+"removal successful");
                Log.d(DEBUG_TAG, "removeOldestSimilarPattern()");
            }
        }
    }

    /**
     * This method is used to suggest locations which can be used based on recognition parameters
     *
     * @param ddm
     * @param locationName
     * @return
     */
    public Cursor getSimilarPatterns(DatabaseDataModel ddm, String locationName) {
        Log.d(DEBUG_TAG, "getSimilarPatterns()");

        //Calculate range of RSSI_TOTAL which will further match
        double lowerRSSIBound = ddm.getRSSITotal() * (1 - MAX_DEVIATION_FROM_RSSI_TOTAL);
        double upperRSSIBound = ddm.getRSSITotal() * (1 + MAX_DEVIATION_FROM_RSSI_TOTAL);

        //Split the query into pieces to manipulate the search criteria inside a loop
        String longQuery = "SELECT * FROM " + PATTERNS_TABLE_NAME + " WHERE RSSI_TOTAL BETWEEN " + lowerRSSIBound + " AND " + upperRSSIBound + " AND SSID1='" + ddm.getSSID(1) + "' :AND SSID2='" + ddm.getSSID(2) + "' :AND SSID3='" + ddm.getSSID(3) + "' :AND SSID4='" + ddm.getSSID(4) + "' :AND SSID5='" + ddm.getSSID(5) + "'";
        String[] queryBuilderArray = longQuery.split(":");

        //initially, the query asks for patterns with all SSIDs matching
        String initialQuery = queryBuilderArray[0] + queryBuilderArray[1] + queryBuilderArray[2] + queryBuilderArray[3] + queryBuilderArray[4];
        Log.d(DEBUG_TAG, initialQuery);
        Cursor similarPatternsCursor = db.rawQuery(initialQuery, null);

        int counter = 4;
        while (similarPatternsCursor.getCount() < MIN_NUMBER_OF_MATCHING_PATTERNS && counter > 0) {
            String mQuery = "";
            for (int i = 0; i < counter; i++) {
                mQuery += queryBuilderArray[i];
            }
            Log.d(DEBUG_TAG, mQuery);
            similarPatternsCursor = db.rawQuery(mQuery, null);
            counter -= 1;
        }
        if (similarPatternsCursor.getCount() < 1) {
            Log.d(DEBUG_TAG, "No similar patterns - cursor count was 0");
            Log.d(DEBUG_TAG, "getSimilarPatterns()");
            return null;
        }

        Log.d(DEBUG_TAG, "getSimilarPatterns()");
        return similarPatternsCursor;
    }

    public void close() {
        myDbOpenHelper.close();
    }

    /**
     * This method returns a DatabaseDataModel object with values ready to be inserted to database
     *
     * @param currentWifiList
     * @param locationName
     * @return
     */
    public DatabaseDataModel setupInsertContent(ArrayList<ScanResult> currentWifiList, String locationName) {
        DatabaseDataModel ddm = null;

        //This switch prevents from nullpointerexception when there are less than 5 detected WiFis
        switch (currentWifiList.size()) {
            case 0:
                Log.d(DEBUG_TAG, "No wifi networks have been detected");
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
            db.execSQL(MESSAGES_TABLE_CREATE);
            db.execSQL(EVENTS_TABLE_CREATE);

            String[] sampleMessages = {"I am in room: ", "Office hours are scheduled for:", "I will be late by ", "I am on a sick leave till: ", "I am unavailable today"};
            for (int i = 0; i < sampleMessages.length; i++) {
                String insertSQL = "INSERT INTO " + MESSAGES_TABLE_NAME + " (MESSAGE) VALUES ('" + sampleMessages[i] + "')";
                db.execSQL(insertSQL);
            }
            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + PATTERNS_TABLE_CREATE + " ver." + DATABASE_VERSION + " created");
            Log.d(DEBUG_TAG, "Table " + MESSAGES_TABLE_CREATE + " created");
            Log.d(DEBUG_TAG, "Table " + EVENTS_TABLE_NAME + " created");
            Log.d(DEBUG_TAG, "Database path is: " + db.getPath());

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