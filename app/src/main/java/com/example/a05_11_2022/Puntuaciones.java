package com.example.a05_11_2022;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Puntuaciones extends Fragment {

    private RecyclerView recycler;
    private PuntAdaptador adapter;
    private RecyclerView.LayoutManager lManager;
    private ArrayList<PuntModelo> puntuacionList = new ArrayList<PuntModelo>();
    private static FirebaseFirestore mFirestore;
    private static List<PuntModelo> puntuaciones;

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
        //adapter = new PuntAdaptador(FirebaseDatabase.mostrarPunts());
        //recycler.setAdapter(adapter);

        adapter = new PuntAdaptador(puntuacionList);
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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mostrarPunts();
    }

    public void setTopTable(List<PuntModelo> puntuaciones){
        /*adapter = new PuntAdaptador(puntuaciones);
        recycler.setAdapter(adapter);
        adapter = new PuntAdaptador(new ArrayList<PuntModelo>());
        recycler.setAdapter(adapter);*/
        puntuacionList = (ArrayList) puntuaciones;
        if (adapter == null) {
            Log.d("Mensaje", "adapter null");
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void mostrarPunts() {

        mFirestore = FirebaseFirestore.getInstance();
        CollectionReference puntsRef = mFirestore.collection("Puntuaciones");
        //Query puntsRef = mFirestore.collection("Puntuaciones").orderBy("name", Query.Direction.ASCENDING).limit(10);
        //puntsRef.orderBy("name", Query.Direction.ASCENDING).limit(10);

        puntsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        orderData(queryDocumentSnapshots);
                    }
                });
    }

    public void orderData(QuerySnapshot data){

        List<Map<String, Object>> top = new ArrayList<Map<String, Object>>();

        for (QueryDocumentSnapshot snap : data) {
            if (top.size() == 0) {
                top.add(snap.getData());
            } else {
                for (Map<String, Object> registro : top) {
                    if (Integer.parseInt(registro.get("puntuacion").toString()) > Integer.parseInt(snap.getData().get("puntuacion").toString())) {
                        top.add(top.indexOf(registro), snap.getData());
                        break;
                    }
                }
                if(!top.contains(snap.getData())){
                    top.add(snap.getData());
                }
            }
        }
        for (Map<String, Object> registro : top) {
            Log.d("registro", registro.get("puntuacion").toString());
        }
        setTopTen(top);
    }

    public void setTopTen(List<Map<String, Object>> orderedList){

        List<PuntModelo> topTen = new ArrayList<PuntModelo>();

        for(int i = 0; i < 10; i++){
            PuntModelo puntuacion = new PuntModelo(i + 1, orderedList.get(i).get("nombre").toString(), Integer.parseInt(orderedList.get(i).get("puntuacion").toString()));
            puntuacionList.add(puntuacion);
            Log.d("Mensaje", puntuacion.getNombre());
        }
        adapter = new PuntAdaptador(puntuacionList);
        recycler.setAdapter(adapter);
        recycler.invalidate();
    }
}