package com.example.a05_11_2022;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Puntuaciones extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_puntuaciones, container, false);

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        ConexionSQLite conexionSQLite = new ConexionSQLite(getContext());

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new PuntAdaptador(conexionSQLite.mostrarPunt());
        recycler.setAdapter(adapter);

        Button button = (Button) view.findViewById(R.id.button_volver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_puntuaciones_to_FirstFragment);
            }

        });
        return view;
    }
}