package com.example.codigozero;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultadoVocacionalActivity extends AppCompatActivity {

    // Reemplaza el contenido de tu onCreate en ResultadoVocacionalActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_vocacional);

        TextView txtCarrera = findViewById(R.id.txt_carrera_ganadora);
        LinearLayout layoutDetalles = findViewById(R.id.layout_detalles);
        Button btnVolver = findViewById(R.id.btn_volver);

        Bundle datos = getIntent().getExtras();
        if (datos != null) {
            Map<String, Integer> resultados = new LinkedHashMap<>(); // Usar LinkedHashMap para mantener orden
            resultados.put("Mecatrónica", datos.getInt("meca"));
            resultados.put("Biotecnología", datos.getInt("bio"));
            resultados.put("Automotriz", datos.getInt("auto"));
            resultados.put("T. Información", datos.getInt("tis"));
            resultados.put("Industrial", datos.getInt("ind"));
            resultados.put("Financiera", datos.getInt("fin"));
            resultados.put("Electrónica", datos.getInt("elec"));

            String carreraIdeal = "";
            int maxPuntos = -1;

            // Primero buscamos el ganador
            for (Map.Entry<String, Integer> entry : resultados.entrySet()) {
                if (entry.getValue() > maxPuntos) {
                    maxPuntos = entry.getValue();
                    carreraIdeal = entry.getKey();
                }
            }
            txtCarrera.setText(carreraIdeal);

            // Luego creamos las barras de forma dinámica
            for (Map.Entry<String, Integer> entry : resultados.entrySet()) {
                View itemView = getLayoutInflater().inflate(R.layout.item_resultado_barra, null);
                TextView nombre = itemView.findViewById(R.id.nombre_carrera);
                TextView porcentaje = itemView.findViewById(R.id.porcentaje_texto);
                ProgressBar barra = itemView.findViewById(R.id.barra_progreso);

                nombre.setText(entry.getKey());

                // Suponiendo un máximo de 50 puntos posibles (ajusta según tus preguntas)
                int valorProgreso = Math.min(entry.getValue() * 2, 100);
                barra.setProgress(valorProgreso);
                porcentaje.setText(valorProgreso + "%");

                layoutDetalles.addView(itemView);
            }
        }

        btnVolver.setOnClickListener(v -> finish());
    }
}