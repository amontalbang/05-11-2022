package com.example.a05_11_2022;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.a05_11_2022.databinding.ActivityMainBinding;
import com.example.a05_11_2022.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private int callbackId = 0;
    private static MediaPlayer mediaPlayer;
    private static ImageView currentImage;
    private static Uri currentSong;
    private Locale locale;
    private Configuration config = new Configuration();
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    // private Music music;
    private static StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Mensaje", "onCreate");
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        ConexionSQLite dbHelper = new ConexionSQLite(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.loop);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        // music.start();
        // music = new Music();
        // music.run(this, null, R.raw.loop);

        findViewById(R.id.google_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Ayuda) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.webViewFragment);
        }

        if (id == R.id.Música) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.musica);
        }

        if (id == R.id.Idioma) {
                    showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static void start(){
        mediaPlayer.start();
    }

    public static void pause(){
        mediaPlayer.pause();
    }

    public static void stop(){
        mediaPlayer.stop();
    }

    public static void setReference(StorageReference ref) {
        reference = ref;
    }

    public static StorageReference getReference() {
        return reference;
    }

    public static void setImage(ImageView image) {
        currentImage = image;
    }

    public static ImageView getImage() {
        return currentImage;
    }

    public static void setSong(Uri song) {
        currentSong = song;
    }

    public static Uri getSong() {
        return currentSong;
    }

    private void showDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.str_button));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                    case 2:
                        locale = new Locale("it");
                        config.locale =locale;
                        break;
                    case 3:
                        locale = new Locale("fr");
                        config.locale =locale;
                        break;
                    case 4:
                        locale = new Locale("de");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();
            }
        });
        b.show();
    }



    // [END on_start_check_user]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken){

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user´s information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                        try{
                            //Google Sign in was succesfull, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e){
                            // Google Sign In failed, update UI appropiately
                            Log.w(TAG, "Google sign in failed", e);
                        }
                    }
                }
            }
    );
    // [END signin]

    private void updateUI(FirebaseUser user){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            /*Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
            finish();*/

            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.login);
        }
    }

    @Override
    public void onStart(){
        Log.d("Mensaje", "onStart");
        super.onStart();
        if (mediaPlayer == null) {
            Log.d("Mensaje", "entra null onStart");
            mediaPlayer = MediaPlayer.create(this, R.raw.loop);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        //Check if the user is signed in (non-null) and update UI accordingly
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    @Override
    public void onPause() {
        Log.d("Mensaje", "onPause");
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    @Override
    public void onResume() {
        Log.d("Mensaje", "onResume");
        super.onResume();
        if (mediaPlayer == null) {
            Log.d("Mensaje", "entra null onResume");
            mediaPlayer = MediaPlayer.create(this, R.raw.loop);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            Log.d("Mensaje", "entra start onResume");
            mediaPlayer.start();
        }
    }


    @Override
    public void onDestroy() {
        Log.d("Mensaje", "onDestroy");
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void onStop() {
        Log.d("Mensaje", "onStop");
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    /*static class Music extends Thread {
        private static MediaPlayer media;

        public void run(Context context, Uri uri, int i) {
            if (media != null) {
                media.stop();
                media.reset();
            }
            if (uri != null) {
                media = MediaPlayer.create(context, uri);
            } else {
                media = MediaPlayer.create(context, i);
            }

            media.start();
            this.start();
        }

        public void pause(){
            media.pause();
        }

        /*public void parar(){
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
    }*/

    /*public void refreshSong(Uri song) {

        if (music != null && music.isAlive()) {
            music.run(this, song, 0);
        }

        Music music = new Music();
        MediaPlayer mediaPlayer = MediaPlayer.create(this, song);
        music.start();
        music.run();
        // currentSong = song;
        // mediaPlayer.stop();
        // mediaPlayer.release();
        // mediaPlayer.reset();
        // mediaPlayer = MediaPlayer.create(this, song);
    }*/
}