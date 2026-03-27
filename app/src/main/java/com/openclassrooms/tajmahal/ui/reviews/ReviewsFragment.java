package com.openclassrooms.tajmahal.ui.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.tajmahal.databinding.FragmentReviewsBinding;
import com.openclassrooms.tajmahal.domain.model.Review;

import dagger.hilt.android.AndroidEntryPoint;
/**
 * Fragment chargé d'afficher la liste des avis (reviews) et de gérer les interactions
 * liées à l'ajout ou la mise à jour d'un avis utilisateur.
 *
 * Rôle :
 * - Observe les LiveData du ReviewsViewModel (liste des avis, statistiques, etc.)
 * - Met à jour l'interface utilisateur en fonction des données reçues
 * - Initialise et configure le RecyclerView et son adapter
 * - Gère les actions utilisateur (validation d'un avis, saisie du texte, notation)
 *
 * Ce Fragment représente la couche UI de la fonctionnalité "Avis" dans l'architecture MVVM.
 */

@AndroidEntryPoint
public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    private ReviewsViewModel viewModel;
    private ReviewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);

        // Adapter
        adapter = new ReviewAdapter();
        binding.reviewsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.reviewsList.setAdapter(adapter);

        // Observer les reviews
        viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            adapter.submitList(reviews);
        });

        //activer ta Toolbar comme ActionBar
        // Sans ça, la flèche n'est PAS cliquable
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.userToolBar);

       // clique retour
        binding.userToolBar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Bouton valider
        binding.userValidate.setOnClickListener(v -> {
            String comment = binding.userReviewText.getText().toString().trim();
            int rate = (int) binding.userRatingBar.getRating();

            if (comment.isEmpty() || rate == 0) return;

            Review newReview = new Review(
                    "Utilisateur",
                    "",
                    comment,
                    rate
            );

            viewModel.addReview(newReview);

            // Reset UI
            binding.userReviewText.setText("");
            binding.userRatingBar.setRating(0);
        });
    }
}

