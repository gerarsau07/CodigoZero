package com.example.codigozero; // <-- Confirma tu paquete

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class NivelUnoActivity extends AppCompatActivity {

    private GameView gameView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modo inmersivo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 1. Cargamos el archivo XML
        setContentView(R.layout.activity_nivel_uno);

        // 2. Buscamos el GameView dentro del XML
        gameView = findViewById(R.id.gameView);
    }

    // Gestionamos las pausas igual que antes
    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pausar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.reanudar();
        }
    }
}