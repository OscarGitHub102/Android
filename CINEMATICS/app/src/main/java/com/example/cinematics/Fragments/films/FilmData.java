package com.example.cinematics.Fragments.films;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinematics.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

public class FilmData extends Fragment {

    String imageUrl, nombrePelicula, fechaEstreno, directorPeli, sinopsis;
    String[] rep, gen, rev;
    double val;
    Bitmap picture2; // Variable de clase para guardar la imagen cargada desde la URL
    ImageView portada;
    TextView titulo, estreno, dir, repartoPelicula, gen1, gen2, sin, value, res;
    View v;

    public static FilmData newInstance(String imageUrl, String titulo, String year, String director, String[] generos, String sinopsis, String[] reparto, double valoracion, String[] resenas) {
        FilmData fragment = new FilmData();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putString("title", titulo);
        args.putString("year", year);
        args.putString("director", director);
        args.putStringArray("generos", generos);
        args.putString("sinopsis", sinopsis);
        args.putStringArray("reparto", reparto);
        args.putDouble("valoracion", valoracion);
        args.putStringArray("resenas", resenas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imageUrl = getArguments().getString("url");
            nombrePelicula = getArguments().getString("title");
            fechaEstreno = getArguments().getString("year");
            directorPeli = getArguments().getString("director");
            gen = getArguments().getStringArray("generos");
            sinopsis = getArguments().getString("sinopsis");
            rep = getArguments().getStringArray("reparto");
            val = getArguments().getDouble("valoracion");
            rev = getArguments().getStringArray("resenas");

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.film, container, false);

        portada = v.findViewById(R.id.image_film_data);
        titulo = v.findViewById(R.id.title_film_data);
        estreno = v.findViewById(R.id.year_film_data);
        dir = v.findViewById(R.id.director_film_data);
        gen1 = v.findViewById(R.id.genre1_film_data);
        gen2 = v.findViewById(R.id.genre2_film_data);
        sin = v.findViewById(R.id.synopsis_film_data);
        repartoPelicula = v.findViewById(R.id.casting_film_data);
        value = v.findViewById(R.id.rating);
        res = v.findViewById(R.id.reviews_film_data);

        loadBitmapFromUrl(imageUrl);

        return v;
    }

    private void loadBitmapFromUrl(String imageUrl) {
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                portada.setImageBitmap(bitmap);
                picture2 = bitmap; // Guardar el bitmap cargado en la variable picture2
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle error
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Optional: handle placeholder image while loading
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        portada.setImageBitmap(picture2);
        String text = "Año de estreno: " + fechaEstreno;
        String text2 = "Valoración media: " + val;
        titulo.setText(nombrePelicula);
        estreno.setText(text);
        dir.setText(directorPeli);
        gen1.setText(gen[0]);
        gen2.setText(gen[1]);
        sin.setText(sinopsis);
        value.setText(text2);

        StringBuilder result = new StringBuilder();

        for (String s : rep) {
            result.append(s);
            result.append("\n");
        }

        repartoPelicula.setText(result.toString());

        StringBuilder result2 = new StringBuilder();

        for (String s : rev) {
            result2.append(s);
            result2.append("\n");
        }

        res.setText(result2.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
