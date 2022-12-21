package com.example.a05_11_2022;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.util.ArrayList;
import java.util.Random;

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
    private static ArrayList<Bitmap> pieces;
    private static int tiempo;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(COLUMNAS == 5){
            aumento = false;
        }
        if (aumento == false){
            timer = new Tiempo();
            COLUMNAS = 3;
        } else {
            COLUMNAS++;
        }
        DIMENSION = COLUMNAS * COLUMNAS;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_puzzle_first, container, false);

        vista = view;
        ImageView image = new ImageView(getContext());
        image = MainActivity.getImage();
        // si no se recupera imagen meter la imagen desde el proyecto en funcion de las columnas
        mGestos = (DetectorGestos) view.findViewById(R.id.grid);

        setPieces(image);
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

                buildPuzzle(getContext());
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

    // Inicio y montaje del puzzle
    private void inicio() {
        mGestos.setNumColumns(COLUMNAS);
        titulo = new String[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            titulo[i]  = String.valueOf(i);
        }
        if (aumento == false){
            timer.Contar();
        } else{
            timer.Continuar();
        }
    }

    // Mezclado de piezas
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

    // Mostrado de puzzle en el grid
    private static void buildPuzzle(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (int i = 0; i < titulo.length; i++) {
            button = new Button(context);
            int index = Integer.parseInt(titulo[i]);
            Drawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), pieces.get(index));
            button.setBackground(drawable);
            buttons.add(button);
        }
        mGestos.setAdapter(new Adaptador(buttons, mColumnWidth, mColumnHeight));
    }

    //
    private void movimiento(Context context, int posicion, int movimiento) {
        String newPosicion = titulo[posicion + movimiento];
        titulo[posicion + movimiento] = titulo[posicion];
        titulo[posicion] = newPosicion;

        buildPuzzle(context);

        if (resuelto()) {
            timer.Detener();
            tiempo = timer.getSegundos();
            goToPantalla();
        }
    }

    // Metodo de cambio de pantalla al realizar el puzzle
    private void goToPantalla(){

        switch (COLUMNAS){
            case 3:
                Navigation.findNavController(vista).navigate(R.id.action_puzzleFirstFragment_to_trans1);
                break;
            case 4:
                Navigation.findNavController(vista).navigate(R.id.action_puzzleFirstFragment_to_trans2);
                break;
            case 5:
                Bundle bundle = new Bundle();
                bundle.putInt("score", tiempo);
                Navigation.findNavController(vista).navigate(R.id.action_puzzleFirstFragment_to_trans3, bundle);
                break;
        }
    }

    // Comprobacion de puzzle resuelto
    private static boolean resuelto() {

        boolean resuelto = false;

        for (int i = 0; i < titulo.length; i++) {
            if (titulo[i].equals(String.valueOf(i))) {
                resuelto = true;
            } else {
                resuelto = false;
                break;
            }
        }
        aumento = resuelto;
        return resuelto;
    }

    // Logica para movimiento de las piezas
    public void moverCeldas(Context context, String direccion, int posicion) {

        if (posicion == 0) {
            if (direccion.equals("right")) {
                movimiento(context, posicion, 1);
            } else if (direccion.equals("down")) {
                movimiento(context, posicion, COLUMNAS);
            } else {
                Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            }

        } else if (posicion > 0 && posicion < COLUMNAS -1) {
            if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(DOWN)) movimiento(context, posicion, COLUMNAS);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();

        } else if (posicion == COLUMNAS -1) {
            if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(DOWN)) movimiento(context, posicion, COLUMNAS);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();

        } else if (posicion > COLUMNAS -1 && posicion < DIMENSION - COLUMNAS && posicion % COLUMNAS == 0) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else if (direccion.equals(DOWN)) movimiento(context, posicion, COLUMNAS);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();

        } else if (posicion == COLUMNAS * 2 - 1 || posicion == COLUMNAS *3 - 1) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(DOWN)) {

                if (posicion <= DIMENSION - COLUMNAS -1) movimiento(context, posicion, COLUMNAS);
                else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();

        } else if (posicion == DIMENSION - COLUMNAS) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();

        } else if (posicion < DIMENSION - 1 && posicion > DIMENSION - COLUMNAS) {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else Toast.makeText(context, "Movimiento NO Válido", Toast.LENGTH_SHORT).show();

        } else {
            if (direccion.equals(UP)) movimiento(context, posicion, -COLUMNAS);
            else if (direccion.equals(LEFT)) movimiento(context, posicion, -1);
            else if (direccion.equals(RIGHT)) movimiento(context, posicion, 1);
            else movimiento(context, posicion, COLUMNAS);
        }
    }

    // Método que recoge las fotos
    private void setPieces(ImageView image) {
        pieces = splitImage(image, DIMENSION);
    }

    // Metodo para fragmentar y escalar las imagenes
    private ArrayList<Bitmap> splitImage(ImageView image, int n) {

        int rows,cols;

        int chunkHeight,chunkWidth;

        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(n);

        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        rows = cols = (int) Math.sqrt(n);
        chunkHeight = bitmap.getHeight() / rows;
        chunkWidth = bitmap.getWidth() / cols;

        int yCoord = 0;
        for(int x = 0; x < rows; x++) {
            int xCoord = 0;
            for(int y = 0; y < cols; y++) {
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        return chunkedImages;
    }
}

