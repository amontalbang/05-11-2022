package com.example.a05_11_2022;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Calendar;

import Utils.Utilidades;

public class trans3 extends Fragment {

    private static final String CHANNEL_ID = "PuzzleDroid";
    private EditText name;
    private int newTime;
    private int oldTime;
    private String scoreName;
    private int callbackId = 0;
    private static MediaPlayer victoria;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trans3, container, false);

        victoria = MediaPlayer.create(getContext(), R.raw.victoria);
        victoria.start();

        newTime = getArguments().getInt("score");
        ConexionSQLite sql = new ConexionSQLite(getContext());
        oldTime = sql.getRecord();
        if(newTime < oldTime || oldTime == 0){
            LayoutInflater inf = getLayoutInflater();
            View layout = inf.inflate(R.layout.toast1,null);
            Toast toast = new Toast(getContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }

        TextView puntuacion = (TextView) view.findViewById(R.id.puntuacion);
        puntuacion.setText(newTime +" puntos");

        Button button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                name = (EditText) getActivity().findViewById(R.id.campoNombre);
                scoreName = name.getText().toString();

                ConexionSQLite dbHelper = new ConexionSQLite(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("INSERT INTO  " + Utilidades.TABLA_PUNTUACIONES + " (" + Utilidades.CAMPO_ID + ", " + Utilidades.CAMPO_NOMBRE +
                        ", " + Utilidades.CAMPO_TIEMPO + ") VALUES (null, '" + scoreName + "', '" + newTime + "')");

                callbackId = callbackId++;
                checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

                ContentResolver cr = getContext().getContentResolver();
                ContentValues values = new ContentValues();

                Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, "Nueva Puntuación PuzzleDroid") // Simple title
                        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "Madrid/Spain")
                        .putExtra(CalendarContract.Events.DESCRIPTION, scoreName + " ha registrado la siguiente puntuación en PuzzleDroid: " + newTime + ". ¡Sigue así!") // Description
                        .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
                startActivity(insertCalendarIntent);
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                Log.d("Nuevo registro añadido","Uri:" + uri);
                Toast.makeText(getContext(),"Puntuacion guardada en calendar", Toast.LENGTH_SHORT).show();

                Navigation.findNavController(view).navigate(R.id.action_trans3_to_FirstFragment);
            }


        });
        return view;
    }

    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(getContext(), p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(getActivity(), permissionsId, callbackId);
    }
}