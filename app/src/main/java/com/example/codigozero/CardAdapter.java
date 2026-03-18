package com.example.codigozero; // <-- ASEGÚRATE QUE SEA TU PAQUETE

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<PreguntaVocacional> preguntas;

    public CardAdapter(List<PreguntaVocacional> preguntas) {
        this.preguntas = preguntas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí inflamos el diseño item_card.xml que creamos en el Paso 3
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Tomamos la pregunta de la lista y la ponemos en el TextView de la carta
        PreguntaVocacional pregunta = preguntas.get(position);
        holder.preguntaTexto.setText(pregunta.getTextoPregunta());
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView preguntaTexto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencia al ID que pusimos en item_card.xml
            preguntaTexto = itemView.findViewById(R.id.item_pregunta);
        }
    }
}