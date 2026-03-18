package com.example.codigozero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

public class TestVocacionalActivity extends AppCompatActivity implements CardStackListener {

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private List<PreguntaVocacional> mazoPreguntas;

    // Variables para acumular los puntos de cada carrera
    private int totalMeca = 0, totalBio = 0, totalAuto = 0, totalTIs = 0,
            totalInd = 0, totalFin = 0, totalElec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_vocacional);

        cardStackView = findViewById(R.id.card_stack_view);

        // Inicializar el mazo de preguntas
        crearMazo();

        // Configurar el Manager de las cartas
        manager = new CardStackLayoutManager(this, this);
        manager.setCanScrollVertical(false); // Solo movimiento Izquierda/Derecha

        // Asignar el manager y el adaptador
        CardAdapter adapter = new CardAdapter(mazoPreguntas);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private void crearMazo() {
        mazoPreguntas = new ArrayList<>();
        // Los puntos se asignan en orden: Meca, Bio, Auto, TIs, Ind, Fin, Elec

        // Preguntas Originales
        mazoPreguntas.add(new PreguntaVocacional("¿Te apasiona desarmar aparatos para ver cómo funcionan?", 10, 0, 5, 0, 0, 0, 8));
        mazoPreguntas.add(new PreguntaVocacional("¿Te gustaría crear software para inteligencia artificial?", 0, 0, 0, 10, 2, 0, 0));
        mazoPreguntas.add(new PreguntaVocacional("¿Te interesa el estudio de microorganismos y genética?", 0, 10, 0, 0, 0, 0, 0));
        mazoPreguntas.add(new PreguntaVocacional("¿Te gustaría optimizar los procesos de una fábrica?", 2, 0, 3, 0, 10, 5, 0));
        mazoPreguntas.add(new PreguntaVocacional("¿Te ves analizando mercados financieros e inversiones?", 0, 0, 0, 0, 0, 10, 0));

        // --- NUEVAS PREGUNTAS ---
        mazoPreguntas.add(new PreguntaVocacional("¿Sueles interesarte por el diseño, ensamblaje y motores de los vehículos?", 5, 0, 10, 0, 0, 0, 2));
        mazoPreguntas.add(new PreguntaVocacional("¿Te gusta soldar y programar tus propios circuitos electrónicos?", 5, 0, 0, 2, 0, 0, 10));
        mazoPreguntas.add(new PreguntaVocacional("¿Te interesa trabajar en laboratorios para desarrollar nuevos medicamentos o alimentos?", 0, 10, 0, 0, 0, 0, 0));
        mazoPreguntas.add(new PreguntaVocacional("¿Eres bueno liderando equipos y organizando los tiempos de un proyecto grande?", 2, 0, 0, 0, 10, 5, 0));
        mazoPreguntas.add(new PreguntaVocacional("¿Te gustaría proteger redes contra hackers y manejar bases de datos gigantes?", 0, 0, 0, 10, 0, 0, 0));
    }

    // --- MÉTODOS DEL CARDSTACKLISTENER ---
    @Override
    public void onCardSwiped(Direction direction) {
        int posicion = manager.getTopPosition() - 1;
        PreguntaVocacional preguntaActual = mazoPreguntas.get(posicion);

        if (direction == Direction.Right) {
            // SI DESLIZA A LA DERECHA (SÍ), SUMAMOS LOS PUNTOS
            totalMeca += preguntaActual.getPtsMecatronica();
            totalBio += preguntaActual.getPtsBiotecnologia();
            totalAuto += preguntaActual.getPtsAutomotriz();
            totalTIs += preguntaActual.getPtsTIs();
            totalInd += preguntaActual.getPtsIndustrial();
            totalFin += preguntaActual.getPtsFinanciera();
            totalElec += preguntaActual.getPtsElectronica();

            Toast.makeText(this, "¡Interesante!", Toast.LENGTH_SHORT).show();
        } else if (direction == Direction.Left) {
            Toast.makeText(this, "Paso...", Toast.LENGTH_SHORT).show();
        }

        // Si ya no quedan más cartas, calculamos el resultado
        if (manager.getTopPosition() == mazoPreguntas.size()) {
            mostrarResultadoFinal();
        }
    }

    private void mostrarResultadoFinal() {
        Intent intent = new Intent(this, ResultadoVocacionalActivity.class);
        // Pasamos todos los acumuladores
        intent.putExtra("meca", totalMeca);
        intent.putExtra("bio", totalBio);
        intent.putExtra("auto", totalAuto);
        intent.putExtra("tis", totalTIs);
        intent.putExtra("ind", totalInd);
        intent.putExtra("fin", totalFin);
        intent.putExtra("elec", totalElec);

        startActivity(intent);
        finish();
    }

    @Override public void onCardDragging(Direction direction, float ratio) {}
    @Override public void onCardRewound() {}
    @Override public void onCardCanceled() {}
    @Override public void onCardAppeared(View view, int position) {}
    @Override public void onCardDisappeared(View view, int position) {}
}