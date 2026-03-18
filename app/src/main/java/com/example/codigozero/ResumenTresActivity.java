package com.example.codigozero;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResumenTresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_resumen_tres);

        TextView tvTituloIngeniero = findViewById(R.id.tvTituloIngeniero);
        TextView tvMensajeFinal = findViewById(R.id.tvMensajeFinal);
        Button btnVolverMenuFin = findViewById(R.id.btnVolverMenuFin);

        int carreraElegida = getIntent().getIntExtra("CARRERA_SELECCIONADA", 0);

        if (carreraElegida == 0) {
            tvTituloIngeniero.setText("INGENIERÍA EN TECNOLOGÍAS DE LA INFORMACIÓN");
            tvMensajeFinal.setText("Has dominado el desarrollo web, la inteligencia artificial y la arquitectura de sistemas. ¡Tus proyectos están listos para revolucionar la industria tecnológica!");
        } else if (carreraElegida == 1) {
            tvTituloIngeniero.setText("INGENIERÍA FINANCIERA");
            tvMensajeFinal.setText("Has dominado los mercados, la gestión de riesgos y la estructuración de capital. ¡Estás listo para liderar el sector corporativo y económico!");
        } else if (carreraElegida == 2) {
            tvTituloIngeniero.setText("INGENIERÍA EN BIOTECNOLOGÍA");
            tvMensajeFinal.setText("Has dominado la biología molecular, los biorreactores y la genética. ¡Estás listo para innovar en la ciencia y salvar el mundo!");
        }

        btnVolverMenuFin.setOnClickListener(v -> {
            Intent intent = new Intent(ResumenTresActivity.this, MenuPrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}