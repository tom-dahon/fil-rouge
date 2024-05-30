package com.tom.sky_rental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExploreActivity extends AppCompatActivity {

    private EditText locationEditText, roomsEditText;
    private Spinner checkInSpinner, checkOutSpinner;
    private Button searchButton;
    private RecyclerView residencesRecyclerView;

    private FirebaseFirestore db;
    private ResidencesAdapter adapter;
    private List<Residence> residenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_activity);

        locationEditText = findViewById(R.id.locationEditText);
        roomsEditText = findViewById(R.id.roomsEditText);
        checkInSpinner = findViewById(R.id.checkInSpinner);
        checkOutSpinner = findViewById(R.id.checkOutSpinner);
        searchButton = findViewById(R.id.searchButton);
        residencesRecyclerView = findViewById(R.id.apartmentsRecyclerView);

        db = FirebaseFirestore.getInstance();
        residenceList = new ArrayList<>();
        adapter = new ResidencesAdapter(this, residenceList);

        residencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        residencesRecyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> checkInAdapter = ArrayAdapter.createFromResource(this, R.array.checkin_array, android.R.layout.simple_spinner_item);
        checkInAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkInSpinner.setAdapter(checkInAdapter);

        ArrayAdapter<CharSequence> checkOutAdapter = ArrayAdapter.createFromResource(this, R.array.checkout_array, android.R.layout.simple_spinner_item);
        checkOutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkOutSpinner.setAdapter(checkOutAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResidences();
            }
        });

        fetchPopularDestinations();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchPopularDestinations() {

        CollectionReference residencesCollection = db.collection("residences");

        // Get the first 8 documents in the "residences" collection
        residencesCollection.limit(8).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the documents
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Loop through documents and create Residence objects
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                String name = documentSnapshot.getString("name");
                                String location = documentSnapshot.getString("location");
                                String image_url = documentSnapshot.getString("image_url");
                                float ratings = Objects.requireNonNull(documentSnapshot.getLong("ratings"));
                                int num_reviews = Objects.requireNonNull(documentSnapshot.getLong("num_reviews")).intValue();
                                int rooms = Objects.requireNonNull(documentSnapshot.getLong("rooms")).intValue();
                                String check_in = documentSnapshot.getString("check_in");
                                String check_out = documentSnapshot.getString("check_out");


                                Residence residence = new Residence(name, location, image_url, ratings, num_reviews, rooms, check_in, check_out);
                                residenceList.add(residence);
                            }
                        }

                        for (Residence residence : residenceList) {
                            System.out.println("Residence Name: " + residence.getName());
                            System.out.println("Residence Image Url: " + residence.getImageUrl());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Task failed with an exception
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }

    private void searchResidences() {
        String location = locationEditText.getText().toString().trim();
        String rooms = roomsEditText.getText().toString().trim();

        db.collection("residences")
                .whereEqualTo("location", location)
                .whereEqualTo("rooms", Integer.parseInt(rooms))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        residenceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Residence residence = document.toObject(Residence.class);
                            residenceList.add(residence);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ExploreActivity.this, "Search failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
