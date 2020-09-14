package com.example.citizensonscience.classes;

public class DataGiveResponse {
    String id, project, donator, givingFinished;

    public String getGivingFinished() {
        return givingFinished;
    }

    public void setGivingFinished(String givingFinished) {
        this.givingFinished = givingFinished;
    }

    public DataGiveResponse() {
    }

    public DataGiveResponse(String id, String projectid, String donatorid) {
        this.id = id;
        this.project = projectid;
        this.donator = donatorid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectid() {
        return project;
    }

    public void setProjectid(String projectid) {
        this.project = projectid;
    }

    public String getDonatorid() {
        return donator;
    }

    public void setDonatorid(String donatorid) {
        this.donator = donatorid;
    }
}
