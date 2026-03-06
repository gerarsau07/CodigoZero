package com.example.codigozero; // <-- REVISA QUE ESTE SEA TU PAQUETE

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Seleccion extends AppCompatActivity {

    private Spinner spinnerCarreras;
    private ImageView imgAvatarGrande;
    private Button btnIniciarRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- MODO INMERSIVO (Pantalla completa) ---
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
                    imgAvatarGrande.setImageResource(R.drawable.avatarTI);
                } else if (position == 1) {
                    imgAvatarGrande.setImageResource(R.drawable.avatarFinaciera); // Cuidado aquí: avatarFinaciera o avatarFinanciera según lo guardaste
                } else if (position == 2) {
                    imgAvatarGrande.setImageResource(R.drawable.avatarBio);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacemos nada si no hay selección
            }
        });

        // --- 3. EVENTO: BOTÓN DE "INICIAR PROTOCOLO" ---
        btnIniciarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Averiguamos cuál opción está seleccionada actualmente en el Spinner
                int seleccionActual = spinnerCarreras.getSelectedItemPosition();

                if (seleccionActual == 0) {
                    Toast.makeText(Seleccion.this, "Iniciando protocolo de T.I. e Innovación...", Toast.LENGTH_SHORT).show();
                    // Aquí irá el Intent al juego de T.I.
                } else if (seleccionActual == 1) {
                    Toast.makeText(Seleccion.this, "Cargando algoritmos Financieros...", Toast.LENGTH_SHORT).show();
                    // Aquí irá el Intent al juego de Financiera
                } else if (seleccionActual == 2) {
                    Toast.makeText(Seleccion.this, "Analizando secuencias de Biotecnología...", Toast.LENGTH_SHORT).show();
                    // Aquí irá el Intent al juego de Biotecnología
                }
            }
        });
    }
}