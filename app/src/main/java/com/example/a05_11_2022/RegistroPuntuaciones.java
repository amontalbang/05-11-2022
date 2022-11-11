package com.example.a05_11_2022;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Time;
import Utils.Utilidades;

public class RegistroPuntuaciones extends AppCompatActivity {

    /* int id = 0;
    String campoId;
    EditText campoNombre;
    Time campoTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_trans3);

        id = id++;
        campoId = Integer.toString(id);
        campoNombre = (EditText) findViewById(R.id.campoNombre);
        campoTiempo = ;
    }

    public void onClick(){
        registrarPuntuaciones();
    }

    private void registrarPuntuaciones() {

        ConexionSQLite conn = new ConexionSQLite((View.OnClickListener) this, "bd_puntuaciones", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(campoId);
        values.put(Utilidades.CAMPO_NOMBRE, campoNombre.getText().toString());
        values.put(campoTiempo.getTime().toString());

        Long idResultante = db.insert(Utilidades.TABLA_PUNTUACIONES, Utilidades.CAMPO_ID, values);
    }*/
}