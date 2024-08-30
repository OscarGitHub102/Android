package com.example.cinematics.Fragments.food;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematics.Models.Dessert;
import com.example.cinematics.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Desserts extends Fragment {
    private FirebaseFirestore db;
    private  static final int totalDesserts = 10;
    private ArrayList<Dessert> desserts;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View v = inflater.inflate(R.layout.food_desserts, container, false);

        recyclerView = v.findViewById(R.id.recyclerDesserts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        desserts = new ArrayList<>();
        charge();

        return v;

    }

    public void charge()
    {
        AdapterDesserts adapter = new AdapterDesserts(desserts);
        recyclerView.setAdapter(adapter);


        db.collection("Dessert")
                .orderBy("IdDessert")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                    {
                        int id = Objects.requireNonNull(document.getLong("IdDessert")).intValue();
                        String name = document.getString("Name");
                        double price = document.getDouble("Price");
                        String imageUrl = document.getString("Image");

                        Dessert dessert = new Dessert(id, imageUrl, name, price);

                        desserts.add(dessert);


                        if (desserts.size() >= totalDesserts) {
                            adapter.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al cargar el postre", Toasty.LENGTH_SHORT, true).show());

    }
}