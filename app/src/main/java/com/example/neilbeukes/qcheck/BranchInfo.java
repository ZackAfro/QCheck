package com.example.neilbeukes.qcheck;

public class BranchInfo {

    private String name;
    private String adress;
    private String status;
    private double geoLat;
    private double geoLng;
    private boolean open;
    private String distanceText;
    private String id;

    private int distanceKm;
    private int timeSeconds;
    private String timeString;

    public BranchInfo(String name, String adress, String status, boolean open, double geoLat, double geoLng, String id) {
        this.adress = adress;
        this.status = status;
        this.name = name;
        this.open = open;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
    }

    public String getId(){
        return id;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public double getGeoLng() {
        return geoLng;
    }

    public boolean isOpen() {
        return open;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public String getStatus() {
        return status;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }
}
