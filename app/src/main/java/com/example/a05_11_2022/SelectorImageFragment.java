package com.example.a05_11_2022;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class SelectorImageFragment extends Fragment {

    private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selector_image, container, false);
        image = view.findViewById(R.id.imageView6);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_selectorImageFragment_to_puzzleFirstFragment);
            }
        });

        return view;
    }

    private void requestPermission() {
        Log.d("Request", "requestPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                pickPhotoFromGallery();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            pickPhotoFromGallery();
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    pickPhotoFromGallery();
                } else {
                    Toast.makeText(getContext(), "Necesitas dar permiso para acceder a tus imagenes", Toast.LENGTH_LONG).show();
                }
            }
    );

    /*private ActivityResultLauncher<Intent> startActivityGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.d("Resultado obtenido", result.toString());
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri data = (Uri) result.getData().getData();
                    imageStream = getContentResolver().openInputStream(selectedImage);
                    try {
                        InputStream imageStream = getContext().getContentResolver().openInputStream(data);
                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d("Resultado", "Llega a setear la imagen");
                    image.setImageURI(data);
                }
            }
    );*/

    private ActivityResultLauncher<Intent> startActivityGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        image.setImageURI(uri);
                        MainActivity.setImage(image);
                    }
                }
            }
    );

    private void pickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        Log.d("Entra", "pickPhotoFromGallery");
        startActivityGallery.launch(intent.createChooser(intent, "ELIGE LA IMAGEN"));
    }
}