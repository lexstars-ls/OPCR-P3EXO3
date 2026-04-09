package com.openclassrooms.tajmahal.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
/**
 * ViewModel dédié à la gestion des avis (Review) dans l'interface utilisateur.
 *
 * Ce ViewModel ne contient aucune logique métier : son rôle est uniquement de servir
 * d'intermédiaire entre la couche UI (ReviewsFragment) et le RestaurantRepository,
 * qui centralise toute la logique métier (validation des avis, calculs, mise à jour
 * de la liste, règles de cohérence).
 *
 * Le ViewModel :
 *  - Expose le LiveData contenant la liste des reviews afin que la UI puisse
 *    s'abonner et se mettre à jour automatiquement.
 *  - Fournit des méthodes simples permettant d'ajouter un avis ou de récupérer
 *    des informations dérivées (total, moyenne, répartition), en déléguant
 *    entièrement le travail au Repository.
 *  - Utilise Hilt (@HiltViewModel) pour recevoir automatiquement le Repository.
 *
 * Cette classe respecte pleinement l'architecture MVVM :
 *      UI (Fragment) → ViewModel → Repository (logique métier)
 *
 * Le ViewModel se concentre donc sur l'exposition des données et la communication
 * avec le Repository, garantissant une séparation claire des responsabilités
 * et une application plus testable et maintenable.
 */

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
