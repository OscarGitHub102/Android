package com.example.cinematics;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.cinematics.Models.Reservation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LogInActivity extends Activity {
    private EditText email, pass;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<Reservation> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mAuth = FirebaseAuth.getInstance();

        Button initial = findViewById(R.id.button_Iniciar_Login);
        Button register = findViewById(R.id.button_Registro_Login);
        Button googleSignInButton = findViewById(R.id.button_Google_Login);
        email = findViewById(R.id.editText_Email_Login);
        pass = findViewById(R.id.editText_Password_login);

        register.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, SignInActivity.class)));

        googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        initial.setOnClickListener(v -> {

            String UserEmail = email.getText().toString().trim();
            String UserPass = pass.getText().toString().trim();

            if (UserEmail.isEmpty() || UserPass.isEmpty()) {
                Toasty.error(LogInActivity.this, "Algún campo está vacío", Toasty.LENGTH_SHORT, true).show();
            } else {
                login(UserEmail, UserPass);
            }
        });
    }

    private void login(String UserEmail, String UserPass) {
        mAuth.signInWithEmailAndPassword(UserEmail, UserPass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Toasty.success(LogInActivity.this, "Hola de nuevo", Toasty.LENGTH_LONG, true).show();
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toasty.error(LogInActivity.this, "Por favor, verifica tu correo electrónico antes de iniciar sesión", Toasty.LENGTH_SHORT, true).show();
                }
            } else {
                Toasty.error(LogInActivity.this, "No se encuentran usuarios con estos campos", Toasty.LENGTH_SHORT, true).show();
            }
        }).addOnFailureListener(e -> Toasty.error(LogInActivity.this, "Fallo en el inicio", Toasty.LENGTH_SHORT, true).show());
    }

    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toasty.error(this, "Fallo en la autenticación con Google", Toasty.LENGTH_SHORT, true).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("User");
                        assert user != null;
                        usersCollection.document(Objects.requireNonNull(user.getEmail())).get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            Toasty.success(LogInActivity.this, "Hola de nuevo", Toasty.LENGTH_LONG, true).show();
                                            startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            boolean isGoogleUser = true;
                                            reservationList = new ArrayList<>();

                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("Email", user.getEmail());
                                            userData.put("UserName", user.getDisplayName());
                                            userData.put("Google", isGoogleUser);
                                            userData.put("Reservations", reservationList);

                                            usersCollection.document(user.getEmail()).set(userData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toasty.success(LogInActivity.this, "Bienvenid@ a Cinematics", Toasty.LENGTH_LONG, true).show();
                                                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> Toasty.error(LogInActivity.this, "Error al guardar datos en Firestore", Toasty.LENGTH_SHORT, true).show());
                                        }
                                    } else {
                                        Toasty.error(LogInActivity.this, "Error al verificar la existencia del usuario", Toasty.LENGTH_SHORT, true).show();
                                    }
                                });
                    } else {
                        Toasty.error(LogInActivity.this, "Fallo en la autenticación con Firebase", Toasty.LENGTH_SHORT, true).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(email != null)
            email.setText("");
        if(pass != null)
            pass.setText("");
    }
}