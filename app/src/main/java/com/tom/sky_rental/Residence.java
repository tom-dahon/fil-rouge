package com.tom.sky_rental;

public class Residence {
    private String name;
    private String location;
    private String image_url;
    private float ratings;
    private int num_reviews;
    private int rooms;
    private String check_in;
    private String check_out;

    // Constructeur sans argument requis pour Firestore
    public Residence() {}

    public Residence(String name, String location, String image_url, float ratings, int num_reviews, int rooms, String check_in, String check_out) {
        this.name = name;
        this.location = location;
        this.image_url = image_url;
        this.ratings = ratings;
        this.num_reviews = num_reviews;
        this.rooms = rooms;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public int getNumReviews() {
        return num_reviews;
    }

    public void setNumReviews(int num_reviews) {
        this.num_reviews = num_reviews;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getCheckIn() {
        return check_in;
    }

    public void setCheckIn(String check_in) {
        this.check_in = check_in;
    }

    public String getCheckOut() {
        return check_out;
    }

    public void setCheckOut(String check_out) {
        this.check_out = check_out;
    }
}
