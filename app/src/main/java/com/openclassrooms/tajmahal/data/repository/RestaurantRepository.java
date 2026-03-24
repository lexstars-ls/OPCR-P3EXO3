package com.openclassrooms.tajmahal.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.tajmahal.data.service.RestaurantApi;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * This is the repository class for managing restaurant data. Repositories are responsible
 * for coordinating data operations from data sources such as network APIs, databases, etc.
 * <p>
 * Typically in an Android app built with architecture components, the repository will handle
 * the logic for deciding whether to fetch data from a network source or use data from a local cache.
 *
 * @see Restaurant
 * @see RestaurantApi
 */
@Singleton
public class RestaurantRepository {

    private final RestaurantApi restaurantApi;
    private final MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>();

    @Inject
    public RestaurantRepository(@NonNull RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
        reviewsLiveData.setValue(restaurantApi.getReviews());
    }

    public LiveData<Restaurant> getRestaurant() {
        return new MutableLiveData<>(restaurantApi.getRestaurant());
    }

    public LiveData<List<Review>> getReviews() {
        return reviewsLiveData;
    }

    //fonction du calcul du nombre total de reviews
    public int getTotalReviews() {
        List<Review> reviews = reviewsLiveData.getValue();
        return (reviews != null) ? reviews.size() : 0;
    }

    //fonction du calcul de la moyenne des notes
    public double getReviewsAverage() {
        List<Review> reviews = reviewsLiveData.getValue();
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Review review : reviews) {
            total += review.getRate();
        }

        return total / reviews.size();
    }


}


// Création de la function de récupération des avis.
 /*
 // ResaurantRepository.java :

 public MutableLiveData<List<Review>> getReviews() {
   ...
}
// création de la function de calcul de la note moyenne des reviews
 public double getReviewsAverage() {
    ...
}
// création de la function de calcul de nombre total de review
public int getTotalReviews() {
    ...
}

 public int[] getReviewsRepartition() {
   ..
}

public int addReview(@NotNull Review oReviewP) {
   ...
}

 public Review getUserReviewIfExist(String sUserName) {
        return restaurantApi.getUserReviewIfExist(sUserName);
    }
  */
