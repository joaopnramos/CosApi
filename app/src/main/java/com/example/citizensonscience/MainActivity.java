package com.example.citizensonscience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.citizensonscience.Netwowk.RetrofitClient;
import com.example.citizensonscience.classes.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPassword, editTextUsername;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPassword = findViewById(R.id.password);
        editTextUsername = findViewById(R.id.username);

        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }
    private void userLogin(){
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty()){
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        Call<LoginResponse> call = RetrofitClient.getmInstance().getApi().login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //Caso exista uma resposta

                if(response.code() == 200) {

                    //Caso o login seja positivo
                    String token = response.body().getToken();
                    String id = response.body().getId();
                    SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.putString("id", id);
                    editor.commit();
                    Intent info = new Intent(getApplicationContext(), DataCollector.class);
                    startActivity(info);
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_LONG).show();
                    System.out.println(response);

                }else{

                    //Caso o login seja negativo
                    Intent info = new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(info);
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //Caso não exista resposta por parte do servidor
                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());


            }
        });



    }

    @Override
    public void onClick(View view) {
        userLogin();
    }


}