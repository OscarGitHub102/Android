package com.example.cinematics.Fragments.food;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematics.Models.Snack;
import com.example.cinematics.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Snacks extends Fragment {
    private FirebaseFirestore db;
    private  static final int totalSnacks = 10;
    private ArrayList<Snack> snacks;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View v = inflater.inflate(R.layout.food_snacks, container, false);

        recyclerView = v.findViewById(R.id.recyclerSnacks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        snacks = new ArrayList<>();
        charge();

        return v;

    }

    public void charge() {
        AdapterSnacks adapter = new AdapterSnacks(snacks);
        recyclerView.setAdapter(adapter);

        db.collection("Snack")
                .orderBy("IdSnack")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        int id = Objects.requireNonNull(document.getLong("IdSnack")).intValue();
                        String name = document.getString("Name");
                        double price = document.getDouble("Price");
                        String imageUrl = document.getString("Image");

                        Snack snack = new Snack(id, imageUrl, name, price);
                        snacks.add(snack);

                        if (snacks.size() >= totalSnacks) {
                            adapter.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al cargar los aperitivos", Toasty.LENGTH_SHORT, true).show());
    }
}