package com.example.codigozero; // Revisa tu paquete

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        Button btnCompletarRegistro = findViewById(R.id.btnCompletarRegistro);

        btnCompletarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                String correo = etCorreo.getText().toString().trim();

                if (nombre.isEmpty() || correo.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Faltan datos obligatorios.", Toast.LENGTH_SHORT).show();
                } else {
                    // Viajamos al Nivel Uno
                    Intent intent = new Intent(RegistroActivity.this, NivelZeroActivity.class);
                    startActivity(intent);
                    finish(); // Cierra el registro
                }
            }
        });
    }
}