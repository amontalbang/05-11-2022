package com.example.a05_11_2022;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class PuzzleFirstFragment extends Fragment {

    private static int COLUMNAS;
    private static int DIMENSION;
    private static int mColumnWidth, mColumnHeight;
    private static boolean aumento;
    private static String[] titulo;
    private static DetectorGestos mGestos;
    private static View vista;
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public Context context;
    private static Tiempo timer;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (aumento == false){
            COLUMNAS = 3;
            DIMENSION = COLUMNAS * COLUMNAS;
        } else {
            COLUMNAS++;
            DIMENSION = COLUMNAS * COLUMNAS;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_puzzle_first, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Puzzle");

        vista = view;
        mGestos = (DetectorGestos) view.findViewById(R.id.grid);

        inicio();
        mezclar();
        setDimensiones();

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onPause() {
        super.onPause();

    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public WifiP2pManager.ActionListener acciones = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(int i) {

        }
    };

    private void setDimensiones() {

        ViewTreeObserver vto = mGestos.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGestos.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGestos.getMeasuredWidth();
                int displayHeight = mGestos.getMeasuredHeight();

                int statusbarHeight = getStatusBasHeight(getContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / COLUMNAS;
                mColumnHeight = requiredHeight / COLUMNAS;

                switch (COLUMNAS){
                    case 3:
                        mostrar(getContext());
                        break;
                    case 4:
                        mostrar1(getContext());
                        break;
                    case 5:
                        mostrar2(getContext());
                        break;
                }
            }
        });
    }

    private int getStatusBasHeight(Context context) {

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private void inicio() {
        timer.Contar();
        mGestos.setNumColumns(COLUMNAS);
        titulo = new String[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            titulo[i]  = String.valueOf(i);
        }
    }

    private void mezclar() {

        int index;
        String temp;
        Random aleatorio = new Random();

        for (int i = titulo.length - 1; i > 0; i--) {
            index = aleatorio.nextInt(i + 1);
            temp = titulo[index];
            titulo[index] = titulo[i];
            titulo[i] = temp;
        }
    }

    private static void mostrar(Context context) {

        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (int i = 0; i < titulo.length; i++) {
            button = new Button(context);

            if (titulo[i].equals("0"))
                button.setBackgroundResource(R.drawable.frame_1);
            else if (titulo[i].equals("1"))
                button.setBackgroundResource(R.drawable.frame_2);
            else if (titulo[i].equals("2"))
                button.setBackgroundResource(R.drawable.frame_3);
            else if (titulo[i].equals("3"))
                button.setBackgroundResource(R.drawable.frame_4);
            else if (titulo[i].equals("4"))
                button.setBackgroundResource(R.drawable.frame_5);
            else if (titulo[i].equals("5"))
                button.setBackgroundResource(R.drawable.frame_6);
            else if (titulo[i].equals("6"))
                button.setBackgroundResource(R.drawable.frame_7);
            else if (titulo[i].equals("7"))
                button.setBackgroundResource(R.drawable.frame_8);
            else if (titulo[i].equals("8"))
                button.setBackgroundResource(R.drawable.frame_9);

            buttons.add(button);
        }

        mGestos.setAdapter(new Adaptador(buttons, mColumnWidth, mColumnHeight));
    }

    private static void mostrar1(Context context) {

        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        Log.d("mostrar1", "mostrar1");
        for (int i = 0; i < titulo.length; i++) {
            button = new Button(context);

            if (titulo[i].equals("0"))
                button.setBackgroundResource(R.drawable.frame2_1);
            else if (titulo[i].equals("1"))
                button.setBackgroundResource(R.drawable.frame2_2);
            else if (titulo[i].equals("2"))
                button.setBackgroundResource(R.drawable.frame2_3);
            else if (titulo[i].equals("3"))
                button.setBackgroundResource(R.drawable.frame2_4);
            else if (titulo[i].equals("4"))
                button.setBackgroundResource(R.drawable.frame2_5);
            else if (titulo[i].equals("5"))
                button.setBackgroundResource(R.drawable.frame2_6);
            else if (titulo[i].equals("6"))
                button.setBackgroundResource(R.drawable.frame2_7);
            else if (titulo[i].equals("7"))
                button.setBackgroundResource(R.drawable.frame2_8);
            else if (titulo[i].equals("8"))
                button.setBackgroundResource(R.drawable.frame2_9);
            else if (titulo[i].equals("9"))
                button.setBackgroundResource(R.drawable.frame2_10);
            else if (titulo[i].equals("10"))
                button.setBackgroundResource(R.drawable.frame2_11);
            else if (titulo[i].equals("11"))
                button.setBackgroundResource(R.drawable.frame2_12);
            else if (titulo[i].equals("12"))
                button.setBackgroundResource(R.drawable.frame2_13);
            else if (titulo[i].equals("13"))
                button.setBackgroundResource(R.drawable.frame2_14);
            else if (titulo[i].equals("14"))
                button.setBackgroundResource(R.drawable.frame2_15);
            else if (titulo[i].equals("15"))
                button.setBackgroundResource(R.drawable.frame2_16);

            buttons.add(button);
        }

        mGestos.setAdapter(new Adaptador(buttons, mColumnWidth, mColumnHeight));
    }

    private static void mostrar2(Context context) {

        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (int i = 0; i < titulo.length; i++) {
            button = new Button(context);

            if (titulo[i].equals("0"))
                button.setBackgroundResource(R.drawable.frame3_1);
            else if (titulo[i].equals("1"))
                button.setBackgroundResource(R.drawable.frame3_2);
            else if (titulo[i].equals("2"))
                button.setBackgroundResource(R.drawable.frame3_3);
            else if (titulo[i].equals("3"))
                button.setBackgroundResource(R.drawable.frame3_4);
            else if (titulo[i].equals("4"))
                button.setBackgroundResource(R.drawable.frame3_5);
            else if (titulo[i].equals("5"))
                button.setBackgroundResource(R.drawable.frame3_6);
            else if (titulo[i].equals("6"))
                button.setBackgroundResource(R.drawable.frame3_7);
            else if (titulo[i].equals("7"))
                button.setBackgroundResource(R.drawable.frame3_8);
            else if (titulo[i].equals("8"))
                button.setBackgroundResource(R.drawable.frame3_9);
            else if (titulo[i].equals("9"))
                button.setBackgroundResource(R.drawable.frame3_10);
            else if (titulo[i].equals("10"))
                button.setBackgroundResource(R.drawable.frame3_11);
            else if (titulo[i].equals("11"))
                button.setBackgroundResource(R.drawable.frame3_12);
            else if (titulo[i].equals("12"))
                button.setBackgroundResource(R.drawable.frame3_13);
            else if (titulo[i].equals("13"))
                button.setBackgroundResource(R.drawable.frame3_14);
            else if (titulo[i].equals("14"))
                button.setBackgroundResource(R.drawable.frame3_15);
            else if (titulo[i].equals("15"))
                button.setBackgroundResource(R.drawable.frame3_16);
            else if (titulo[i].equals("16"))
                button.setBackgroundResource(R.drawable.frame3_17);
            else if (titulo[i].equals("17"))
                button.setBackgroundResource(R.drawable.frame3_18);
            else if (titulo[i].equals("18"))
                button.setBackgroundResource(R.drawable.frame3_19);
            else if (titulo[i].equals("19"))
                button.setBackgroundResource(R.drawable.frame3_20);
            else if (titulo[i].equals("20"))
                button.setBackgroundResource(R.drawable.frame3_21);
            else if (titulo[i].equals("21"))
                button.setBackgroundResource(R.drawable.frame3_22);
            else if (titulo[i].equals("22"))
                button.setBackgroundResource(R.drawable.frame3_23);
            else if (titulo[i].equals("23"))
                button.setBackgroundResource(R.drawable.frame3_24);
            else if (titulo[i].equals("24"))
                button.setBackgroundResource(R.drawable.frame3_25);

            buttons.add(button);
        }

        mGestos.setAdapter(new Adaptador(buttons, mColumnWidth, mColumnHeight));
    }


    private void movimiento(Context context, int posicion, int movimiento) {

        String newPosicion = titulo[posicion + movimiento];
        titulo[posicion + movimiento] = titulo[posicion];
        titulo[posicion] = newPosicion;

        switch (COLUMNAS){
            case 3:
                mostrar(context);
                break;
            case 4:
                mostrar1(context);
                break;
            case 5:
                mostrar2(context);
                break;
        }

        if (resuelto()) {
            timer.Detener();
            String tiempo = Integer.toString(timer.getSegundos());
            Log.d("Tiempo empleado", tiempo);
            goToPantalla();
        }
    }

    private void goToPantalla(){

        switch (COLUMNAS){
            case 3:
                Navigation.findNavController(vista).navigate(R.id.action_puzzleFirstFragment_to_trans1);
                break;
            case 4:
                Navigation.findNavController(vista).navigate(R.id.action_puzzleFirstFragment_to_trans2);
                break;
            case 5:
                Navigation.findNavController(vista).navigate(R.id.action_puzzleFirstFragment_to_trans3);
                break;
        }
    }


    private static boolean resuelto() {

        boolean resuelto = false;
        Log.d("movimiento", "movimiento");

        for (int i = 0; i < titulo.length; i++) {
            if (titulo[i].equals(String.valueOf(i))) {
                resuelto = true;
            } else {
                Log.d("resuelto", " no resuelto");
                resuelto = false;
                break;
            }
        }
        aumento = resuelto;
        return resuelto;
    }

    public void moverCeldas(Context context, String direccion, int posicion) {

        //celda arriba-izq
        if (posicion == 0) {
            if (direccion.equals("right")) {
                movimiento(context, posicion, 1);
            } else if (direccion.equals("down")) {
                movimiento(context, posicion, COLUMNAS);
            } else {
                Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            }
            //celda arriba-centro
        } else if (posicion > 0 && posicion < COLUMNAS -1) {
            if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(DOWN)) movimiento(context, posicion, COLUMNAS);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            //celda arriba-derecha
        } else if (posicion == COLUMNAS -1) {
            if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(DOWN)) movimiento(context, posicion, COLUMNAS);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            //celdas izquierdas
        } else if (posicion > COLUMNAS -1 && posicion < DIMENSION - COLUMNAS && posicion % COLUMNAS == 0) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else if (direccion.equals(DOWN)) movimiento(context, posicion, COLUMNAS);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            //lado derecho y celdas esquina derecha
        } else if (posicion == COLUMNAS * 2 - 1 || posicion == COLUMNAS *3 - 1) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(DOWN)) {

                if (posicion <= DIMENSION - COLUMNAS -1) movimiento(context, posicion, COLUMNAS);
                else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            //
        } else if (posicion == DIMENSION - COLUMNAS) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            //
        } else if (posicion < DIMENSION - 1 && posicion > DIMENSION - COLUMNAS) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            //
        } else {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else movimiento(context, posicion, COLUMNAS);
        }
    }
}

