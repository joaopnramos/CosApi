package com.example.citizensonscience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class DataCollector extends AppCompatActivity {
    private TextView id;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collector);

        id = findViewById(R.id.textView2);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        String ric = preferences.getString("token", "Não encontrado");






    }
}