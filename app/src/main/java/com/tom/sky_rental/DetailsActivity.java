package com.tom.sky_rental;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager2 photoSlider;
    private TextView descriptionTextView;
    private LinearLayout reviewsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(DetailsActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_explore:
                    intent = new Intent(DetailsActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_my_trips:
                    intent = new Intent(DetailsActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(DetailsActivity.this, ExploreActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        });

        Residence residence = (Residence) getIntent().getSerializableExtra("RESIDENCE");

        photoSlider = findViewById(R.id.photoSlider);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        reviewsContainer = findViewById(R.id.reviewsContainer);

        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(residence.getImageUrl());

        PhotoSliderAdapter adapter = new PhotoSliderAdapter(photoUrls, this);
        photoSlider.setAdapter(adapter);

        descriptionTextView.setText("This is a beautiful apartment with a stunning view of the city. It comes with all amenities you need for a comfortable stay.");

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Alice", 5, "Amazing place! Had a great time."));
        reviews.add(new Review("Bob", 4, "Very good, but could be better with some improvements."));
        reviews.add(new Review("Charlie", 3, "Average experience."));

        for (Review review : reviews) {
            View reviewView = LayoutInflater.from(this).inflate(R.layout.item_review, reviewsContainer, false);
            TextView authorTextView = reviewView.findViewById(R.id.authorTextView);
            RatingBar ratingBar = reviewView.findViewById(R.id.ratingBar);
            TextView commentTextView = reviewView.findViewById(R.id.commentTextView);

            authorTextView.setText(review.getAuthor());
            ratingBar.setRating(review.getRating());
            commentTextView.setText(review.getComment());

            reviewsContainer.addView(reviewView);
        }
    }

    private static class Review {
        private String author;
        private int rating;
        private String comment;

        public Review(String author, int rating, String comment) {
            this.author = author;
            this.rating = rating;
            this.comment = comment;
        }

        public String getAuthor() {
            return author;
        }

        public int getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }
    }
}
