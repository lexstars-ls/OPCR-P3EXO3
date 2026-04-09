package com.openclassrooms.tajmahal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Review;
import com.openclassrooms.tajmahal.FakeRestaurantApi;


import java.util.List;
/**
 * Classe de tests unitaires dédiée à la vérification du comportement du RestaurantRepository.
 *
 * Ces tests ont pour objectif de valider la logique métier appliquée lors de l'ajout
 * d'une nouvelle Review dans le repository. Ils permettent de s'assurer que :
 *
 *  - Une review valide est correctement ajoutée en tête de liste.
 *  - Les champs optionnels (comme un commentaire vide) n'empêchent pas l'ajout.
 *  - Les reviews invalides (par exemple un rate égal à 0 ou des champs vides)
 *    sont correctement rejetées et ne modifient pas la liste existante.
 *  - Le LiveData exposé par le repository est bien mis à jour après un ajout réussi.
 *
 * L'utilisation de Robolectric permet d'exécuter ces tests sans environnement Android réel,
 * tandis que InstantTaskExecutorRule garantit l'exécution synchrone du LiveData.
 *
 * FakeRestaurantApi est utilisée pour isoler le repository de toute dépendance réseau
 * et assurer un environnement de test totalement contrôlé.
 */

@RunWith(RobolectricTestRunner.class)
public class RestaurantRepositoryTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private RestaurantRepository repository;
    private FakeRestaurantApi fakeApi;

    @Before
    public void setup() {
        fakeApi = new FakeRestaurantApi();
        repository = new RestaurantRepository(fakeApi);
    }

    @Test
    public void addReview_validReview_isAddedOnTop() {
        // GIVEN
        Review newReview = new Review(
                "TestUser",
                "https://example.com/pic.jpg",
                "Super resto",
                5
        );

        int oldSize = repository.getTotalReviews();

        // WHEN
        repository.addReview(newReview);

        // THEN
        List<Review> updated = repository.getReviews().getValue();

        assertNotNull(updated);
        assertEquals(oldSize + 1, updated.size());
        assertEquals("TestUser", updated.get(0).getUsername());
    }

    @Test
    public void addReview_emptyComment_isAdded() {
        // GIVEN
        Review newReview = new Review(
                "TestUser",
                "https://example.com/pic.jpg",
                "",
                4
        );

        int oldSize = repository.getTotalReviews();

        // WHEN
        repository.addReview(newReview);

        // THEN
        List<Review> updated = repository.getReviews().getValue();

        assertNotNull(updated);
        assertEquals(oldSize + 1, updated.size());
        assertEquals("", updated.get(0).getComment());
    }

    @Test
    public void addReview_empty_isRejected() {
        // GIVEN
        Review newReview = new Review(
                "",
                "",
                "",
                0
        );

        int oldSize = repository.getTotalReviews();

        // WHEN
        boolean result = repository.addReview(newReview);

        // THEN
        List<Review> updated = repository.getReviews().getValue();

        assertNotNull(updated);
        assertFalse(result); // l'ajout doit échouer
        assertEquals(oldSize, updated.size()); // rien n'a été ajouté
    }


    @Test
    public void addReview_zeroRate_isRejected() {
        // GIVEN
        Review newReview = new Review(
                "TestUser",
                "https://example.com/pic.jpg",
                "Pas terrible",
                0
        );

        int oldSize = repository.getTotalReviews();

        // WHEN
        boolean result = repository.addReview(newReview);

        // THEN
        List<Review> updated = repository.getReviews().getValue();

        assertNotNull(updated);
        assertEquals(oldSize, updated.size()); // la taille ne doit PAS changer
        assertFalse(result); // la méthode doit indiquer que l'ajout a échoué
    }


    @Test
    public void addReview_updatesLiveData() {
        // GIVEN
        Review newReview = new Review(
                "User",
                "pic",
                "Commentaire",
                5
        );

        // WHEN
        repository.addReview(newReview);

        // THEN
        assertNotNull(repository.getReviews().getValue());
        assertTrue(repository.getReviews().getValue().size() > 0);
    }
}
