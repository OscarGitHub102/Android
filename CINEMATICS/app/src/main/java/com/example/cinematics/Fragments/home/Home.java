package com.example.cinematics.Fragments.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cinematics.R;

public class Home extends Fragment {

    ImageView twitter, instagram, facebook, youtube;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home, container, false);

        twitter = v.findViewById(R.id.twitter);
        twitter.setOnClickListener(view -> {
            String url = "https://x.com/ifpleonardo";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        instagram = v.findViewById(R.id.instagram);
        instagram.setOnClickListener(view -> {
            String url = "https://www.instagram.com/ifpleonardodavincimadrid/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        facebook = v.findViewById(R.id.facebook);
        facebook.setOnClickListener(view -> {
            String url = "https://www.facebook.com/people/IFP-Leonardo-da-Vinci-Madrid/100075817996576/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        youtube = v.findViewById(R.id.youtube);
        youtube.setOnClickListener(view -> {
            String url = "https://www.youtube.com/channel/UCUtjLNATtgjQoIi2K8zMILA";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}