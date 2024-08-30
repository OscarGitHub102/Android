package com.example.cinematics.Fragments.location;

import android.annotation.SuppressLint;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Location extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    GoogleMap mMap;
    ImageView bus155, bus34, metro5, cercanias5;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.location, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        bus155 = v.findViewById(R.id.bus155);
        bus155.setOnClickListener(v1 -> {
            String url = "https://www.emtmadrid.es/Bloques-EMT/EMT-BUS/Mi-linea-(1).aspx?linea=155&lang=es-ES";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        bus34 = v.findViewById(R.id.bus34);
        bus34.setOnClickListener(v12 -> {
            String url = "https://www.emtmadrid.es/Bloques-EMT/EMT-BUS/Mi-linea-(1).aspx?linea=34&lang=es-ES";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        metro5 = v.findViewById(R.id.metro5);
        metro5.setOnClickListener(v13 -> {
            String url = "https://www.metromadrid.es/es/linea/linea-5";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        cercanias5 = v.findViewById(R.id.cercanias5);
        cercanias5.setOnClickListener(v14 -> {
            String url = "https://www.redtransporte.com/madrid/cercanias-renfe/linea-c-5.html";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        return v;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng cinema = new LatLng(40.37844,-3.7629072);
        mMap.addMarker(new MarkerOptions().position(cinema).title("CINEMATICS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cinema));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
}