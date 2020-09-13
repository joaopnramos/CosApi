package com.example.citizensonscience.classes;

import java.util.TimerTask;

public class Job extends TimerTask {
    private String s1= "", s2= "", s3= "", s4= "";
    private int Period;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    int timesToRun = 0;

    public void setTimesToRun(int timesToRun) {
        this.timesToRun = timesToRun;
    }

    public int  runTimes = 0;
    @Override
    public void run() {



        System.out.println("Projeto id: " +  id );
        System.out.println("sensor 1 " + s1);
        System.out.println("sensor 2 " + s2);
        System.out.println("sensor 3 " + s3);
        System.out.println("sensor 4 " + s4);

        runTimes++;
        if (runTimes >= timesToRun) {
            this.cancel();

        }

    }
}