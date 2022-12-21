package com.example.a05_11_2022;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.concurrent.ThreadLocalRandom;

public class Photo_selector extends Fragment {

    private int num;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        num = num++;
        View view = inflater.inflate(R.layout.fragment_photo_selector, container, false);
        int randomNum = ThreadLocalRandom.current().nextInt(num, 20 + 1);

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://damers-android-2022.appspot.com/Fotos_PuzzleDoid/img" + randomNum + ".jpg");
        ImageView iv = view.findViewById(R.id.imageView4);
        GlideApp.with(this)
                .load(ref)
                .into(iv);
        MainActivity.setReference(ref);
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
