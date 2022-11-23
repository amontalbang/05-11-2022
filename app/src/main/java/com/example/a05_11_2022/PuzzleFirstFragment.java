package com.example.a05_11_2022;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private ImageView image;

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

        initInternalStorage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_puzzle_first, container, false);

        vista = view;
        // ImageView image = new ImageView(getContext());
        mGestos = (DetectorGestos) view.findViewById(R.id.grid);

        setPieces();
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

                /*switch (COLUMNAS){
                    case 3:
                        mostrar(getContext());
                        break;
                    case 4:
                        mostrar1(getContext());
                        break;
                    case 5:
                        mostrar2(getContext());
                        break;
                }*/
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

    private void movimiento(Context context, int posicion, int movimiento) {

        String newPosicion = titulo[posicion + movimiento];
        titulo[posicion + movimiento] = titulo[posicion];
        titulo[posicion] = newPosicion;

        /*switch (COLUMNAS){
            case 3:
                mostrar(context);
                break;
            case 4:
                mostrar1(context);
                break;
            case 5:
                mostrar2(context);
                break;
        }*/

        buildPuzzle(context);

        if (resuelto()) {
            timer.Detener();
            String tiempo = Integer.toString(timer.getSegundos());
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

    private void setPieces() {
        /*switch (COLUMNAS) {
            case 3:
                image.setImageResource(R.drawable.leon);
                break;
            case 4:
                image.setImageResource(R.drawable.perro);
                break;
            case 5:
                image.setImageResource(R.drawable.paleta);
                break;
        }*/
        pieces = splitImage(image, DIMENSION);
    }

    private ArrayList<Bitmap> splitImage(ImageView image, int n) {

        //For the number of rows and columns of the grid to be displayed
        int rows,cols;

        //For height and width of the small image chunks
        int chunkHeight,chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(n);

        //Getting the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        rows = cols = (int) Math.sqrt(n);
        chunkHeight = bitmap.getHeight() / rows;
        chunkWidth = bitmap.getWidth() / cols;

        //xCoord and yCoord are the pixel positions of the image chunks
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

        /* Now the chunkedImages has all the small image chunks in the form of Bitmap class.
         * You can do what ever you want with this chunkedImages as per your requirement.
         * I pass it to a new Activity to show all small chunks in a grid for demo.
         * You can get the source code of this activity from my Google Drive Account.
         */
    }

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        image.setImageURI(uri);
                    }
                }
            }

    );

    private void initInternalStorage() {
        Intent intent = new Intent();
        activityLauncher.launch(intent);
    }
}

