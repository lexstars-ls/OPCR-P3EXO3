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
 * Fragment permettant d'afficher la liste des avis du restaurant et d'ajouter
 * un nouvel avis via un formulaire simple (note + commentaire).
 *
 * Ce fragment :
 *
 *  - Utilise ViewBinding pour gérer la vue de manière sûre et sans findViewById.
 *  - Utilise Hilt (@AndroidEntryPoint) pour permettre l'injection du ReviewsViewModel.
 *  - Configure un RecyclerView avec un ReviewAdapter pour afficher dynamiquement
 *    la liste des avis existants.
 *  - Observe le LiveData des reviews exposé par le ViewModel afin de mettre à jour
 *    automatiquement la liste lorsqu'un nouvel avis est ajouté.
 *  - Gère la barre d’outils (Toolbar) avec un bouton de retour vers l’écran précédent.
 *  - Permet à l’utilisateur de saisir un commentaire et une note, puis d’ajouter un avis.
 *
 * La validation et la logique métier (par exemple : refuser une review sans note)
 * ne sont **pas gérées dans le fragment**, mais entièrement déléguées au Repository
 * via le ViewModel, conformément à l’architecture MVVM :
 *
 *      UI (Fragment) → ViewModel → Repository (logique métier)
 *
 * Le fragment se concentre donc uniquement sur :
 *  - la gestion de l’interface utilisateur,
 *  - la récupération des entrées utilisateur,
 *  - les interactions (clics, navigation),
 *  - l’observation des données.
 *
 * Toute la logique métier (validation du rate, ajout ou rejet, mise à jour des données)
 * est centralisée dans le Repository, garantissant une architecture propre,
 * testable et maintenable.
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

        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);

        adapter = new ReviewAdapter();
        binding.reviewsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.reviewsList.setAdapter(adapter);

        viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            adapter.submitList(reviews);
        });

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.userToolBar);

        binding.userToolBar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        binding.userValidate.setOnClickListener(v -> {
            String comment = binding.userReviewText.getText().toString().trim();
            int rate = (int) binding.userRatingBar.getRating();

            // Nettoyage du commentaire : jamais de hint dans la liste
            if (comment.isEmpty()) {
                comment = "";
            }

            Review newReview = new Review(
                    "Utilisateur",
                    "",
                    comment,
                    rate
            );

            boolean success = viewModel.addReview(newReview);

            if (success) {
                binding.userReviewText.setText("");
                binding.userRatingBar.setRating(0);
            } else {
                Toast.makeText(requireContext(), "Veuillez choisir une note", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
