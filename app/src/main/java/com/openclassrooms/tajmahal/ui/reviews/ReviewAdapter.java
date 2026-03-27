package com.openclassrooms.tajmahal.ui.reviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.tajmahal.databinding.ReviewsBinding;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;
/**
 * Adapter du RecyclerView affichant la liste des avis (reviews).
 *
 * Rôle :
 * - Convertir chaque objet Review en un item visuel affiché dans la liste
 * - Gérer l'inflation du layout de chaque item
 * - Lier les données (nom, note, commentaire, photo) aux vues correspondantes
 * - Optimiser les performances grâce à DiffUtil pour ne mettre à jour que les éléments modifiés
 *
 * Cet adapter fait le lien entre les données fournies par le ViewModel et l'affichage
 * dans le RecyclerView.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviews = new ArrayList<>();

    public void submitList(List<Review> newReviews) {
        reviews.clear();
        reviews.addAll(newReviews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewsBinding binding = ReviewsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final ReviewsBinding binding;

        public ReviewViewHolder(@NonNull ReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Review review) {

            // Nom
            binding.reviewName.setText(review.getUsername());

            // Note
            binding.reviewBar.setRating(review.getRate());

            // Texte
            binding.reviewText.setText(review.getComment());

            // Image (si tu veux utiliser Glide)
            Glide.with(binding.getRoot().getContext())
                    .load(review.getPicture())
                    .placeholder(com.openclassrooms.tajmahal.R.drawable.ic_launcher)
                    .into(binding.reviewPicture);
        }
    }
}
