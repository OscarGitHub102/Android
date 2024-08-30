package com.example.cinematics.Fragments.reservation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.cinematics.Models.Dessert;
import com.example.cinematics.Models.Drink;
import com.example.cinematics.Models.Film;
import com.example.cinematics.Models.Reservation;
import com.example.cinematics.Models.Snack;
import com.example.cinematics.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import es.dmoral.toasty.Toasty;

public class Reservations extends Fragment {

    private AutoCompleteTextView autoCompleteTextViewFilms;
    private LinearLayout linearLayoutSnacks, linearLayoutDrinks, linearLayoutDesserts;
    private CheckBox snackCb1, snackCb2, snackCb3, snackCb4, snackCb5, snackCb6, snackCb7, snackCb8, snackCb9, snackCb10;
    private CheckBox drinkCb1, drinkCb2, drinkCb3, drinkCb4, drinkCb5, drinkCb6, drinkCb7, drinkCb8, drinkCb9, drinkCb10;
    private CheckBox dessertCb1, dessertCb2, dessertCb3, dessertCb4, dessertCb5, dessertCb6, dessertCb7, dessertCb8, dessertCb9, dessertCb10;

    private TextView dateText;
    private String formattedDateTime;
    private Date selectedDateTime;
    private Calendar calendar;
    private TextView price;
    private TextView priceDiscount;
    private EditText code;
    private Dialog dialog;
    private String selectedFilm = "";
    private String filmSummary = "";
    private int positionListSnacks = 0, positionListDrinks = 0, positionListDesserts = 0;
    private ArrayList<String> selectedSnacks;
    private ArrayList<String> selectedDrinks;
    private ArrayList<String> selectedDesserts;
    private static int showSnacksCheckbox = 0;
    private static int showDrinksCheckbox = 0;
    private static int showDessertsCheckbox = 0;
    private ArrayList <String> discounts;
    private String discount;
    private double initialPrice, totalPrice;

