package com.example.a05_11_2022;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Photo_selector extends Fragment {

    private ImageView image;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_photo_selector, container, false);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 20 + 1);

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://damers-android-2022.appspot.com/Fotos_PuzzleDoid/img" + randomNum + ".jpg");
        ImageView iv = view.findViewById(R.id.imageView4);
        GlideApp.with(this)
                .load(ref)
                .into(iv);
        MainActivity.setImage(iv);

        Button button_continue = (Button) view.findViewById(R.id.button8);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_photo_selector_to_puzzleFirstFragment);
            }

        });
        return view;
    }
}
