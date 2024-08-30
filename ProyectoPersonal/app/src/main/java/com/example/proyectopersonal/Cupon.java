package com.example.proyectopersonal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Cupon extends AppCompatActivity {

    private TextView texto1;

    private TextView texto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupon);

        Bundle extras = getIntent().getExtras();

        String pelicula = extras.getString("pelicula");
        int num1 = extras.getInt("asiento1");
        int num2 = extras.getInt("asiento2");

        texto1 = findViewById(R.id.textView12);
        texto2 = findViewById(R.id.textView14);

        texto1.setText(pelicula);
        texto2.setText(num1+ " y " +num2);

    }

    public void principal(View view)
    {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }
}