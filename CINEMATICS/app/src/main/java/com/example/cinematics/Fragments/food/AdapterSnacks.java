package com.example.cinematics.Fragments.food;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematics.Models.Snack;
import com.example.cinematics.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSnacks extends RecyclerView.Adapter<AdapterSnacks.ViewHolder> {

    private final ArrayList<Snack> snacks_adapter;

    public AdapterSnacks(ArrayList<Snack> snacks_adapter) {
        this.snacks_adapter = snacks_adapter;
    }

    @NonNull
    @Override
    public AdapterSnacks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override

    public void onBindViewHolder(@NonNull AdapterSnacks.ViewHolder holder, int position) {
        String price = snacks_adapter.get(position).getPrice() + "€";

        Picasso.get().load(snacks_adapter.get(position).getURL()).into(holder.imageView);

        holder.textView.setText(snacks_adapter.get(position).getName());
        holder.textView2.setText(price);
    }


    @Override
    public int getItemCount() {
        return snacks_adapter.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_food);
            textView = itemView.findViewById(R.id.name_food);
            textView2 = itemView.findViewById(R.id.price_food);

        }
    }
}
