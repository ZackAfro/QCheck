package com.example.neilbeukes.qcheck;

public class BranchInfo {

    String name;
    String adress;
    String status;

    public BranchInfo(String name, String adress, String status) {
        this.adress = adress;
        this.status = status;
        this.name = name;
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
