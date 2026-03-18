package com.example.codigozero;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class NivelTresActivity extends AppCompatActivity {

    private GameView4 gameView4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_nivel_tres);
        gameView4 = findViewById(R.id.gameView4);

        int carreraElegida = getIntent().getIntExtra("CARRERA_SELECCIONADA", 0);
        if (gameView4 != null) gameView4.setSkinYCarrera(carreraElegida);
    }

    @Override protected void onPause() { super.onPause(); if (gameView4 != null) gameView4.pausar(); }
    @Override protected void onResume() { super.onResume(); if (gameView4 != null) gameView4.reanudar(); }
}