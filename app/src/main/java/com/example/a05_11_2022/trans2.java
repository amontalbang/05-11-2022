package com.example.a05_11_2022;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class trans2 extends Fragment {

    private static MediaPlayer victoria;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_trans2, container, false);
        ImageView image = view.findViewById(R.id.trans2_resolved);
        image.setImageDrawable(MainActivity.getImage().getDrawable());

        victoria = MediaPlayer.create(getContext(), R.raw.victoria);
        victoria.start();

        Button button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_trans2_to_selectorImageFragment);
            }

        });

        Button button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_trans1_to_FirstFragment);
            }

        });
        return view;
    }
}