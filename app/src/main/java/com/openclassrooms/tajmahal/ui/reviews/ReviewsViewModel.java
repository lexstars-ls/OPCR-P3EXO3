package com.openclassrooms.tajmahal.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewsViewModel extends ViewModel {

    private final RestaurantRepository repository;

    @Inject
    public ReviewsViewModel(RestaurantRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Review>> getReviews() {
        return repository.getReviews();
    }

    public boolean addReview(Review review) {
        return repository.addReview(review);
    }

    public int getTotalReviews() {
        return repository.getTotalReviews();
    }

    public double getAverageRating() {
        return repository.getReviewsAverage();
    }

    public int[] getRepartition() {
        return repository.getReviewsRepartition();
    }
}
