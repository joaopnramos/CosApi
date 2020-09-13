package com.example.citizensonscience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;

import com.example.citizensonscience.Netwowk.RetrofitClient;
import com.example.citizensonscience.classes.DataGiveResponse;
import com.example.citizensonscience.classes.DonatorResponse;
import com.example.citizensonscience.classes.Job;
import com.example.citizensonscience.classes.Project;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCollector extends AppCompatActivity implements SensorEventListener {
    private TextView id;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    private List<DataGiveResponse> projectList = new ArrayList<>();
    private List<DonatorResponse> donoList = new ArrayList<>();
    private SensorManager mSensorManager = null;
    private Sensor mSensor;
    public String sensorName = "";
    private Sensor sensortemperature, sensorProximity, sensorAcelarato, sensorPressure;
    private SensorManager sensorManager;
    public String aceelaration;
    private Timer timeTask = new Timer();
    private List<Job> jobs = new ArrayList<>();
    private boolean bdone = false;

    private int runTimes = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collector);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcelarato = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, sensorAcelarato, sensorManager.SENSOR_DELAY_NORMAL);

        sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener((SensorEventListener) this, sensorPressure, sensorManager.SENSOR_DELAY_NORMAL);

        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener((SensorEventListener) this, sensorProximity, sensorManager.SENSOR_DELAY_NORMAL);


        sensortemperature = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener((SensorEventListener) this, sensortemperature, sensorManager.SENSOR_DELAY_NORMAL);


        id = findViewById(R.id.textView2);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        String token = preferences.getString("token", "Não encontrado");
        final String iduser = preferences.getString("id", "id não encontrado");
        final String sendToken = "Token \t" + token;
        final String[] iddonator = new String[1];
        final String[] sensorsArray = {"temperature", "Proximity", "Light", "pressure"};


        //Saber o id do donator
        Call<List<DonatorResponse>> calld = RetrofitClient.getmInstance().getApi().userDonator(iduser, sendToken);
        try {
            donoList = calld.execute().body();
            for (int i = 0; i < donoList.size(); i++) {
                final DonatorResponse dono = donoList.get(i);
                String idDonaotr = dono.getId();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("iddonator", idDonaotr);
                editor.commit();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Id donator
        String idDonator = preferences.getString("iddonator", "Não encontrado");


        Call<List<DataGiveResponse>> call = RetrofitClient.getmInstance().getApi().DataDataGive(idDonator, sendToken);

        try {
            projectList = call.execute().body();
            for (int i = 0; i < projectList.size(); i++) {
                //Consigo obter o id de todos os projeto;
                final DataGiveResponse project = projectList.get(i);
                //id dos projetos
                final String projectid = project.getProjectid();

                Call<Project> info = RetrofitClient.getmInstance().getApi().infoProjeto(projectid, sendToken);

                Project proj = null;
                try {
                    proj = info.execute().body();
                    final int timesRun = proj.getPeriodChoice();
                    timeTask = new Timer();
                    Job j = new Job();
                    j.setPeriod(proj.getSpacetimeChoice());
                    j.setId(projectid);
                    j.setTimesToRun(timesRun);
                    jobs.add(j);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Não ouve resposta por parte do id ");
                }
        }}
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Não ouve resposta");
        }



        for (Job j : jobs) {
            timeTask.scheduleAtFixedRate(j, 1000, j.getPeriod()*1000);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for (Job j : jobs) {
                j.setS1(String.valueOf(sensorEvent.values[0]));
            }


        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            for (Job j : jobs) {
                j.setS2(String.valueOf(sensorEvent.values[0]));
            }


        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            for (Job j : jobs) {
                j.setS3(String.valueOf(sensorEvent.values[0]));
            }


        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            for (Job j : jobs) {
                j.setS4(String.valueOf(sensorEvent.values[0]));
            }


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, sensorAcelarato, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, sensorPressure, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, sensorProximity, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, sensortemperature, sensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener((SensorEventListener) this);
        super.onStop();
    }


}
