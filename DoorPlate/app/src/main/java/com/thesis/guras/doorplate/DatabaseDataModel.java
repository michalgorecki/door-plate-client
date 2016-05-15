package com.thesis.guras.doorplate;

import android.util.Log;

import java.util.Calendar;


/**
 * Created by guras on 27.01.16.
 */
public class DatabaseDataModel{

    private String locationName;
    private String SSID1;
    private float RSSI1;
    private String SSID2;
    private float RSSI2;
    private String SSID3;
    private float RSSI3;
    private String SSID4;
    private float RSSI4;
    private String SSID5;
    private float RSSI5;
    private float RSSI_TOTAL;

    //initializes with nulls
    public DatabaseDataModel() {
        locationName = "";
        SSID1 = "";
        SSID2 = "";
        SSID3 = "";
        SSID4 = "";
        SSID5 = "";
        RSSI1 = 0;
        RSSI2 = 0;
        RSSI3 = 0;
        RSSI4 = 0;
        RSSI5 = 0;
        RSSI_TOTAL = 0;
    }

    /**
     *
     * @param name
     * @param S1
     * @param R1
     * @param S2
     * @param R2
     * @param S3
     * @param R3
     * @param S4
     * @param R4
     * @param S5
     * @param R5
     */
    public DatabaseDataModel(String name,String S1, float R1,String S2, float R2,String S3, float R3,String S4, float R4,String S5, float R5){
        this.locationName = name.toLowerCase();
        this.SSID1 = S1;
        this.RSSI1 = R1;
        this.SSID2 = S2;
        this.RSSI2 = R2;
        this.SSID3 = S3;
        this.RSSI3 = R3;
        this.SSID4 = S4;
        this.RSSI4 = R4;
        this.SSID5 = S5;
        this.RSSI5 = R5;
        Calendar calendar = Calendar.getInstance();
        /*this.TIMESTAMP = String.valueOf(calendar.get(Calendar.YEAR))+"-"+String.valueOf(calendar.get(Calendar.MONTH))+"-"+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+
                "-"+String.valueOf(calendar.get(Calendar.HOUR))+"-"+String.valueOf(calendar.get(Calendar.MINUTE))+"-"+String.valueOf(calendar.get(Calendar.SECOND));
        Log.d("DDM",TIMESTAMP);*/
        this.RSSI_TOTAL = Math.abs(this.RSSI1) + Math.abs(this.RSSI2) + Math.abs(this.RSSI3) + Math.abs(this.RSSI4) + Math.abs(this.RSSI5);
    }

    public String getRecordData(){
        return "Record data: "+locationName+", "+SSID1+"/"+RSSI1+", "+SSID2+"/"+RSSI2+", "+SSID3+"/"+RSSI3+", "+SSID4+"/"+RSSI4+", "+SSID5+"/"+RSSI5;
    }

    /**
     *
     * @param index
     * @return
     */
    public String getSSID(int index){
        if(index > 0 && index < 6){
            switch(index){
                case 1 :
                    return SSID1;
                case 2 :
                    return SSID2;
                case 3 :
                    return SSID3;
                case 4 :
                    return SSID4;
                case 5 :
                    return SSID5;
                default :
                    break;
            }
        }
       return null;
    }

    /**
     *
     * @param index
     * @return
     */
    public float getRSSI(int index){
        if(index > 0 && index < 6){
            switch(index){
                case 1 :
                    return RSSI1;
                case 2 :
                    return RSSI2;
                case 3 :
                    return RSSI3;
                case 4 :
                    return RSSI4;
                case 5 :
                    return RSSI5;
                default :
                    break;
            }
        }
        return -1;
    }
    /*public String getTimestamp(){
        return TIMESTAMP;
    }*/
    public String getLocationName(){
        return locationName;
    }

    public float getRSSITotal(){
        return RSSI_TOTAL;
    }

}

