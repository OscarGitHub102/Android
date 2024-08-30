package com.example.cinematics.Fragments.films;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematics.Models.Film;
import com.example.cinematics.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Films extends Fragment {

    FirebaseFirestore db;
    private ArrayList<Film> films;
    private RecyclerViewInterface recyclerViewInterface;
    private final static int total_Film = 10;

    @SuppressLint("WrongThread")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        View v = inflater.inflate(R.layout.films, container, false);

        Context context = v.getContext();
        RecyclerView recyclerView = v.findViewById(R.id.recyclerFilms);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        films = new ArrayList<>();
        Adapter adapter = new Adapter(films, recyclerViewInterface);

        charge(adapter);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        if (context instanceof RecyclerViewInterface)
        {
            recyclerViewInterface = (RecyclerViewInterface) context;
        }
        else
        {
            throw new RuntimeException(context
                    + " must implement OnHabitInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void charge(Adapter adapter)
    {
        db.collection("Film")
                .orderBy("IdFilm")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                    {
                        String title = document.getString("Title");
                        int year = Objects.requireNonNull(document.getLong("Year")).intValue();
                        String director = document.getString("Director");
                        String synopsis = document.getString("Synopsis");
                        int id = Objects.requireNonNull(document.getLong("IdFilm")).intValue();
                        List<String> genresList = (List<String>) document.get("Genres");
                        List<String> castingList = (List<String>) document.get("Casting");
                        double rating = document.getDouble("Rating");
                        List<String> reviewsList = (List<String>) document.get("Reviews");

                        String posterUrl = document.getString("Poster");

                        Film film = new Film(id, posterUrl, title, year, director, genresList, synopsis, castingList, rating, reviewsList);

                        films.add(film);
                        if (films.size() == total_Film)
                        {
                            adapter.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al cargar la película", Toasty.LENGTH_SHORT, true).show());
    }

}
