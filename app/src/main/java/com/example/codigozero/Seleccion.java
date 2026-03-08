package com.example.codigozero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Seleccion extends AppCompatActivity {

    private Spinner spinnerCarreras;
    private ImageView imgAvatarGrande;
    private Button btnIniciarRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- MODO INMERSIVO (Pantalla completa sin barra superior) ---
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seleccion);

        // --- VINCULAMOS LOS ELEMENTOS DEL DISEÑO ---
        spinnerCarreras = findViewById(R.id.spinnerCarreras);
        imgAvatarGrande = findViewById(R.id.imgAvatarGrande);
        btnIniciarRuta = findViewById(R.id.btnIniciarRuta);

        // --- 1. CONFIGURAMOS LAS OPCIONES DEL SPINNER (LISTA DESPLEGABLE) ---
        String[] opciones = {"Tecnologías de la Información", "Ingeniería Financiera", "Biotecnología"};

        // Creamos un adaptador para pasar los textos al Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinnerCarreras.setAdapter(adapter);

        // --- 2. EVENTO: CAMBIAR LA IMAGEN CUANDO SE SELECCIONA UNA CARRERA ---
        spinnerCarreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 = TI, 1 = Financiera, 2 = Biotecnología
                if (position == 0) {
                    imgAvatarGrande.setImageResource(R.drawable.avatar_ti);
                } else if (position == 1) {
                    imgAvatarGrande.setImageResource(R.drawable.avatar_financiera);
                } else if (position == 2) {
                    imgAvatarGrande.setImageResource(R.drawable.avatar_bio);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacemos nada si no hay selección
            }
        });

        // --- 3. EVENTO: BOTÓN DE "INICIAR PROTOCOLO" PARA IR AL JUEGO ---
        btnIniciarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos qué número de carrera está seleccionado (0, 1 o 2)
                int seleccionActual = spinnerCarreras.getSelectedItemPosition();

                // Preparamos el viaje hacia la pantalla del Nivel 1
                Intent intent = new Intent(Seleccion.this, NivelUnoActivity.class);

                // "Empacamos" la carrera elegida para que el Nivel 1 sepa qué skin usar
                intent.putExtra("CARRERA_SELECCIONADA", seleccionActual);

                // Iniciamos el nivel
                startActivity(intent);

                // Cerramos esta pantalla para que el jugador no pueda regresar aquí con el botón de "Atrás" del celular
                finish();
            }
        });
    }
}