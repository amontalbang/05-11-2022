package com.example.a05_11_2022;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class selection extends Fragment {

    private selection binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        Button button_play = (Button) view.findViewById(R.id.button_normal);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selection_to_puzzleFirstFragment2);
            }

        });

        return view;
    }
}
