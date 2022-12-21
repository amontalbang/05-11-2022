package com.example.a05_11_2022;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabase {

    private static FirebaseFirestore mFirestore;
    private static List<PuntModelo> puntuaciones;

    public static void mostrarPunts() {

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
                Puntuaciones puntClass = new Puntuaciones();
                puntClass.setTopTable(puntuaciones);
            }
        });
    }

    public static void orderData(QuerySnapshot data){

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

    public static void setTopTen(List<Map<String, Object>> orderedList){

        List<PuntModelo> topTen = new ArrayList<PuntModelo>();

        for(int i = 0; i < 10; i++){
            PuntModelo puntuacion = new PuntModelo(i + 1, orderedList.get(i).get("nombre").toString(), Integer.parseInt(orderedList.get(i).get("puntuacion").toString()));
            topTen.add(puntuacion);
            Log.d("setTopTen_puntuacion", puntuacion.getNombre());
        }
        puntuaciones = topTen;
    }
}

