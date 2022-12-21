package com.example.a05_11_2022;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import Utils.Utilidades;

public class ConexionSQLite extends SQLiteOpenHelper {

    public ConexionSQLite(@Nullable Context context) {
        super(context, Utilidades.TABLA_PUNTUACIONES, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_PUNTUACIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Utilidades.TABLA_PUNTUACIONES);
        onCreate(db);
    }

    public List<PuntModelo> mostrarPunt(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Puntuaciones ORDER BY Tiempo ASC", null);
        List<PuntModelo> puntuaciones = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                puntuaciones.add(new PuntModelo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return puntuaciones;
    }

    public int getRecord(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(Tiempo) FROM Puntuaciones", null);
        cursor.moveToFirst();
        List<Integer> puntuaciones = new ArrayList<Integer>();
        puntuaciones.add(cursor.getInt(0));
        int record = puntuaciones.get(0);
        cursor.close();
        return record;
    }
}
