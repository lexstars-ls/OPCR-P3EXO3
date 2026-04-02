package com.openclassrooms.tajmahal;

import com.openclassrooms.tajmahal.data.service.RestaurantApi;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;

public class FakeRestaurantApi implements RestaurantApi {

    private final List<Review> reviews = new ArrayList<>();

    public FakeRestaurantApi() {
        // Exemple cohérent avec ta vraie FakeAPI
        reviews.add(new Review(
                "Alice",
                "https://example.com/alice.jpg",
                "Très bon restaurant",
                3
        ));

        reviews.add(new Review(
                "Bob",
                "https://example.com/bob.jpg",
                "Bon resto",
                4
        ));
    }

    @Override
    public Restaurant getRestaurant() {
        // Pour nos tests sur les reviews, on s’en fiche un peu
        return new Restaurant(
                "Fake", "Type", "Horaires",
                "Adresse", "site", "tel",
                true, true
        );
    }

    @Override
    public List<Review> getReviews() {
        return reviews;
    }

}