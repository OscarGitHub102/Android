package com.example.cinematics.Fragments.films;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematics.Models.Film;
import com.example.cinematics.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    ArrayList<Film> films_adapter;
    private final RecyclerViewInterface recyclerViewInterface;

    public Adapter(ArrayList<Film> films_adapter, RecyclerViewInterface recyclerViewInterface) {
        this.films_adapter = films_adapter;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_recyclerview, parent, false);
        return new ViewHolder(v, recyclerViewInterface, films_adapter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(films_adapter.get(position).getURL()).into(holder.imageView);
        holder.title.setText(films_adapter.get(position).getTitle());
        holder.year.setText(String.valueOf(films_adapter.get(position).getYear()));
        holder.flecha.setImageResource(R.drawable.arrow_right);
    }

    @Override
    public int getItemCount() {
        return films_adapter.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, year;
        ImageView imageView, flecha;
        ArrayList<Film> films;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, ArrayList<Film> films) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_film);
            title = itemView.findViewById(R.id.title_film);
            year = itemView.findViewById(R.id.year_film);
            flecha = itemView.findViewById(R.id.arrow);

            this.films = films;

            itemView.setOnClickListener(view -> {

                if(recyclerViewInterface != null){
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.OnItemClick(pos, films);
                    }
                }
            });

        }
    }
}