    private boolean isDiscount = false;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private final ArrayList<Snack> selectedSnacksDetails = new ArrayList<>();
    private final ArrayList<Drink> selectedDrinksDetails = new ArrayList<>();
    private final ArrayList<Dessert> selectedDessertsDetails = new ArrayList<>();
    private Film selectedFilmDetails;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.reservation, container, false);

        Button buttonReserve = v.findViewById(R.id.btnReservar);
        ImageView dateImage = v.findViewById(R.id.time);
        dateText = v.findViewById(R.id.textViewDate);
        calendar = Calendar.getInstance();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // FILMS

        String[] titles = new String[]{
                "Avatar",
                "Celda 211",
                "El pianista",
                "Gladiator",
                "Gran Torino",
                "Interestelar",
                "Intocable",
                "La Sociedad de la Nieve",
                "Los Pájaros",
                "Million Dollar Baby",
                "Oppenheimer",
                "Origen",
                "Parásitos",
                "Ready Player One",
                "Soy Leyenda"
        };

        dateImage.setOnClickListener(v1 -> pickDateTime());

        autoCompleteTextViewFilms = v.findViewById(R.id.autoCompleteFilm);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, titles);
        autoCompleteTextViewFilms.setAdapter(adapter);

        autoCompleteTextViewFilms.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedFilm = adapterView.getItemAtPosition(i).toString();
            filmSummary = "Película:\n\n" + selectedFilm + " - 7€\n\n-----------------------";
        });

        // SNACKS

        ArrayList<String> listSnacks = new ArrayList<>();
        listSnacks.add("Cubo de palomitas");
        listSnacks.add("Bolsa de patatas");
        listSnacks.add("Nachos");
        listSnacks.add("Bolsa de ganchitos");
        listSnacks.add("Cacahuetes salados");
        listSnacks.add("Bolsa de anacardos");
        listSnacks.add("Barrita de chocolate");
        listSnacks.add("Bolsa de chuches");
        listSnacks.add("Hamburguesa");
        listSnacks.add("Perrito caliente");


        ImageView snacksDeploymentArrow = v.findViewById(R.id.desplegableSnacks);
        snacksDeploymentArrow.setImageResource(R.drawable.arrow_down);
        RelativeLayout relativeLayoutSnacks = v.findViewById(R.id.eleccionSnacks);

        linearLayoutSnacks = v.findViewById(R.id.checkBoxSnacks);

        snackCb1 = v.findViewById(R.id.snackCb1);
        fillList(listSnacks, snackCb1, positionListSnacks++);
        snackCb2 = v.findViewById(R.id.snackCb2);
        fillList(listSnacks, snackCb2, positionListSnacks++);
        snackCb3 = v.findViewById(R.id.snackCb3);
        fillList(listSnacks, snackCb3, positionListSnacks++);
        snackCb4 = v.findViewById(R.id.snackCb4);
        fillList(listSnacks, snackCb4, positionListSnacks++);
        snackCb5 = v.findViewById(R.id.snackCb5);
        fillList(listSnacks, snackCb5, positionListSnacks++);
        snackCb6 = v.findViewById(R.id.snackCb6);
        fillList(listSnacks, snackCb6, positionListSnacks++);
        snackCb7 = v.findViewById(R.id.snackCb7);
        fillList(listSnacks, snackCb7, positionListSnacks++);
        snackCb8 = v.findViewById(R.id.snackCb8);
        fillList(listSnacks, snackCb8, positionListSnacks++);
        snackCb9 = v.findViewById(R.id.snackCb9);
        fillList(listSnacks, snackCb9, positionListSnacks++);
        snackCb10 = v.findViewById(R.id.snackCb10);
        fillList(listSnacks, snackCb10, positionListSnacks++);


        selectedSnacks = new ArrayList<>();
        relativeLayoutSnacks.setOnClickListener(view -> {

            if (showSnacksCheckbox % 2 != 0) {
                linearLayoutSnacks.setVisibility(View.GONE);
                showSnacksCheckbox++;
            } else {
                linearLayoutSnacks.setVisibility(View.VISIBLE);
                showSnacksCheckbox++;
            }

            snackCb1.setOnClickListener(view1 -> checkBoxSnack(snackCb1));
            snackCb2.setOnClickListener(view12 -> checkBoxSnack(snackCb2));
            snackCb3.setOnClickListener(view13 -> checkBoxSnack(snackCb3));
            snackCb4.setOnClickListener(view14 -> checkBoxSnack(snackCb4));
            snackCb5.setOnClickListener(view15 -> checkBoxSnack(snackCb5));
            snackCb6.setOnClickListener(view16 -> checkBoxSnack(snackCb6));
            snackCb7.setOnClickListener(view119 -> checkBoxSnack(snackCb7));
            snackCb8.setOnClickListener(view120 -> checkBoxSnack(snackCb8));
            snackCb9.setOnClickListener(view121 -> checkBoxSnack(snackCb9));
            snackCb10.setOnClickListener(view122 -> checkBoxSnack(snackCb10));

        });

        // DRINKS

        ArrayList<String> listDrinks = new ArrayList<>();
        listDrinks.add("Refresco de cola");
        listDrinks.add("Refresco de naranja");
        listDrinks.add("Refresco de limón");
        listDrinks.add("Botella de agua");
        listDrinks.add("Bebida gaseosa");
        listDrinks.add("Té de limón");
        listDrinks.add("Bebida energética");
        listDrinks.add("Café con leche");
        listDrinks.add("Cerveza");
        listDrinks.add("Champín");


        ImageView drinksDeploymentArrow = v.findViewById(R.id.desplegableDrinks);
        drinksDeploymentArrow.setImageResource(R.drawable.arrow_down);
        RelativeLayout relativeLayoutDrinks = v.findViewById(R.id.eleccionDrinks);

        linearLayoutDrinks = v.findViewById(R.id.checkBoxDrinks);

        drinkCb1 = v.findViewById(R.id.drinkCb1);
        fillList(listDrinks, drinkCb1, positionListDrinks++);
        drinkCb2 = v.findViewById(R.id.drinkCb2);
        fillList(listDrinks, drinkCb2, positionListDrinks++);
        drinkCb3 = v.findViewById(R.id.drinkCb3);
        fillList(listDrinks, drinkCb3, positionListDrinks++);
        drinkCb4 = v.findViewById(R.id.drinkCb4);
        fillList(listDrinks, drinkCb4, positionListDrinks++);
        drinkCb5 = v.findViewById(R.id.drinkCb5);
        fillList(listDrinks, drinkCb5, positionListDrinks++);
        drinkCb6 = v.findViewById(R.id.drinkCb6);
        fillList(listDrinks, drinkCb6, positionListDrinks++);
        drinkCb7 = v.findViewById(R.id.drinkCb7);
        fillList(listDrinks, drinkCb7, positionListDrinks++);
        drinkCb8 = v.findViewById(R.id.drinkCb8);
        fillList(listDrinks, drinkCb8, positionListDrinks++);
        drinkCb9 = v.findViewById(R.id.drinkCb9);
        fillList(listDrinks, drinkCb9, positionListDrinks++);
        drinkCb10 = v.findViewById(R.id.drinkCb10);
        fillList(listDrinks, drinkCb10, positionListDrinks++);


        selectedDrinks = new ArrayList<>();
        relativeLayoutDrinks.setOnClickListener(view -> {

            if (showDrinksCheckbox % 2 != 0) {
                linearLayoutDrinks.setVisibility(View.GONE);
                showDrinksCheckbox++;
            } else {
                linearLayoutDrinks.setVisibility(View.VISIBLE);
                showDrinksCheckbox++;
            }

            drinkCb1.setOnClickListener(view17 -> checkBoxDrink(drinkCb1));
            drinkCb2.setOnClickListener(view18 -> checkBoxDrink(drinkCb2));
            drinkCb3.setOnClickListener(view19 -> checkBoxDrink(drinkCb3));
            drinkCb4.setOnClickListener(view110 -> checkBoxDrink(drinkCb4));
            drinkCb5.setOnClickListener(view112 -> checkBoxDrink(drinkCb5));
            drinkCb6.setOnClickListener(view111 -> checkBoxDrink(drinkCb6));
            drinkCb7.setOnClickListener(view123 -> checkBoxDrink(drinkCb7));
            drinkCb8.setOnClickListener(view124 -> checkBoxDrink(drinkCb8));
            drinkCb9.setOnClickListener(view125 -> checkBoxDrink(drinkCb9));
            drinkCb10.setOnClickListener(view126 -> checkBoxDrink(drinkCb10));

        });

        // DESSERTS

        ArrayList<String> listDesserts = new ArrayList<>();
        listDesserts.add("Tarrina de helado");
        listDesserts.add("Bizcocho");
        listDesserts.add("Crepes");
        listDesserts.add("Porción de tarta");
        listDesserts.add("Plátano");
        listDesserts.add("Manzana");
        listDesserts.add("Trufas de chocolate");
        listDesserts.add("Caja de bombones");
        listDesserts.add("Actimel");
        listDesserts.add("Yogur líquido");


        ImageView dessertsDeploymentArrow = v.findViewById(R.id.desplegableDesserts);
        dessertsDeploymentArrow.setImageResource(R.drawable.arrow_down);
        RelativeLayout relativeLayoutDesserts = v.findViewById(R.id.eleccionDesserts);

        linearLayoutDesserts = v.findViewById(R.id.checkBoxDesserts);

        dessertCb1 = v.findViewById(R.id.dessertCb1);
        fillList(listDesserts, dessertCb1, positionListDesserts++);
        dessertCb2 = v.findViewById(R.id.dessertCb2);
        fillList(listDesserts, dessertCb2, positionListDesserts++);
        dessertCb3 = v.findViewById(R.id.dessertCb3);
        fillList(listDesserts, dessertCb3, positionListDesserts++);
        dessertCb4 = v.findViewById(R.id.dessertCb4);
        fillList(listDesserts, dessertCb4, positionListDesserts++);
        dessertCb5 = v.findViewById(R.id.dessertCb5);
        fillList(listDesserts, dessertCb5, positionListDesserts++);
        dessertCb6 = v.findViewById(R.id.dessertCb6);
        fillList(listDesserts, dessertCb6, positionListDesserts++);
        dessertCb7 = v.findViewById(R.id.dessertCb7);
        fillList(listDesserts, dessertCb7, positionListDesserts++);
        dessertCb8 = v.findViewById(R.id.dessertCb8);
        fillList(listDesserts, dessertCb8, positionListDesserts++);
        dessertCb9 = v.findViewById(R.id.dessertCb9);
        fillList(listDesserts, dessertCb9, positionListDesserts++);
        dessertCb10 = v.findViewById(R.id.dessertCb10);
        fillList(listDesserts, dessertCb10, positionListDesserts++);

        selectedDesserts = new ArrayList<>();
        relativeLayoutDesserts.setOnClickListener(view -> {

            if (showDessertsCheckbox % 2 != 0) {
                linearLayoutDesserts.setVisibility(View.GONE);
                showDessertsCheckbox++;
            } else {
                linearLayoutDesserts.setVisibility(View.VISIBLE);
                showDessertsCheckbox++;
            }

            dessertCb1.setOnClickListener(view113 -> checkBoxDessert(dessertCb1));
            dessertCb2.setOnClickListener(view114 -> checkBoxDessert(dessertCb2));
            dessertCb3.setOnClickListener(view115 -> checkBoxDessert(dessertCb3));
            dessertCb4.setOnClickListener(view116 -> checkBoxDessert(dessertCb4));
            dessertCb5.setOnClickListener(view117 -> checkBoxDessert(dessertCb5));
            dessertCb6.setOnClickListener(view118 -> checkBoxDessert(dessertCb6));
            dessertCb7.setOnClickListener(view127 -> checkBoxDessert(dessertCb7));
            dessertCb8.setOnClickListener(view128 -> checkBoxDessert(dessertCb8));
            dessertCb9.setOnClickListener(view129 -> checkBoxDessert(dessertCb9));
            dessertCb10.setOnClickListener(view130 -> checkBoxDessert(dessertCb10));

        });

        // RESUMEN

        buttonReserve.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (selectedFilm.isEmpty()) {
                    Toasty.error(requireContext(), "NO HA SELECCIONADO LA PELÍCULA", Toasty.LENGTH_SHORT, true).show();
                } else if (selectedDateTime == null) {
                    Toasty.error(requireContext(), "NO HA SELECCIONADO LA FECHA DE LA RESERVA", Toasty.LENGTH_SHORT, true).show();
                } else if (selectedSnacks.isEmpty() && selectedDrinks.isEmpty() && selectedDesserts.isEmpty()) {
                    showSummary();
                } else {
                    checkFirestore(selectedSnacks, selectedDrinks, selectedDesserts);
                }
            }
        });


        return v;
    }
    private void pickDateTime() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar minDate = (Calendar) calendar.clone();
        minDate.add(Calendar.DAY_OF_MONTH, 1);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(year1, monthOfYear, dayOfMonth);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view1, hourOfDay, minute1) -> {
                if (hourOfDay < 10 || hourOfDay > 22) {
                    Toasty.error(requireContext(), "La hora debe estar entre las 10 AM y las 10 PM", Toasty.LENGTH_SHORT, true).show();
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);
                    calendar.set(Calendar.SECOND, 0);

                    selectedDateTime = calendar.getTime();

                    formattedDateTime = formatDate(selectedDateTime);
                    dateText.setText(formattedDateTime);
                }
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public void fillList(ArrayList<String> arrayList, CheckBox cb, int position) {
        cb.setText(arrayList.get(position));
    }

    public void checkBoxSnack(CheckBox cb) {
        String snack = cb.getText().toString();

        if (cb.isChecked()) {
            boolean encontrado = false;

            for (int i = 0; i < selectedSnacks.size(); i++) {
                if (selectedSnacks.get(i).equals(snack)) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                selectedSnacks.add(snack);
            }

        } else {

            boolean encontrado = false;

            for (int i = 0; i < selectedSnacks.size(); i++) {
                if (selectedSnacks.get(i).equals(snack)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                selectedSnacks.remove(snack);
            }
        }
    }

    public void checkBoxDrink(CheckBox cb) {
        String drink = cb.getText().toString();

        if (cb.isChecked()) {
            boolean encontrado = false;

            for (int i = 0; i < selectedDrinks.size(); i++) {
                if (selectedDrinks.get(i).equals(drink)) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                selectedDrinks.add(drink);
            }

        } else {

            boolean encontrado = false;

            for (int i = 0; i < selectedDrinks.size(); i++) {
                if (selectedDrinks.get(i).equals(drink)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                selectedDrinks.remove(drink);
            }
        }
    }

    public void checkBoxDessert(CheckBox cb) {
        String dessert = cb.getText().toString();

        if (cb.isChecked()) {
            boolean encontrado = false;

            for (int i = 0; i < selectedDesserts.size(); i++) {
                if (selectedDesserts.get(i).equals(dessert)) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                selectedDesserts.add(dessert);
            }

        } else {

            boolean encontrado = false;

            for (int i = 0; i < selectedDesserts.size(); i++) {
                if (selectedDesserts.get(i).equals(dessert)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                selectedDesserts.remove(dessert);
            }
        }
    }


    private void checkFirestore(ArrayList<String> selectedSnacks, ArrayList<String> selectedDrinks, ArrayList<String> selectedDesserts) {
        selectedSnacksDetails.clear();
        selectedDrinksDetails.clear();
        selectedDessertsDetails.clear();

        // Número atómico --> Contador para que no se repitan registros
        AtomicInteger snackCounter = new AtomicInteger(0);
        AtomicInteger drinkCounter = new AtomicInteger(0);
        AtomicInteger dessertCounter = new AtomicInteger(0);

        for (int i = 0; i < selectedSnacks.size();i++)
        {
            db.collection("Snack").whereEqualTo("Name", selectedSnacks.get(i)).limit(1).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("Name");
                            double price = document.getDouble("Price");
                            String imageUrl = document.getString("Image");
                            int id = Objects.requireNonNull(document.getLong("IdSnack")).intValue();

                            assert imageUrl != null;
                            Snack snack = new Snack(id, imageUrl, name, price);
                            selectedSnacksDetails.add(snack);

                            if (snackCounter.incrementAndGet() == selectedSnacks.size() && drinkCounter.get() == selectedDrinks.size() && dessertCounter.get() == selectedDesserts.size() ) {
                                showSummary();
                            }

                        }


                    });
        }

        for (int i = 0; i < selectedDrinks.size();i++)
        {
            db.collection("Drink").whereEqualTo("Name", selectedDrinks.get(i)).limit(1).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("Name");
                            double price = document.getDouble("Price");
                            String imageUrl = document.getString("Image");
                            int id = Objects.requireNonNull(document.getLong("IdDrink")).intValue();

                            assert imageUrl != null;
                            Drink drink = new Drink(id, imageUrl, name, price);
                            selectedDrinksDetails.add(drink);

                            if (drinkCounter.incrementAndGet() == selectedDrinks.size() && snackCounter.get() == selectedSnacks.size() && dessertCounter.get() == selectedDesserts.size()) {
                                showSummary();
                            }


                        }
                    });

        }

        for (int i = 0; i < selectedDesserts.size();i++)
        {
            db.collection("Dessert").whereEqualTo("Name", selectedDesserts.get(i)).limit(1).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("Name");
                            double price = document.getDouble("Price");
                            String imageUrl = document.getString("Image");
                            int id = Objects.requireNonNull(document.getLong("IdDessert")).intValue();


                            Dessert dessert = new Dessert(id, imageUrl, name, price);
                            selectedDessertsDetails.add(dessert);
                            if (dessertCounter.incrementAndGet() == selectedDesserts.size() && drinkCounter.get() == selectedDrinks.size()&& snackCounter.get() == selectedSnacks.size() ) {
                                showSummary();
                            }

                        }

                    });
        }

    }
    @SuppressLint("SetTextI18n")
    private void showSummary() {

        StringBuilder foodSummary = new StringBuilder();
        totalPrice = 7;

        if (!selectedSnacksDetails.isEmpty()) {
            foodSummary.append("\n\nAperitivos:\n");
            for (Snack snack : selectedSnacksDetails) {
                foodSummary.append(snack.getName()).append(" - ").append(snack.getPrice()).append("€\n");
                totalPrice+= snack.getPrice();
            }
            foodSummary.append("\n-----------------------");
        }

        if (!selectedDrinksDetails.isEmpty()) {

            foodSummary.append("\n\nBebidas:\n");
            for (Drink drink : selectedDrinksDetails) {
                foodSummary.append(drink.getName()).append(" - ").append(drink.getPrice()).append("€\n");
                totalPrice+= drink.getPrice();
            }
            foodSummary.append("\n-----------------------");
        }

        if (!selectedDessertsDetails.isEmpty()) {

            foodSummary.append("\n\nPostres:\n");
            for (Dessert dessert : selectedDessertsDetails) {
                foodSummary.append(dessert.getName()).append(" - ").append(dessert.getPrice()).append("€\n");
                totalPrice+= dessert.getPrice();
            }
            foodSummary.append("\n-----------------------");
        }

        foodSummary.append("\n\nFecha: ").append(formattedDateTime);

        discounts = new ArrayList<>();
        discounts.add("Chollometro10");
        discounts.add("Cinéfilo");
        discounts.add("Cinematics");

        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.reservation_summary);
        dialog.setCancelable(false);

        Button buttonCancel = dialog.findViewById(R.id.botonCancel);
        Button buttonOk = dialog.findViewById(R.id.botonOk);
        TextView data = dialog.findViewById(R.id.data);
        TextView tvCode = dialog.findViewById(R.id.tvCode);
        code = dialog.findViewById(R.id.etCode);
        ImageView checkCode = dialog.findViewById(R.id.check);
        price = dialog.findViewById(R.id.tvPrice);
        priceDiscount = dialog.findViewById(R.id.tvPriceDiscount);

        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.reservation_summary_custom);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(drawable);

        String priceFormat = String.format(Locale.US,"%.2f", totalPrice);
        data.setText(filmSummary + foodSummary);
        price.setText("Precio: " + priceFormat + "€");
        price.setPaintFlags(price.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        initialPrice = totalPrice;

        if(selectedSnacks.size() == 1 && (selectedSnacks.get(0).equals("Cubo de palomitas") || selectedSnacks.get(0).equals("Nachos"))
                && selectedDrinks.size() == 1 && (selectedDrinks.get(0).equals("Refresco de cola") || selectedDrinks.get(0).equals("Refresco de naranja") || selectedDrinks.get(0).equals("Refresco de limón"))
                && selectedDesserts.isEmpty())
        {
            // COMBO CLÁSICO
            totalPrice = 12;
            isDiscount = true;
            tvCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceDiscount.setVisibility(View.VISIBLE);

            String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
            priceDiscount.setText("Precio (con promoción): " + priceFormat2 + "€");

        } else if (selectedDrinks.size() == 1 && selectedDrinks.get(0).equals("Botella de agua")
                && selectedDesserts.size() == 1 && (selectedDesserts.get(0).equals("Plátano") || selectedDesserts.get(0).equals("Manzana"))
                && selectedSnacks.isEmpty())
        {
            // COMBO FITNESS
            totalPrice = 7.5;
            isDiscount = true;
            tvCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceDiscount.setVisibility(View.VISIBLE);

            String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
            priceDiscount.setText("Precio (con promoción): " + priceFormat2 + "€");

        } else if (selectedSnacks.size() == 1 && (selectedSnacks.get(0).equals("Cacahuetes salados") || selectedSnacks.get(0).equals("Bolsa de anacardos"))
                && selectedDrinks.size() == 1 && (selectedDrinks.get(0).equals("Refresco de cola") || selectedDrinks.get(0).equals("Refresco de naranja") || selectedDrinks.get(0).equals("Refresco de limón"))
                && selectedDesserts.isEmpty())
        {
            // COMBO SECO
            totalPrice = 10;
            isDiscount = true;
            tvCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceDiscount.setVisibility(View.VISIBLE);

            String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
            priceDiscount.setText("Precio (con promoción): " + priceFormat2 + "€");

        } else if (selectedSnacks.size() == 1 && (selectedSnacks.get(0).equals("Bolsa de patatas") || selectedSnacks.get(0).equals("Nachos") || selectedSnacks.get(0).equals("Bolsa de ganchitos"))
                && selectedDrinks.size() == 1 && (selectedDrinks.get(0).equals("Bebida gaseosa con limón") || selectedDrinks.get(0).equals("Té de limón"))
                && selectedDesserts.size() == 1 && (selectedDesserts.get(0).equals("Crepes") || selectedDesserts.get(0).equals("Porción de tarta")))
        {

            // COMBO MENÚ
            totalPrice = 12;
            isDiscount = true;
            tvCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceDiscount.setVisibility(View.VISIBLE);

            String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
            priceDiscount.setText("Precio (con promoción): " + priceFormat2 + "€");

        } else if (selectedSnacks.size() == 1 && selectedSnacks.get(0).equals("Bolsa de chuches")
                && selectedDrinks.size() == 1 && selectedDrinks.get(0).equals("Champín")
                && selectedDesserts.size() == 1 && (selectedDesserts.get(0).equals("Trufas de chocolate") || selectedDesserts.get(0).equals("Caja de bombones")))
        {
            // COMBO CELEBRACIÓN
            totalPrice = 20;
            isDiscount = true;
            tvCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceDiscount.setVisibility(View.VISIBLE);

            String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
            priceDiscount.setText("Precio (con promoción): " + priceFormat2 + "€");

        } else if((selectedSnacks.size() == 2 && (selectedSnacks.get(0).equals("Cacahuetes salados") || selectedSnacks.get(1).equals("Bolsa de chuches") && (selectedSnacks.get(1).equals("Cacahuetes salados") || selectedSnacks.get(1).equals("Bolsa de chuches")))
                && selectedDrinks.size() == 1 && selectedDrinks.get(0).equals("Té de limón")
                && selectedDesserts.size() == 1 && selectedDesserts.get(0).equals("Tarrina de helado")))
        {
            // COMBO ROCO
            totalPrice = 18;
            isDiscount = true;
            tvCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceDiscount.setVisibility(View.VISIBLE);

            String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
            priceDiscount.setText("Precio (con promoción): " + priceFormat2 + "€");

        } else
        {
            tvCode.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            checkCode.setVisibility(View.VISIBLE);

            checkCode.setOnClickListener(view -> {

                totalPrice = initialPrice;

                boolean correcto = false;

                for (int i = 0; i < discounts.size(); i++) {

                    discount = code.getText().toString();

                    if(discounts.get(i).equals(discount))
                    {
                        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        switch (i)
                        {
                            case 0:
                                totalPrice = totalPrice - totalPrice * 0.20;
                                break;
                            case 1:
                                totalPrice = totalPrice - totalPrice * 0.15;
                                break;
                            case 2:
                                totalPrice = totalPrice - totalPrice * 0.10;
                                break;
                        }

                        String priceFormat2 = String.format(Locale.US,"%.2f", totalPrice);
                        priceDiscount.setVisibility(View.VISIBLE);
                        priceDiscount.setText("Precio (con descuento): " + priceFormat2 + "€");
                        isDiscount = true;
                        correcto = true;
                        Toasty.success(requireContext(),"Código aplicado con éxito",Toasty.LENGTH_LONG).show();

                    }
                }

                if(!correcto)
                {
                    totalPrice = initialPrice;
                    isDiscount = false;
                    Toasty.error(requireContext(), "Código incorrecto", Toasty.LENGTH_SHORT, true).show();
                    price.setPaintFlags(price.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    priceDiscount.setVisibility(View.GONE);
                }

            });
        }

        dialog.show();

        buttonOk.setOnClickListener(view -> {
            dialog.dismiss();

            checkFilm(film -> {
                if (film != null) {
                    selectedFilmDetails = film;
                    Toasty.success(requireContext(), "Reserva confirmada", Toasty.LENGTH_LONG, true).show();

                    String mail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

                    db.collection("User").whereEqualTo("Email", mail).limit(1).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        ArrayList<HashMap<String, Object>> reservationsData = (ArrayList<HashMap<String, Object>>) document.get("Reservations");

                                        int id;
                                        if (reservationsData == null) {
                                            reservationsData = new ArrayList<>();
                                            id = 1;
                                        } else {
                                            id = reservationsData.size();
                                            id++;
                                        }

                                        BigDecimal bd = BigDecimal.valueOf(totalPrice);
                                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                                        double roundedValue = bd.doubleValue();

                                        Reservation newReservation = new Reservation(id, selectedFilmDetails, selectedSnacksDetails, selectedDrinksDetails, selectedDessertsDetails, roundedValue, selectedDateTime);

                                        HashMap<String, Object> newReservationData = new HashMap<>();
                                        newReservationData.put("IdReservation", newReservation.getId());

                                        // Añadir correo electrónico del usuario a los datos de la reserva
                                        newReservationData.put("UserEmail", mail);

                                        HashMap<String, Object> filmData = new HashMap<>();
                                        filmData.put("idFilm", selectedFilmDetails.getIdFilm());
                                        filmData.put("title", selectedFilmDetails.getTitle());
                                        filmData.put("year", selectedFilmDetails.getYear());
                                        filmData.put("director", selectedFilmDetails.getDirector());
                                        filmData.put("generos", selectedFilmDetails.getGeneros());
                                        filmData.put("sinopsis", selectedFilmDetails.getSinopsis());
                                        filmData.put("reparto", selectedFilmDetails.getReparto());
                                        filmData.put("rating", selectedFilmDetails.getRating());
                                        filmData.put("reviews", selectedFilmDetails.getReviews());
                                        filmData.put("url", selectedFilmDetails.getURL());

                                        newReservationData.put("film", filmData);

                                        List<HashMap<String, Object>> snackList = new ArrayList<>();
                                        for (Snack snack : selectedSnacksDetails) {
                                            HashMap<String, Object> snackData = new HashMap<>();
                                            snackData.put("idSnack", snack.getIdSnack());
                                            snackData.put("name", snack.getName());
                                            snackData.put("price", snack.getPrice());
                                            snackData.put("url", snack.getURL());
                                            snackList.add(snackData);
                                        }
                                        newReservationData.put("ReservationSnack", snackList);

                                        List<HashMap<String, Object>> drinkList = new ArrayList<>();
                                        for (Drink drink : selectedDrinksDetails) {
                                            HashMap<String, Object> drinkData = new HashMap<>();
                                            drinkData.put("idDrink", drink.getIdDrink());
                                            drinkData.put("name", drink.getName());
                                            drinkData.put("price", drink.getPrice());
                                            drinkData.put("url", drink.getURL());
                                            drinkList.add(drinkData);
                                        }
                                        newReservationData.put("ReservationDrink", drinkList);

                                        List<HashMap<String, Object>> dessertList = new ArrayList<>();
                                        for (Dessert dessert : selectedDessertsDetails) {
                                            HashMap<String, Object> dessertData = new HashMap<>();
                                            dessertData.put("idDessert", dessert.getIdDessert());
                                            dessertData.put("name", dessert.getName());
                                            dessertData.put("price", dessert.getPrice());
                                            dessertData.put("url", dessert.getURL());
                                            dessertList.add(dessertData);
                                        }
                                        newReservationData.put("ReservationDessert", dessertList);

                                        newReservationData.put("FinalPrice", newReservation.getFinalPrice());
                                        newReservationData.put("Date", newReservation.getDate());

                                        String reservationDocId = mail + "_" + newReservation.getId();

                                        ArrayList<HashMap<String, Object>> finalReservationsData = reservationsData;
                                        db.collection("Reservations").document(reservationDocId).set(newReservationData)
                                                .addOnSuccessListener(documentReference -> {

                                                    newReservationData.remove("UserEmail");
                                                    finalReservationsData.add(newReservationData);

                                                    document.getReference().update("Reservations", finalReservationsData)
                                                            .addOnSuccessListener(aVoid -> generateAndDownloadReservationDocument(newReservation))
                                                            .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al actualizar el documento del usuario", Toasty.LENGTH_LONG, true).show());
                                                })
                                                .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al añadir la reserva a la colección Reservations", Toasty.LENGTH_LONG, true).show());
                                    }
                                } else {
                                    Toasty.error(requireContext(), "Usuario no encontrado", Toasty.LENGTH_LONG, true).show();
                                }
                            })
                            .addOnFailureListener(e -> Toasty.error(requireContext(), "Error al obtener el usuario", Toasty.LENGTH_LONG, true).show());
                } else {
                    Toasty.error(requireContext(), "La película está vacía", Toasty.LENGTH_LONG, true).show();
                }
            });
        });

        buttonCancel.setOnClickListener(view -> {
            selectedSnacksDetails.clear();
            selectedDrinksDetails.clear();
            selectedDessertsDetails.clear();
            selectedFilmDetails = null;
            isDiscount = false;
            dialog.dismiss();
        });
    }
    private interface FilmLoadCallback {
        void onFilmLoaded(Film film);
    }
    private void checkFilm(FilmLoadCallback callback) {
        db.collection("Film").whereEqualTo("Title", selectedFilm).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

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

                Film f = new Film(id, posterUrl, title, year, director, genresList, synopsis, castingList, rating, reviewsList);
                callback.onFilmLoaded(f);
                return;
            }
            callback.onFilmLoaded(null);
        }).addOnFailureListener(e -> callback.onFilmLoaded(null));
    }
    @SuppressLint("SimpleDateFormat")
    public void generateAndDownloadReservationDocument(Reservation res) {

        String mail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.logo);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        String base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        // ESTILOS

        StringBuilder content = new StringBuilder("<html><head>");
        content.append("<style>");
        content.append("body { font-family: Helvetica, Arial, sans-serif; margin: 0; padding: 0; background-color: #FFFFFFFF; }");
        content.append(".header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #ccc; padding-bottom: 10px; margin-bottom: 20px; }");
        content.append(".logo { width: 100px; height: auto; }");
        content.append(".header h2 { margin: 0; }");
        content.append(".details { margin-bottom: 20px; }");
        content.append(".details p { margin: 5px 0; }");
        content.append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
        content.append("th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }");
        content.append("th { background-color: #e2e2e2; }");
        content.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        content.append(".total { text-align: right; margin-top: 20px; font-size: 18px; }");
        content.append(".tachado { text-decoration: line-through; color: #000; }");
        content.append(".empty { font-style: italic; color: #888; }");
        content.append(".footer { text-align: center; margin-top: 20px; font-size: 14px; color: #888; }");
        content.append(".important { font-weight: bold; color: #d9534f; }");
        content.append("@media (max-width: 600px) {");
        content.append("  .container { padding: 10px; }");
        content.append("  .header { flex-direction: column; text-align: center; }");
        content.append("  .header img { margin-bottom: 10px; }");
        content.append("}");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<img src='data:image/png;base64,").append(base64Image).append("' alt='Logo' class='logo'/>");
        content.append("<div>");
        content.append("<h2>Justificante de Pago</h2>");
        content.append("<p>Fecha actual: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("<div class='details'>");
        content.append("<p>Correo electrónico: ").append(mail).append("</p>");
        content.append("<p>Código de la Reserva: ").append(res.getId()).append("</p>");
        content.append("<p>Fecha de la reserva: ").append(formattedDateTime).append("</p>");
        content.append("<p class='important'><strong>Recuerde presentar este documento en el mostrador para abonar el precio</strong></p>");
        content.append("</div>");
        content.append("<table>");
        content.append("<tr><th>Descripción</th><th>Precio</th></tr>");
        content.append("<tr><td>Entrada para ").append(res.getFilm().getTitle()).append("</td><td>7.00 €</td></tr>");

        if (res.getReservationSnack() != null && !res.getReservationSnack().isEmpty()) {
            for (int i = 0; i < res.getReservationSnack().size(); i++) {
                content.append("<tr><td>Snack: ").append(res.getReservationSnack().get(i).getName()).append("</td><td>").append(res.getReservationSnack().get(i).getPrice()).append(" €</td></tr>");
            }
        } else {
            content.append("<tr><td colspan='2' class='empty'>No se han seleccionado snacks</td></tr>");
        }

        if (res.getReservationDrink() != null && !res.getReservationDrink().isEmpty()) {
            for (int i = 0; i < res.getReservationDrink().size(); i++) {
                content.append("<tr><td>Bebida: ").append(res.getReservationDrink().get(i).getName()).append("</td><td>").append(res.getReservationDrink().get(i).getPrice()).append(" €</td></tr>");
            }
        } else {
            content.append("<tr><td colspan='2' class='empty'>No se han seleccionado bebidas</td></tr>");
        }

        if (res.getReservationDessert() != null && !res.getReservationDessert().isEmpty()) {
            for (int i = 0; i < res.getReservationDessert().size(); i++) {
                content.append("<tr><td>Postre: ").append(res.getReservationDessert().get(i).getName()).append("</td><td>").append(res.getReservationDessert().get(i).getPrice()).append(" €</td></tr>");
            }
        } else {
            content.append("<tr><td colspan='2' class='empty'>No se han seleccionado postres</td></tr>");
        }
        content.append("</table>");

        BigDecimal bd = BigDecimal.valueOf(initialPrice);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double roundedValue = bd.doubleValue();

        if (isDiscount) {
            content.append("<p class='total'>Precio inicial: <span class='tachado'>").append(roundedValue).append(" €</span></p>");
            content.append("<p class='total'>Precio con descuento: ").append(res.getFinalPrice()).append(" €</p>");
        } else {
            content.append("<p class='total'>Precio final: ").append(res.getFinalPrice()).append(" €</p>");
        }

        content.append("<p class='footer'>¡Gracias por confiar en Cinematics! Disfrute de su película.</p>");

        content.append("</div>");
        content.append("</body></html>");

        try {

            String fileName = "reserva_" + res.getId() + ".pdf";
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, fileName);

            OutputStream OS = Files.newOutputStream(file.toPath());

            HtmlConverter.convertToPdf(content.toString(), OS);

            OS.close();

            Toasty.success(requireContext(), "Documento de reserva generado correctamente", Toasty.LENGTH_LONG, true).show();

            downloadFileAndOpenPDF(getContext(), file);

        } catch (IOException e) {
            e.printStackTrace();
            Toasty.error(requireContext(), "Error al generar el documento de reserva", Toasty.LENGTH_LONG, true).show();
        }
    }

    private void downloadFileAndOpenPDF(Context context, File file) {

        MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, (path, uri) -> {

            isDiscount = false;
            Toasty.success(context, "Documento de reserva descargado correctamente", Toasty.LENGTH_LONG, true).show();

            Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toasty.error(context, "No se encontró una aplicación para abrir el PDF", Toasty.LENGTH_SHORT, true).show();
            }
        });
    }
    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
    @Override
    public void onResume() {
        super.onResume();

        linearLayoutSnacks.setVisibility(View.GONE);
        linearLayoutDrinks.setVisibility(View.GONE);
        linearLayoutDesserts.setVisibility(View.GONE);

        if(autoCompleteTextViewFilms != null)
            autoCompleteTextViewFilms.setText("");

        if(snackCb1 != null)
            snackCb1.setChecked(false);
        if(snackCb2 != null)
            snackCb2.setChecked(false);
        if(snackCb3 != null)
            snackCb3.setChecked(false);
        if(snackCb4 != null)
            snackCb4.setChecked(false);
        if(snackCb5 != null)
            snackCb5.setChecked(false);
        if(snackCb6 != null)
            snackCb6.setChecked(false);
        if(snackCb7 != null)
            snackCb7.setChecked(false);
        if(snackCb8 != null)
            snackCb8.setChecked(false);
        if(snackCb9 != null)
            snackCb9.setChecked(false);
        if(snackCb10 != null)
            snackCb10.setChecked(false);

        if(drinkCb1 != null)
            drinkCb1.setChecked(false);
        if(drinkCb2 != null)
            drinkCb2.setChecked(false);
        if(drinkCb3 != null)
            drinkCb3.setChecked(false);
        if(drinkCb4 != null)
            drinkCb4.setChecked(false);
        if(drinkCb5 != null)
            drinkCb5.setChecked(false);
        if(drinkCb6 != null)
            drinkCb6.setChecked(false);
        if(drinkCb7 != null)
            drinkCb7.setChecked(false);
        if(drinkCb8 != null)
            drinkCb8.setChecked(false);
        if(drinkCb9 != null)
            drinkCb9.setChecked(false);
        if(drinkCb10 != null)
            drinkCb10.setChecked(false);

        if(dessertCb1 != null)
            dessertCb1.setChecked(false);
        if(dessertCb2 != null)
            dessertCb2.setChecked(false);
        if(dessertCb3 != null)
            dessertCb3.setChecked(false);
        if(dessertCb4 != null)
            dessertCb4.setChecked(false);
        if(dessertCb5 != null)
            dessertCb5.setChecked(false);
        if(dessertCb6 != null)
            dessertCb6.setChecked(false);
        if(dessertCb7 != null)
            dessertCb7.setChecked(false);
        if(dessertCb8 != null)
            dessertCb8.setChecked(false);
        if(dessertCb9 != null)
            dessertCb9.setChecked(false);
        if(dessertCb10 != null)
            dessertCb10.setChecked(false);

        if(selectedSnacks != null)
            selectedSnacks.clear();
        if(selectedDrinks != null)
            selectedDrinks.clear();
        if(selectedDesserts != null)
            selectedDesserts.clear();

        if(dateText != null)
        dateText.setText("Fecha de reserva");

    }

}