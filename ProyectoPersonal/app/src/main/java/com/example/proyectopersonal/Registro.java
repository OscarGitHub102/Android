package com.example.proyectopersonal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private ImageView titanic;
    private ImageView grease;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Bundle extras = getIntent().getExtras();
        String usuario = extras.getString("user");
        String contrasena = extras.getString("contra");

        titanic = findViewById(R.id.img_titanic);
        grease = findViewById(R.id.img_grease);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        titanic.setOnClickListener(view -> createNewUser(usuario, contrasena, "Titanic"));
        grease.setOnClickListener(view -> createNewUser(usuario, contrasena, "Grease"));

    }

    private void createNewUser(String usuario, String contra, String pelicula)
    {

        Map<String, Object> newUser = new HashMap<>();

        int aleatorio = (int) (Math.random() * 99) + 1;
        int aleatorio2 = aleatorio + 1;

        newUser.put("usuario", usuario);
        newUser.put("contrasena", contra);
        newUser.put("pelicula", pelicula);
        newUser.put("butaca1", aleatorio);
        newUser.put("butaca2", aleatorio2);

        db.collection("USUARIOS")
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {

                        Log.d(TAG, "DocumentSnapshot added with ID: " +documentReference.getId());
                        Toast.makeText(Registro.this, "¡Usuario registrado correctamente!", Toast.LENGTH_SHORT).show();
                        cupon(pelicula, aleatorio, aleatorio2);

                    }

                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        Log.d(TAG, "DocumentSnapshot couldn't be added");

                    }
                });

    }

    public void cupon(String peli, int numero1, int numero2)
    {

        Intent i = new Intent(this, Cupon.class);

        i.putExtra("pelicula", peli);
        i.putExtra("asiento1", numero1);
        i.putExtra("asiento2", numero2);

        startActivity(i);
        finish();

    }
}