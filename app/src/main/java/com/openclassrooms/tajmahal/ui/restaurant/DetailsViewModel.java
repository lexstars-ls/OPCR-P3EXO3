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
/**
 * ViewModel responsable de fournir à l'interface utilisateur toutes les informations
 * nécessaires à l'affichage des détails du restaurant ainsi que des statistiques
 * liées aux avis (moyenne, total, répartition des notes).
 *
 * Ce ViewModel :
 *
 *  - Utilise Hilt (@HiltViewModel) pour recevoir automatiquement le RestaurantRepository.
 *  - Observe en continu la liste des reviews exposée par le repository afin de mettre
 *    à jour plusieurs LiveData dérivés :
 *        → totalReviews : nombre total d'avis
 *        → averageRating : moyenne des notes
 *        → reviewsRepartition : répartition des notes de 1★ à 5★
 *  - Fournit également les informations du restaurant via getTajMahalRestaurant().
 *  - Expose une méthode utilitaire getCurrentDay() permettant d'afficher le jour
 *    actuel dans la langue de l'utilisateur.
 *
 * Toute la logique métier (calculs, validation, récupération des données) est déléguée
 * au RestaurantRepository. Le ViewModel se contente de transformer ces données en
 * informations prêtes à être affichées par le fragment, conformément à l'architecture MVVM.
 *
 * Grâce à l'utilisation de LiveData, l'interface utilisateur reste automatiquement
 * synchronisée avec les données, sans gestion manuelle du cycle de vie.
 */

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