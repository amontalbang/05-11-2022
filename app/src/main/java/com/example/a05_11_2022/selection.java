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

public class selection extends Fragment {

    /*private selection binding;
    private ImageView image;
    private String type;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_selection, container, false);
        image = new ImageView(getContext());

        Button button_play = (Button) view.findViewById(R.id.button_play);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MainActivity.setImage(null, null);
                Navigation.findNavController(view).navigate(R.id.action_selection_to_puzzleFirstFragment2);
            }

        });

        Button button_gallery = view.findViewById(R.id.button_scores);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "gallery";
                requestPermission();
            }
        });

        Button button_camera = view.findViewById(R.id.button_exit);
        button_camera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "camera";
                requestPermission();
            }
        });

        Button button_start = view.findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_selection_to_puzzleFirstFragment2);
            }
        });

        return view;
    }

    private void requestPermission() {
        Log.d("Request", "requestPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (type == "camera") {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                }
            } else {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickPhotoFromGallery();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        } else {
            if (type == "camera") {
                takePhotoFromCamera();
            } else {
                pickPhotoFromGallery();
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (type == "camera") {
                        takePhotoFromCamera();
                    } else {
                        pickPhotoFromGallery();
                    }
                } else {
                    Toast.makeText(getContext(), "Necesitas dar permiso para acceder a tus imagenes", Toast.LENGTH_LONG).show();
                }
            }
    );

    private ActivityResultLauncher<Intent> startActivityGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        image.setImageURI(uri);
                        // MainActivity.setImage(image, null);
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> startActivityCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        image.setImageBitmap(bitmap);
                        // MainActivity.setImage(image, null);
                    }
                }
            }
    );

    private void pickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityGallery.launch(intent.createChooser(intent, "ELIGE LA IMAGEN"));
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityCamera.launch(intent);
    }*/
}
