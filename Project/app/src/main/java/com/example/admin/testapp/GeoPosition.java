package com.example.admin.testapp;

/**
 * Created by admin on 17.09.2017.
 */

class GeoPosition {
    private double latitude;
    private double longitude;

    protected static final GeoPosition leftTopBelarus = new GeoPosition(56.2278222,22.8722176);
    protected static final GeoPosition rightBottomBelarus= new GeoPosition(51.1795334,33.0539735);

    GeoPosition(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    GeoPosition(GeoPosition a){
        this.latitude = a.getLatitude();
        this.longitude = a.getLongitude();
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void  setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
