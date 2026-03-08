package com.example.codigozero; // <-- REVISA QUE ESTE SEA TU PAQUETE

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class NivelZeroActivity extends AppCompatActivity {

    // Instanciamos el motor original para el Nivel 0
    private GameView gameView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- MODO INMERSIVO (Pantalla completa sin barra superior) ---
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 1. Cargamos el archivo XML
        setContentView(R.layout.activity_nivel_zero);

        // 2. Buscamos el GameView dentro del XML
        gameView = findViewById(R.id.gameViewNivel0);
    }

    // Gestionamos las pausas para que el hilo del juego se detenga si se minimiza la app
    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pausar();
        }
    }

    // Reanudamos el juego si el usuario vuelve a la app
    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.reanudar();
        }
    }
}