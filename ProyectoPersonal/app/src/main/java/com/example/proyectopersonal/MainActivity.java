package com.example.proyectopersonal;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private FirebaseFirestore db;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextTextPassword);

        register = findViewById(R.id.button);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(view ->checkData());

    }

    private void checkData()
    {

        String User = user.getText().toString();
        String Password = password.getText().toString();

        if(User.equals("") | Password.equals("") | (User.equals("") && Password.equals("")))
        {

            Toast.makeText(this, "Usuario y/o Contraseña no introducidos", Toast.LENGTH_SHORT).show();

        }else
        {

            checkUser(User);

        }

    }

    private void checkUser(String User)
    {

        db.collection("USUARIOS").whereEqualTo("usuario", User)
                .get()
                .addOnCompleteListener(task ->
                {

                    if(task.isSuccessful())
                    {

                        if(task.getResult().isEmpty())
                        {

                            registro();

                        }else
                        {

                            Toast.makeText(this, "Usuario ya registrado", Toast.LENGTH_SHORT).show();
                            user.getText().clear();
                            password.getText().clear();

                        }

                    }else
                    {

                        Log.w(TAG,"Error de búsqueda", task.getException());
                        Toast.makeText(this, "Error de búsqueda" +task.getException(), Toast.LENGTH_SHORT).show();

                    }

                });

    }

    private void registro()
    {

        Intent i = new Intent(this, Registro.class);

        i.putExtra("user", user.getText().toString());
        i.putExtra("contra", password.getText().toString());

        startActivity(i);
        finish();

    }

}