package com.example.a05_11_2022;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends Fragment {

    CircleImageView profileImage;
    TextView text;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        text = view.findViewById(R.id.textView);
        profileImage = view.findViewById(R.id.profile_image);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Glide
                    .with(this)
                    .load(currentUser.getPhotoUrl().toString())
                    .placeholder(R.drawable.ic_player_foreground)
                    .into(profileImage);
            TextView name = (TextView) view.findViewById(R.id.textView);
            name.setText(currentUser.getDisplayName());
            Log.d("message", currentUser.getDisplayName());
        }

        Button button6 = (Button) view.findViewById(R.id.signout_button);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }

        });

        Button button4 = (Button) view.findViewById(R.id.back_button);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_FirstFragment);
            }

        });
        return view;
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        MainActivity.stop();
        startActivity(intent);
        //finish();
    }

    public void show(FragmentManager supportFragmentManager, String navegar_a_fragment) {
    }
}