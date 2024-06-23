package com.tom.sky_rental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ExploreActivity extends AppCompatActivity {

    private EditText locationEditText, roomsEditText;
    private TextView checkIn, checkOut;
    private Button selectStartDateButton, selectEndDateButton;
    private Button searchButton;
    private RecyclerView residencesRecyclerView;

    private FirebaseFirestore db;
    private ResidencesAdapter adapter;
    private List<Residence> residenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_activity);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ExploreActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_explore:
                    intent = new Intent(ExploreActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my_trips:
                    intent = new Intent(ExploreActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(ExploreActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        });

        locationEditText = findViewById(R.id.locationEditText);
        roomsEditText = findViewById(R.id.roomsEditText);
        searchButton = findViewById(R.id.searchButton);
        residencesRecyclerView = findViewById(R.id.apartmentsRecyclerView);

        db = FirebaseFirestore.getInstance();
        residenceList = new ArrayList<>();
        adapter = new ResidencesAdapter(this, residenceList);

        residencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        residencesRecyclerView.setAdapter(adapter);

        checkIn = findViewById(R.id.checkIn);
        checkOut = findViewById(R.id.checkOut);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = day + "/" + (month + 1) + "/" + year;
        checkIn.setText(currentDate);
        checkOut.setText(currentDate);

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkIn);
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkOut);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fermer le clavier quand on lance la recherche
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchResidences();
            }
        });

        fetchPopularDestinations();
    }

    private void showDatePickerDialog(final TextView dateTextView) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ExploreActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Note: month is 0-based, so add 1 to display it correctly
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateTextView.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchPopularDestinations() {

        CollectionReference residencesCollection = db.collection("residences");

        residencesCollection.limit(8).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                String id = documentSnapshot.getId();
                                String name = documentSnapshot.getString("name");
                                String location = documentSnapshot.getString("location");
                                String image_url = documentSnapshot.getString("image_url");
                                float ratings = Objects.requireNonNull(documentSnapshot.getLong("ratings"));
                                int num_reviews = Objects.requireNonNull(documentSnapshot.getLong("num_reviews")).intValue();
                                int rooms = Objects.requireNonNull(documentSnapshot.getLong("rooms")).intValue();
                                String check_in = documentSnapshot.getString("check_in");
                                String check_out = documentSnapshot.getString("check_out");


                                Residence residence = new Residence(id, name, location, image_url, ratings, num_reviews, rooms, check_in, check_out);
                                residenceList.add(residence);
                            }
                        }

                        for (Residence residence : residenceList) {
                            System.out.println("Residence Name: " + residence.getName());
                            System.out.println("Residence Image Url: " + residence.getImageUrl());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void searchResidences() {
        String searchLocation = locationEditText.getText().toString().trim();
        String searchRooms = roomsEditText.getText().toString().trim();

        db.collection("residences")
                .whereEqualTo("location", searchLocation)
                .whereEqualTo("rooms", Integer.parseInt(searchRooms))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        residenceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String location = document.getString("location");
                            String image_url = document.getString("image_url");
                            float ratings = Objects.requireNonNull(document.getLong("ratings"));
                            int num_reviews = Objects.requireNonNull(document.getLong("num_reviews")).intValue();
                            int rooms = Objects.requireNonNull(document.getLong("rooms")).intValue();
                            String check_in = document.getString("check_in");
                            String check_out = document.getString("check_out");


                            Residence residence = new Residence(id, name, location, image_url, ratings, num_reviews, rooms, check_in, check_out);
                            residenceList.add(residence);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ExploreActivity.this, "Search failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
