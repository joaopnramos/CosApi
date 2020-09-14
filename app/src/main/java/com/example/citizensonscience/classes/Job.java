package com.example.citizensonscience.classes;

import com.example.citizensonscience.Netwowk.RetrofitClient;

import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Job extends TimerTask {
    private String temperature = "0.0", proximity = "0.0", light = "0.0", pressure = "0.0";
    private int Period;
    private String project;
    private String owner;
    private String token;
    private String DG;

    public String getDG() {
        return DG;
    }

    public void setDG(String DG) {
        this.DG = DG;
    }

    public Job() {
    }

    public Job(String temperature, String proximity, String light, String pressure, String project, String owner) {
        this.temperature = temperature;
        this.proximity = proximity;
        this.light = light;
        this.pressure = pressure;
        this.project = project;
        this.owner = owner;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getProximity() {
        return proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    int timesToRun = 0;

    public void setTimesToRun(int timesToRun) {
        this.timesToRun = timesToRun;
    }

    public int runTimes = 0;

    @Override
    public void run() {


        System.out.println("Projeto id: " + project);
        System.out.println("sensor 1 " + temperature);
        System.out.println("sensor 2 " + proximity);
        System.out.println("sensor 3 " + light);
        System.out.println("sensor 4 " + pressure);

        runTimes++;
        if (runTimes >= timesToRun) {
            DataGiveResponse dt = new DataGiveResponse();
            dt.setGivingFinished("true");
            Call<DataGiveResponse> send = RetrofitClient.getmInstance().getApi().endProject(token, this.DG, dt);
            send.enqueue(new Callback<DataGiveResponse>() {
                @Override
                public void onResponse(Call<DataGiveResponse> call, Response<DataGiveResponse> response) {
                    System.out.println("Done");
                    System.out.println(response.message());
                }
                @Override
                public void onFailure(Call<DataGiveResponse> call, Throwable t) {
                }
            });
            this.cancel();
        }
        Job data = new Job(temperature, proximity, light, pressure, project, owner);
        Call<Job> lets = RetrofitClient.getmInstance().getApi().inserData(token, data);
        lets.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                System.out.println(response);
            }
            @Override
            public void onFailure(Call<Job> call, Throwable t) {
            }
        });

    }
}