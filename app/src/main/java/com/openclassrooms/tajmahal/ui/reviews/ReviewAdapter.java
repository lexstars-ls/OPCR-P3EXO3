package com.openclassrooms.tajmahal.ui.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.tajmahal.databinding.ReviewsBinding;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;
/**
 * Adapter du RecyclerView affichant la liste des avis (Review) du restaurant.
 *
 * Cette classe :
 *
 *  - Gère une liste interne de Review et met à jour l'affichage via submitList(),
 *    méthode simple permettant de remplacer entièrement la liste et de rafraîchir l'UI.
 *  - Utilise ViewBinding (ReviewsBinding) pour éviter les erreurs de type et simplifier
 *    l'accès aux vues dans chaque ViewHolder.
 *  - Charge les images de profil des utilisateurs via Glide, avec un placeholder par défaut.
 *  - Applique une logique d'affichage conditionnelle :
 *        → si le commentaire est vide ou null, le TextView est masqué
 *        → sinon, il est affiché avec le texte correspondant
 *  - Affiche également le nom de l'utilisateur et sa note via une RatingBar.
 *
 * Le ViewHolder encapsule toute la logique de binding afin de garder l'adapter simple
 * et lisible. L'adapter ne contient aucune logique métier : il se contente de transformer
 * les données en éléments visuels pour le RecyclerView.
 *
 * Cette classe respecte les bonnes pratiques Android :
 *  - séparation claire entre données et UI
 *  - utilisation de ViewBinding
 *  - gestion efficace des vues via RecyclerView
 *  - chargement optimisé des images avec Glide
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

        // function pour la gestion des élements a stocker avant upload
        public void bind(Review review) {

            // Nom
            binding.reviewName.setText(review.getUsername());

            // Note
            binding.reviewBar.setRating(review.getRate());

            // Commentaire
            String comment = review.getComment();

            if (comment == null || comment.trim().isEmpty()) {
                binding.reviewText.setVisibility(View.GONE);
            } else {
                binding.reviewText.setVisibility(View.VISIBLE);
                binding.reviewText.setText(comment);
            }

            // Image
            Glide.with(binding.getRoot().getContext())
                    .load(review.getPicture())
                    .placeholder(com.openclassrooms.tajmahal.R.drawable.ic_launcher)
                    .into(binding.reviewPicture);
        }
    }
}
