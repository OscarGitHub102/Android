package com.example.cinematics.Fragments.food;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematics.Models.Drink;
import com.example.cinematics.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Drinks extends Fragment {
    private FirebaseFirestore db;
    private  static final int totalDrinks = 10;
    private ArrayList<Drink> drinks;
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View v = inflater.inflate(R.layout.food_drinks, container, false);

        recyclerView = v.findViewById(R.id.recyclerDrinks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        drinks = new ArrayList<>();
        charge();

        return v;

    }

    public void charge()
    {
        AdapterDrinks adapter = new AdapterDrinks(drinks);
        recyclerView.setAdapter(adapter);

        db.collection("Drink")
                .orderBy("IdDrink")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                    {
                        int id = Objects.requireNonNull(document.getLong("IdDrink")).intValue();
                        String name = document.getString("Name");
                        double price = document.getDouble("Price");
                        String imageUrl = document.getString("Image");

                        Drink drink = new Drink(id, imageUrl, name, price);

                       drinks.add(drink);

                        if (drinks.size() >= totalDrinks) {
                            adapter.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al cargar la bebida", Toasty.LENGTH_SHORT, true).show());
    }


}