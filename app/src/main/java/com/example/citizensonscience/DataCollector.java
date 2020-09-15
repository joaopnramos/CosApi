package com.example.citizensonscience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.citizensonscience.Netwowk.RetrofitClient;
import com.example.citizensonscience.classes.DataGiveResponse;
import com.example.citizensonscience.classes.DonatorResponse;
import com.example.citizensonscience.classes.Job;
import com.example.citizensonscience.classes.Project;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;

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
    Hashtable<String , Integer  > timeDictionary = new Hashtable<String, Integer>();

    private ConstraintLayout layout;
    private int runTimes = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collector);

        layout = findViewById(R.id.main1);
        AnimationDrawable animD = (AnimationDrawable) layout.getBackground();
        animD.setEnterFadeDuration(2000);
        animD.setExitFadeDuration(4000);
        animD.start();

        TextView text = findViewById(R.id.textView2);
        TextView text1 = findViewById(R.id.textView3);

        //Registro de todos os sensores
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcelarato = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener((SensorEventListener) this, sensortemperature, sensorManager.SENSOR_DELAY_NORMAL);
        sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener((SensorEventListener) this, sensorProximity, sensorManager.SENSOR_DELAY_NORMAL);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener((SensorEventListener) this, sensorAcelarato, sensorManager.SENSOR_DELAY_NORMAL);
        sensortemperature = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener((SensorEventListener) this, sensorPressure, sensorManager.SENSOR_DELAY_NORMAL);

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

        //Chamada para saber em que projetos os donator se encontra
        Call<List<DataGiveResponse>> call = RetrofitClient.getmInstance().getApi().DataDataGive(idDonator, sendToken);

        try {
            projectList = call.execute().body();
            if (projectList != null && projectList.isEmpty()){

                text.setText("Sorry, at this moment there are no projects available");
                text1.setText("Please, visit the website and subscribe to projects!");

            }
            for (int i = 0; i < projectList.size(); i++) {
                //Consigo obter o id de todos os projeto;
                final DataGiveResponse project = projectList.get(i);
                //id dos projetos
                final String projectid = project.getProjectid();
                final String dgID = project.getId();

                //Saber os detalhes de cada projeto e executalos como uma tarefa executada atraves da variavel tempo
                Call<Project> info = RetrofitClient.getmInstance().getApi().infoProjeto(projectid, sendToken);
                Project proj = null;
                try {
                    proj = info.execute().body();
                    final int timesRun = proj.getPeriodChoice();
                    timeTask = new Timer();
                    System.out.println();

                    //Criação de uma tarefa
                    Job j = new Job();
                    j.setDG(dgID);
                    j.setPeriod(proj.getSpacetimeChoice());
                    j.setProject(projectid);
                    j.setOwner(idDonator);
                    j.setTimesToRun(timesRun);
                    j.setToken(sendToken);
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

        timeDictionary.put("30", 30000);
        timeDictionary.put("1", 60000);
        timeDictionary.put("2", 7200000);
        timeDictionary.put("12", 43200000);
        timeDictionary.put("24", 86400000);
        //A execução de cada tarefa, ou seja, a recolha dos dados de sensores de cada projeto
        for (Job j : jobs) {
            String s = String.valueOf(j.getPeriod());
            timeTask.scheduleAtFixedRate(j, 1000, timeDictionary.get(s));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            for (Job j : jobs) {
                j.setTemperature(String.valueOf(sensorEvent.values[0]));
            }


        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            for (Job j : jobs) {
                j.setProximity(String.valueOf(sensorEvent.values[0]));
            }


        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            for (Job j : jobs) {
                j.setLight(String.valueOf(sensorEvent.values[0]));
            }


        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            for (Job j : jobs) {
                j.setPressure(String.valueOf(sensorEvent.values[0]));
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
