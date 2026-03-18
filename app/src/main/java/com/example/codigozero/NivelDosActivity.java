package com.example.codigozero;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class NivelDosActivity extends AppCompatActivity {

    private GameView3 gameView3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_nivel_dos);
        gameView3 = findViewById(R.id.gameView3);

        int carreraElegida = getIntent().getIntExtra("CARRERA_SELECCIONADA", 0);
        if (gameView3 != null) gameView3.setSkinYCarrera(carreraElegida);
    }

    @Override protected void onPause() { super.onPause(); if (gameView3 != null) gameView3.pausar(); }
    @Override protected void onResume() { super.onResume(); if (gameView3 != null) gameView3.reanudar(); }
}