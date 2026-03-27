package com.openclassrooms.tajmahal.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel utilisé pour gérer toutes les fonctionnalités liées aux avis (reviews).
 * Il sert d'intermédiaire entre l'interface utilisateur et la couche Repository/FakeAPI.
 *
 * Rôle :
 * - Récupère la liste des avis depuis le Repository
 * - Ajoute un nouvel avis
 * - Vérifie si un utilisateur a déjà laissé un avis
 * - Fournit les statistiques : moyenne, total, répartition
 *
 * Le but est de centraliser la logique métier des avis et de fournir des LiveData
 * observables par les Fragments.
 */

@HiltViewModel
public class ReviewsViewModel extends ViewModel {

    private final RestaurantRepository repository;

    @Inject
    public ReviewsViewModel(RestaurantRepository repository) {
        this.repository = repository;
    }

    // LiveData des reviews
    public LiveData<List<Review>> getReviews() {
        return repository.getReviews();
    }

    // Ajouter une review
    public void addReview(Review review) {
        repository.addReview(review);
    }

    // Nombre total
    public int getTotalReviews() {
        return repository.getTotalReviews();
    }

    // Moyenne
    public double getAverageRating() {
        return repository.getReviewsAverage();
    }

    // Répartition
    public int[] getRepartition() {
        return repository.getReviewsRepartition();
    }
}
