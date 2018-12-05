package com.lee.map01;


public class Common {
    
    public static String Location_City = "";
    public static String Location_Address = "";
    public static String Location_End = "";
    public static String Sreach_City = "";
    public static double Location_Latitude ;
    public static double Location_Longitude ;
    public static double Sreach_Latitude ;
    public static double Sreach_Longitude ;
    
    public static Common mCommon;
    public static final int ACCESS_COARSE_LOCATION = 3;
    public String ADDRESS = "";
    public String LONGITUDE ;
    public String LATITUDE;
    public float RADIUS;

    public float getRADIUS() {
        return RADIUS;
    }

    public void setRADIUS(float RADIUS) {
        this.RADIUS = RADIUS;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public static Common getInstense(){
        if (mCommon == null){
            mCommon = new Common();
        }
        return mCommon;
    }
}
