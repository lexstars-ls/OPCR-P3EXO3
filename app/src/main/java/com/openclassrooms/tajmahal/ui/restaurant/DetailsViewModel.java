package com.openclassrooms.tajmahal.ui.restaurant;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Restaurant;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailsViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Double> averageRating = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalReviews = new MutableLiveData<>();
    private final MutableLiveData<int[]> reviewsRepartition = new MutableLiveData<>();
    public LiveData<Double> getAverageRating() {
        return averageRating;
    }
    public LiveData<int[]> getReviewsRepartition() {
        return reviewsRepartition;
    }
    public LiveData<Integer> getTotalReviews() {
        return totalReviews;
    }

    @Inject
    public DetailsViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;

        // Observe reviews and update LiveData
        restaurantRepository.getReviews().observeForever(reviews -> {
            if (reviews != null) {

                // Total des reviews
                totalReviews.setValue(restaurantRepository.getTotalReviews());

                // Moyenne des notes
                averageRating.setValue(restaurantRepository.getReviewsAverage());

                // Répartition des notes (1★ → 5★)
                reviewsRepartition.setValue(restaurantRepository.getReviewsRepartition());
            }
        });
    }


    public LiveData<Restaurant> getTajMahalRestaurant() {
        return restaurantRepository.getRestaurant();
    }

    public String getCurrentDay(Context context) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString;

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                dayString = context.getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                dayString = context.getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                dayString = context.getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                dayString = context.getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                dayString = context.getString(R.string.friday);
                break;
            case Calendar.SATURDAY:
                dayString = context.getString(R.string.saturday);
                break;
            case Calendar.SUNDAY:
                dayString = context.getString(R.string.sunday);
                break;
            default:
                dayString = "";
        }
        return dayString;
    }

}