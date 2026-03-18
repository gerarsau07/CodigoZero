package com.example.codigozero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        CardView btnTest = findViewById(R.id.btn_ir_test);
        CardView btnNivel0 = findViewById(R.id.btn_ir_nivel0);
        CardView btnNivel1 = findViewById(R.id.btn_ir_nivel1);

        // --- 1. Botón del Test Vocacional ---
        btnTest.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, TestVocacionalActivity.class);
            startActivity(intent);
        });

        // --- 2. Botón para ir al Nivel 0 (Modo Historia Completo) ---
        btnNivel0.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, NivelZeroActivity.class);
            startActivity(intent);
        });

        // --- 3. Botón para ir al Nivel 1 (Selección de Skin/Ruta) ---
        btnNivel1.setOnClickListener(v -> {
            // Ahora te manda directamente a la pantalla donde eliges tu avatar
            Intent intent = new Intent(MenuPrincipalActivity.this, Seleccion.class);
            startActivity(intent);
        });
    }
}