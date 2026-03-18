package com.example.codigozero;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResumenDosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_resumen_dos);

        TextView tvTituloTSU = findViewById(R.id.tvTituloTSU);
        Button btnVolverMenuTSU = findViewById(R.id.btnVolverMenuTSU);

        int carreraElegida = getIntent().getIntExtra("CARRERA_SELECCIONADA", 0);

        // Personalizamos el título de TSU
        if (carreraElegida == 0) {
            tvTituloTSU.setText("ERES T.S.U. EN TECNOLOGÍAS DE LA INFORMACIÓN");
        } else if (carreraElegida == 1) {
            tvTituloTSU.setText("ERES T.S.U. EN INGENIERÍA FINANCIERA");
        } else if (carreraElegida == 2) {
            tvTituloTSU.setText("ERES T.S.U. EN BIOTECNOLOGÍA");
        }

        btnVolverMenuTSU.setOnClickListener(v -> {
            Intent intent = new Intent(ResumenDosActivity.this, MenuPrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}