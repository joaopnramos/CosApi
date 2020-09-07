package com.example.citizensonscience;

public class DataGiveResponse {
    String id, projectid, donatorid;

    public DataGiveResponse(String id, String projectid, String donatorid) {
        this.id = id;
        this.projectid = projectid;
        this.donatorid = donatorid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getDonatorid() {
        return donatorid;
    }

    public void setDonatorid(String donatorid) {
        this.donatorid = donatorid;
    }
}
