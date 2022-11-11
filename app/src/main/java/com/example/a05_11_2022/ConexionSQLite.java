package com.example.a05_11_2022;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

import Utils.Utilidades;

public class ConexionSQLite extends SQLiteOpenHelper {

    public ConexionSQLite(@Nullable View.OnClickListener context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super((Context) context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_PUNTUACIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
