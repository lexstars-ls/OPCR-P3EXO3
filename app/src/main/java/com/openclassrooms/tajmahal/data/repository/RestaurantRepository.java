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
/**
 * Repository centralisant l'accès aux données du restaurant et de ses reviews.
 *
 * Cette classe joue le rôle d'intermédiaire entre la couche de données (RestaurantApi)
 * et la couche de présentation (ViewModel / UI). Elle encapsule :
 *
 *  - La récupération du restaurant et des reviews depuis l'API.
 *  - La gestion d'un LiveData contenant la liste des reviews, afin de permettre
 *    une mise à jour réactive de l'interface utilisateur.
 *  - Une logique métier simple, notamment la validation d'une review avant son ajout.
 *
 * Le constructeur initialise le LiveData avec une copie de la liste fournie par l'API,
 * afin d'éviter toute modification involontaire de la source d'origine.
 *
 * Les principales responsabilités du repository sont :
 *
 *  - Fournir les données du restaurant sous forme de LiveData.
 *  - Exposer la liste des reviews et permettre son observation.
 *  - Calculer des informations dérivées (nombre total, moyenne, répartition des notes).
 *  - Ajouter une nouvelle review en respectant les règles métier :
 *        → une review avec un rate égal à 0 est rejetée.
 *        → une review valide est ajoutée en tête de liste.
 *
 * Le repository est annoté @Singleton pour garantir une instance unique dans l'application,
 * et utilise l'injection de dépendances via Hilt (@Inject) pour recevoir l'API.
 *
 * Cette classe assure donc une séparation claire des responsabilités :
 * la logique métier et la gestion des données sont centralisées ici,
 * tandis que la UI reste simple et réactive grâce au LiveData.
 */

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
