package com.example.cinematics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematics.Fragments.films.FilmData;
import com.example.cinematics.Fragments.films.RecyclerViewInterface;
import com.example.cinematics.Models.Film;
import com.example.cinematics.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        View headerView = navigationView.getHeaderView(0);
        ImageView image = headerView.findViewById(R.id.image);
        TextView username = headerView.findViewById(R.id.username);
        TextView email = headerView.findViewById(R.id.correo);

        chargePhoto(image);
        chargeUsername(username);
        chargeMail(email);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_films, R.id.nav_food, R.id.nav_reservation, R.id.nav_discounts, R.id.nav_location)
                .setOpenableLayout(drawer)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;

        navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_disconnect) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("DESCONECTAR");
                dialog.setMessage("¿Segur@ que desea cerrar sesión?\nLa aplicación se cerrará");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Confirmar", (dialog1, id1) -> {
                    mAuth.signOut();
                    Toasty.info(MainActivity.this, "Desconectad@", Toasty.LENGTH_SHORT, true).show();
                    drawer.closeDrawers();
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));

                });
                dialog.setNegativeButton("Cancelar", (dialog12, id12) -> dialog12.dismiss());
                dialog.show();
                return true;

            } else if (id == R.id.nav_share) {

                shareApp();
                drawer.closeDrawers();
                return true;
            }

            boolean cent = NavigationUI.onNavDestinationSelected(item, navController);
            if (cent) {
                drawer.closeDrawers();
            }
            return cent;
        });

    }

    public void chargeMail(TextView mail) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mail.setText(currentUser.getEmail());
        } else {
            Toasty.error(MainActivity.this, "Ha ocurrido un error. Inténtelo de nuevo más tarde", Toasty.LENGTH_LONG, true).show();
            finish();
        }
    }

    public void chargeUsername(TextView name) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("User").whereEqualTo("Email", currentUser.getEmail()).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String username = documentSnapshot.getString("UserName");
                    if (username == null) {
                        name.setVisibility(View.GONE);
                    }
                    name.setText(username);
                }
            }).addOnFailureListener(e -> {
                Toasty.error(MainActivity.this, "Ha ocurrido un error, pruebe mas tarde", Toasty.LENGTH_LONG, true).show();
                finish();
            });
        } else {
            Toasty.error(MainActivity.this, "No se encuentra usuari@", Toasty.LENGTH_LONG, true).show();
            finish();
        }
    }

    public void chargePhoto(ImageView photo) {
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().getPhotoUrl() != null) {
                String userPhoto = mAuth.getCurrentUser().getPhotoUrl().toString() + "?timestamp=" + System.currentTimeMillis();

                Picasso.get()
                        .load(userPhoto)
                        .resize(70, 70)
                        .centerCrop()
                        .into(photo);
            } else {
                photo.setImageResource(R.mipmap.logo);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void OnItemClick(int position, ArrayList<Film> films) {
        Film film = films.get(position);
        String posterUrl = film.getURL();

        Picasso.get().load(posterUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                Bundle arguments = new Bundle();
                arguments.putString("url", film.getURL());
                arguments.putString("title", film.getTitle());
                arguments.putString("year", String.valueOf(film.getYear()));
                arguments.putString("director", film.getDirector());

                List<String> generosList = film.getGeneros();
                String[] generosArray = generosList.toArray(new String[0]);
                arguments.putStringArray("generos", generosArray);

                arguments.putString("sinopsis", film.getSinopsis());

                List<String> repartoList = film.getReparto();
                String[] repartoArray = repartoList.toArray(new String[0]);
                arguments.putStringArray("reparto", repartoArray);
                arguments.putDouble("valoracion", film.getRating());

                List<String> resenasList = film.getReviews();
                String[] resenasArray = resenasList.toArray(new String[0]);
                arguments.putStringArray("resenas", resenasArray);

                FilmData filmDataFragment = new FilmData();
                filmDataFragment.setArguments(arguments);

                navController.navigate(R.id.action_nav_films_to_filmData, arguments);
                toolbar.setTitle(film.getTitle());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle error
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Cinematics");
        String shareMessage = "Descarga la aplicación de CINEMATICS en: https://drive.google.com/file/d/15QL4YRKrFsFCbgE6UyKhBnUbllfLeLXc/view?usp=sharing";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Compartir vía"));
    }

}