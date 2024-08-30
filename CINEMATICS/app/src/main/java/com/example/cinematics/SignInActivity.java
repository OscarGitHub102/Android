package com.example.cinematics;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.cinematics.Models.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SignInActivity extends Activity {

    private EditText email, username, pass;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private boolean Google;
    private ArrayList<Reservation> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText2);
        username = findViewById(R.id.editText);
        pass = findViewById(R.id.editText3);

        Button register = findViewById(R.id.button_Registro_Login);

        register.setOnClickListener(v -> {
            String UserName = username.getText().toString().trim();
            String UserEmail = email.getText().toString().trim();
            String UserPassword = pass.getText().toString().trim();

            if (UserName.isEmpty() || UserEmail.isEmpty() || UserPassword.isEmpty()) {
                Toasty.error(SignInActivity.this, "Algún dato está vacío. Rellene todos los campos", Toasty.LENGTH_SHORT, true).show();
            }else if (!checkEmail(UserEmail)) {
                Toasty.error(SignInActivity.this, "El correo no contiene arroba", Toasty.LENGTH_LONG, true).show();
            } else if (!checkTextEmail(UserEmail)){
                Toasty.error(SignInActivity.this, "El correo no tiene texto antes del arroba", Toasty.LENGTH_LONG, true).show();
            } else if (!checkExtension(UserEmail)) {
                Toasty.error(SignInActivity.this, "El correo no contiene la extensión necesaria", Toasty.LENGTH_LONG, true).show();
            } else if (UserPassword.length() < 6) {
                Toasty.error(SignInActivity.this, "La contraseña no tiene la longitud mínima necesaria (6 caracteres)", Toasty.LENGTH_LONG, true).show();
            } else {
                RegisterUser(UserName, UserEmail, UserPassword);
            }
        });
    }

    public boolean checkEmail(String mail) {
        return mail != null && mail.contains("@");
    }
    public boolean checkTextEmail(String mail) {
        int index = mail.indexOf("@");
        return index > 0;
    }
    public boolean checkExtension(String mail)
    {
        String[] extensionesValidas = {".com", ".org", ".es"};

        for (String extensionesValida : extensionesValidas) {
            if (mail.endsWith(extensionesValida)) {
                return true;
            }
        }
        return false;
    }
    public void RegisterUser(String UserName, String UserEmail, String UserPass) {
        mAuth.createUserWithEmailAndPassword(UserEmail, UserPass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                        if (emailTask.isSuccessful()) {
                            reservationList = new ArrayList<>();
                            Google = false;

                            Map<String, Object> map = new HashMap<>();
                            map.put("Email", UserEmail);
                            map.put("UserName", UserName);
                            map.put("Password", UserPass);
                            map.put("Google", Google);
                            map.put("Reservations", reservationList);

                            db.collection("User").document(UserEmail).set(map).addOnSuccessListener(unused -> {
                                Toasty.success(SignInActivity.this, "Registro exitoso. Por favor, verifica tu correo electrónico.", Toasty.LENGTH_LONG, true).show();
                                startActivity(new Intent(SignInActivity.this, LogInActivity.class));
                                finish();
                            }).addOnFailureListener(e -> Toasty.error(SignInActivity.this, "Error al introducir en la base de datos", Toasty.LENGTH_SHORT).show());
                        } else {
                            Toasty.error(SignInActivity.this, "Error al enviar el correo de verificación", Toasty.LENGTH_SHORT, true).show();
                        }
                    });
                }
            } else {
                Toasty.error(SignInActivity.this, "Error al crear usuario", Toasty.LENGTH_SHORT, true).show();
                Log.e(TAG, "Error al crear usuario:", task.getException());
            }
        }).addOnFailureListener(e -> Toasty.error(SignInActivity.this, "Error al crear", Toasty.LENGTH_SHORT, true).show());
    }


}
