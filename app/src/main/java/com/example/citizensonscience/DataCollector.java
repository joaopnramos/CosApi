package com.example.citizensonscience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.citizensonscience.Netwowk.RetrofitClient;
import com.example.citizensonscience.classes.DataGiveResponse;
import com.example.citizensonscience.classes.DonatorResponse;
import com.example.citizensonscience.classes.Project;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCollector extends AppCompatActivity implements SensorEventListener{
    private TextView id;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    private List<DataGiveResponse> projectList = new ArrayList<>();
    private List<DonatorResponse> donoList = new ArrayList<>();
    private SensorManager mSensorManager = null;
    private Sensor mSensor;
    public String sensorName = "";
    private Sensor sensortemperature, sensorProximity, sensorLight, sensorPressure;
    private SensorManager sensorManager;
    public RunSensors rs = new RunSensors();
    public String aceelaration;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collector);

        id = findViewById(R.id.textView2);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        String token = preferences.getString("token", "Não encontrado");
        final String iduser = preferences.getString("id", "id não encontrado");
        final String sendToken = "Token \t" + token;
        final String[] iddonator = new String[1];
        final String[] sensorsArray = {"temperature", "Proximity", "Light", "pressure"};
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Saber o id do donator
        Call<List<DonatorResponse>> calld = RetrofitClient.getmInstance().getApi().userDonator(iduser, sendToken);
        calld.enqueue(new Callback<List<DonatorResponse>>() {
            @Override
            public void onResponse(Call<List<DonatorResponse>> call, Response<List<DonatorResponse>> response) {


                donoList = response.body();
                for (int i=0; i<donoList.size(); i++){
                    final DonatorResponse dono = donoList.get(i);
                    String idDonaotr = dono.getId();

                    SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("iddonator", idDonaotr );
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<DonatorResponse>> call, Throwable t) {
            }
        });


        //Id donator
        String idDonator = preferences.getString("iddonator", "Não encontrado");


        Call<List<DataGiveResponse>> call = RetrofitClient.getmInstance().getApi().DataDataGive(idDonator, sendToken);
        call.enqueue(new Callback<List<DataGiveResponse>>(){

            @Override
            public void onResponse(Call<List<DataGiveResponse>> call, Response<List<DataGiveResponse>> response) {

                projectList = response.body();
                for (int i=0; i<projectList.size(); i++){
                    //Consigo obter o id de todos os projeto;
                    final DataGiveResponse project = projectList.get(i);
                    //id dos projetos
                    final String projectid = project.getId();

                    Call<Project> info = RetrofitClient.getmInstance().getApi().infoProjeto(projectid, sendToken);
                    info.enqueue(new Callback<Project>() {
                        @Override
                        public void onResponse(Call<Project> call, Response<Project> response) {

                            int timesRun =response.body().getPeriodChoice();
                            int i = 0;
                            while(i < 2){
                                int j;
                                for (j=0; j< sensorsArray.length; j++){

                                    sensorName = sensorsArray[j];
                                    rs.run();
                                    System.out.println(aceelaration + "1");



                                }

                                i++;
                            };

                        }

                        @Override
                        public void onFailure(Call<Project> call, Throwable t) {

                            System.out.println("Não ouve resposta por parte do id ");

                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<List<DataGiveResponse>> call, Throwable t) {
                System.out.println("Não ouve resposta");
            }
        });
}

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            aceelaration = String.valueOf(sensorEvent.values[0]);



        }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_GRAVITY){
            System.out.println(String.valueOf(sensorEvent.values[0]));


        }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            System.out.println(String.valueOf(sensorEvent.values[0]));

        }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
            System.out.println(String.valueOf(sensorEvent.values[0]) + String.valueOf(sensorEvent.values[1]) );


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this,sensorLight,sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this,sensorPressure,sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this,sensorProximity,sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this,sensortemperature,sensorManager.SENSOR_DELAY_NORMAL);

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

    class RunSensors extends Thread{

        @Override
        public void run() {
            super.run();
            runs();
            System.out.println();


        }


    }


    public void runs(){
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this,sensorLight,sensorManager.SENSOR_DELAY_NORMAL);

    }
}
