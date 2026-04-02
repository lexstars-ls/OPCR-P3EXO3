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
