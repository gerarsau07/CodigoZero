package com.example.codigozero; // <-- REVISA QUE ESTE SEA TU PAQUETE

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ResumenActivity extends AppCompatActivity {

    // Declaramos los elementos de la interfaz
    private TextView tvNivelCompletado;
    private TextView tvMat1, tvMat2, tvMat3, tvMat4;
    private Button btnAvanzarNivel;

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
        setContentView(R.layout.activity_resumen);

        // --- 1. VINCULAMOS LOS ELEMENTOS DEL DISEÑO XML ---
        tvNivelCompletado = findViewById(R.id.tvNivelCompletado);
        tvMat1 = findViewById(R.id.tvMat1);
        tvMat2 = findViewById(R.id.tvMat2);
        tvMat3 = findViewById(R.id.tvMat3);
        tvMat4 = findViewById(R.id.tvMat4);
        btnAvanzarNivel = findViewById(R.id.btnAvanzarNivel);

        // --- 2. RECIBIMOS LA CARRERA QUE VIENE DESDE EL JUEGO ---
        // Si por alguna razón no llega el dato, el valor por defecto será 0 (T.I.)
        int carreraElegida = getIntent().getIntExtra("CARRERA_SELECCIONADA", 0);

        // --- 3. CAMBIAMOS LOS TEXTOS DEPENDIENDO DE LA CARRERA ---
        if (carreraElegida == 0) {
            // TECNOLOGÍAS DE LA INFORMACIÓN (Basado en tu boceto)
            tvNivelCompletado.setText("NIVEL 1 COMPLETADO: RUTA T.I.");

            tvMat1.setText("MATEMÁTICAS:\nFundamentos Matemáticos, Cálculo Diferencial, Cálculo Integral.");
            tvMat2.setText("PROGRAMACIÓN:\nFundamentos de Programación, Programación Estructurada, P.O.O.");
            tvMat3.setText("REDES Y BASES:\nFundamentos de Redes, Conmutación, Bases de Datos.");
            tvMat4.setText("FORMACIÓN HUMANA:\nDesarrollo Humano, Habilidades Socioemocionales, Pensamiento.");

        } else if (carreraElegida == 1) {
            // INGENIERÍA FINANCIERA
            tvNivelCompletado.setText("NIVEL 1 COMPLETADO: RUTA FINANCIERA");

            tvMat1.setText("MATEMÁTICAS:\nÁlgebra Lineal, Cálculo Financiero, Probabilidad y Estadística.");
            tvMat2.setText("FINANZAS:\nFundamentos de Finanzas, Contabilidad Básica, Microeconomía.");
            tvMat3.setText("HERRAMIENTAS:\nAnálisis de Datos Económicos, Ofimática Empresarial.");
            tvMat4.setText("FORMACIÓN HUMANA:\nDesarrollo Humano, Ética Profesional, Comunicación Asertiva.");

        } else if (carreraElegida == 2) {
            // BIOTECNOLOGÍA
            tvNivelCompletado.setText("NIVEL 1 COMPLETADO: RUTA BIOTECNOLOGÍA");

            tvMat1.setText("CIENCIAS EXACTAS:\nQuímica General, Matemáticas Básicas, Física Aplicada.");
            tvMat2.setText("BIOLOGÍA:\nBiología Celular, Microbiología General, Bioquímica.");
            tvMat3.setText("LABORATORIO:\nIntroducción a la Biotecnología, Seguridad y Procesos.");
            tvMat4.setText("FORMACIÓN HUMANA:\nDesarrollo Humano, Ética Científica, Metodología de la Investigación.");
        }

        // --- 4. ACCIÓN DEL BOTÓN DE AVANZAR ---
        btnAvanzarNivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Viajamos al Nivel 2 llevando la carrera seleccionada
                Intent intent = new Intent(ResumenActivity.this, NivelDosActivity.class);
                intent.putExtra("CARRERA_SELECCIONADA", carreraElegida);
                startActivity(intent);
                finish(); // Cerramos el resumen
            }
        });
    }
}