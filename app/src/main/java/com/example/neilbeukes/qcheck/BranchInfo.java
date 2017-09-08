package com.example.neilbeukes.qcheck;

public class BranchInfo {

    private String name;
    private String adress;
    private String status;
    private double geoLat;
    private double geoLng;
    private boolean open;

    public BranchInfo(String name, String adress, String status, boolean open, double geoLat, double geoLng) {
        this.adress = adress;
        this.status = status;
        this.name = name;
        this.open = open;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
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
}
