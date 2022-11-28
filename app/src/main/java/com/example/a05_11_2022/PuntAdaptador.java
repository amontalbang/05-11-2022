package com.example.a05_11_2022;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PuntAdaptador extends RecyclerView.Adapter<PuntAdaptador.PuntViewHolder> {
    private List<PuntModelo> items;

    public static class PuntViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView posicion;
        public TextView nombre;
        public TextView puntuacion;

        public PuntViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre);
            puntuacion = (TextView) v.findViewById(R.id.puntuacion);
            posicion = (TextView) v.findViewById(R.id.posicion);
        }
    }

    public PuntAdaptador(List<PuntModelo> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PuntViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.puntuaciones_card, viewGroup, false);
        return new PuntViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PuntViewHolder viewHolder, int i) {
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.puntuacion.setText("Tiempo: "+ String.valueOf(items.get(i).getTiempo()) + " seg");
        viewHolder.posicion.setText("Posición "+ (i + 1));
    }
}