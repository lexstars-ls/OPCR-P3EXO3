package com.openclassrooms.tajmahal;

import com.openclassrooms.tajmahal.data.service.RestaurantApi;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;
/**
 * Implémentation factice de l'interface RestaurantApi utilisée exclusivement pour les tests unitaires.
 *
 * Cette Fake API permet de simuler une source de données sans effectuer de requêtes réseau
 * ni dépendre d'un backend réel. Elle fournit :
 *
 *  - Une liste de reviews pré-remplies, cohérente avec les données attendues par l'application.
 *  - Un Restaurant générique utilisé uniquement pour satisfaire les appels de l'interface.
 *
 * L'objectif est d'offrir un environnement de test totalement contrôlé, stable et déterministe.
 * Cela permet de tester le RestaurantRepository et la logique métier associée sans effets de bord,
 * sans latence réseau et sans dépendance externe.
 *
 * En isolant ainsi la couche de données, les tests peuvent se concentrer uniquement sur :
 *  - La validation des règles métier (ex : review invalide rejetée)
 *  - La mise à jour correcte du LiveData
 *  - L'ordre d'insertion des reviews
 *
 * Cette classe est volontairement simple : elle ne contient aucune logique complexe et ne fait
 * qu'exposer des données statiques afin de garantir la reproductibilité des tests.
 */

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