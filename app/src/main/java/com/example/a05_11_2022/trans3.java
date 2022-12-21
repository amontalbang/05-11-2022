package com.example.a05_11_2022;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.ImageView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Utils.Utilidades;

public class trans3 extends Fragment {

    private static final String CHANNEL_ID = "PuzzleDroid_Record";
    private EditText name;
    private int newTime;
    private int oldTime;
    private String scoreName;
    private int callbackId = 0;
    private static MediaPlayer victoria;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trans3, container, false);
        ImageView image = view.findViewById(R.id.trans3_resolved);
        StorageReference reference = MainActivity.getReference();
        GlideApp.with(view).load(reference).into(image);

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
            showPushNotification();
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

                /** Registrar puntuacion en DataBase*/
                mAuth = FirebaseAuth.getInstance();
                mFirestore = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userName = scoreName;
                String email = user.getEmail();
                int punt = newTime;
                Log.d("db userName", userName);
                Log.d("db email", email);
                Log.d("db punt", Integer.toString(newTime));
                registroPuntCloud(userName, email, punt);

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

    private void showPushNotification() {
        createNotificationChannel();

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("NUEVO RÉCORD en PuzzleDroid")
                .setContentText("Se ha producido un nuevo Récord en el juego, ¡Corre, ve a superarlo!")
                .setColor(Color.RED)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(11, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void registroPuntCloud(String userName, String  email, int punt){
        Map <String, Object> map = new HashMap<>();
        map.put("nombre", userName);
        map.put("usuario", email);
        map.put("puntuacion", punt);

        mFirestore.collection("Puntuaciones").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Puntuacion guardada en base de datos", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "NO se guardó la puntuación en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
        }
}