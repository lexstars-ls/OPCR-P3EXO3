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
    public void addReview_empty_isAdded() {
        // GIVEN
        Review newReview = new Review(
                "",
                "",
                "",
                0
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
    public void addReview_zeroRate_isAdded() {
        // GIVEN
        Review newReview = new Review(
                "TestUser",
                "https://example.com/pic.jpg",
                "Pas terrible",
                0
        );

        int oldSize = repository.getTotalReviews();

        // WHEN
        repository.addReview(newReview);

        // THEN
        List<Review> updated = repository.getReviews().getValue();

        assertNotNull(updated);
        assertEquals(oldSize + 1, updated.size());
        assertEquals(0, updated.get(0).getRate());
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
