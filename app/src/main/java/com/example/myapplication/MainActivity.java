package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Evento para el botón de menú
        toolbar.setNavigationOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Evento para ítems del menú
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_inicio) {
                Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_perfil) {
                Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_config) {
                Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        Button btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "¡Iniciando Nivel 1: El Código Base!", Toast.LENGTH_SHORT).show();

            // Cuando creemos la siguiente pantalla (GameActivity), descomenta esto:
            // Intent intent = new Intent(MainActivity.this, GameActivity.class);
            // startActivity(intent);
        });
    }
}