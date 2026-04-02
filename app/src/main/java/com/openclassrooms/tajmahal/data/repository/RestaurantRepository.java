package com.openclassrooms.tajmahal.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.tajmahal.data.service.RestaurantApi;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RestaurantRepository {

    private final RestaurantApi restaurantApi;
    private final MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>();

    @Inject
    public RestaurantRepository(@NonNull RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;

        // On copie la liste pour la rendre modifiable
        List<Review> initial = restaurantApi.getReviews();
        if (initial == null) initial = new ArrayList<>();
        else initial = new ArrayList<>(initial);

        reviewsLiveData.setValue(initial);
    }

    public LiveData<Restaurant> getRestaurant() {
        return new MutableLiveData<>(restaurantApi.getRestaurant());
    }

    public LiveData<List<Review>> getReviews() {
        return reviewsLiveData;
    }

    public int getTotalReviews() {
        List<Review> reviews = reviewsLiveData.getValue();
        return (reviews != null) ? reviews.size() : 0;
    }

    public double getReviewsAverage() {
        List<Review> reviews = reviewsLiveData.getValue();
        if (reviews == null || reviews.isEmpty()) return 0;

        double total = 0;
        for (Review review : reviews) {
            total += review.getRate();
        }
        return total / reviews.size();
    }

    public int[] getReviewsRepartition() {
        int[] repartition = new int[5];

        List<Review> reviews = reviewsLiveData.getValue();
        if (reviews == null) return repartition;

        for (Review review : reviews) {
            int rate = review.getRate();
            if (rate >= 1 && rate <= 5) {
                repartition[rate - 1]++;
            }
        }
        return repartition;
    }

    // LOGIQUE MÉTIER : un rate est obligatoire
    public boolean addReview(Review newReview) {

        if (newReview.getRate() == 0) {
            return false; // refusé
        }

        List<Review> current = reviewsLiveData.getValue();
        if (current == null) current = new ArrayList<>();
        else current = new ArrayList<>(current);

        current.add(0, newReview);
        reviewsLiveData.setValue(current);

        return true; // accepté
    }
}
