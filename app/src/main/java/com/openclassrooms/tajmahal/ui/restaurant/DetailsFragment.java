package com.openclassrooms.tajmahal.ui.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.databinding.FragmentDetailsBinding;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.ui.reviews.ReviewsFragment;

import dagger.hilt.android.AndroidEntryPoint;
/**
 * Fragment affichant les informations détaillées du restaurant ainsi que les statistiques
 * liées aux avis (moyenne, total, répartition des notes).
 *
 * Ce fragment :
 *
 *  - Utilise ViewBinding pour gérer la vue de manière sûre et sans findViewById.
 *  - Utilise Hilt (@AndroidEntryPoint) pour permettre l'injection de dépendances
 *    dans le ViewModel associé.
 *  - Observe plusieurs LiveData exposés par le DetailsViewModel afin de mettre à jour
 *    l'interface utilisateur de manière réactive (données du restaurant, moyenne des notes,
 *    total des avis, répartition des étoiles).
 *  - Configure l'affichage plein écran en rendant la barre de statut transparente.
 *  - Gère les interactions utilisateur : ouverture de Google Maps, du navigateur,
 *    du composeur téléphonique, et navigation vers le ReviewsFragment pour laisser un avis.
 *
 * Le fragment se contente d'afficher les données et de gérer les interactions UI.
 * Toute la logique métier (calculs, récupération des données, formatage) est déléguée
 * au ViewModel, conformément à l'architecture MVVM.
 *
 * DetailsFragment sert donc de couche de présentation, réactive et légère,
 * connectée au ViewModel pour garantir une UI toujours synchronisée avec les données.
 */
@AndroidEntryPoint
public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;
    private DetailsViewModel detailsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI();
        setupViewModel();

        detailsViewModel.getTajMahalRestaurant().observe(
                getViewLifecycleOwner(),
                this::updateUIWithRestaurant
        );

        detailsViewModel.getTotalReviews().observe(
                getViewLifecycleOwner(),
                total -> binding.restaurantTotalRating.setText(String.valueOf(total))
        );

        detailsViewModel.getAverageRating().observe(
                getViewLifecycleOwner(),
                avg -> {
                    binding.restaurantRatingBar.setRating(avg.floatValue());
                    binding.restaurantRate.setText(String.format("%.1f", avg));
                }
        );

        detailsViewModel.getReviewsRepartition().observe(
                getViewLifecycleOwner(),
                rep -> {
                    if (rep == null) return;
                    binding.progress5Stars.setProgress(rep[4]);
                    binding.progress4Stars.setProgress(rep[3]);
                    binding.progress3Stars.setProgress(rep[2]);
                    binding.progress2Stars.setProgress(rep[1]);
                    binding.progress1Stars.setProgress(rep[0]);
                }
        );
    }

    private void setupUI() {
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private void setupViewModel() {
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    private void updateUIWithRestaurant(Restaurant restaurant) {
        if (restaurant == null) return;

        binding.tvRestaurantName.setText(restaurant.getName());
        binding.tvRestaurantDay.setText(detailsViewModel.getCurrentDay(requireContext()));
        binding.tvRestaurantType.setText(
                String.format("%s %s", getString(R.string.restaurant), restaurant.getType())
        );
        binding.tvRestaurantHours.setText(restaurant.getHours());
        binding.tvRestaurantAddress.setText(restaurant.getAddress());
        binding.tvRestaurantWebsite.setText(restaurant.getWebsite());
        binding.tvRestaurantPhoneNumber.setText(restaurant.getPhoneNumber());
        binding.chipOnPremise.setVisibility(restaurant.isDineIn() ? View.VISIBLE : View.GONE);
        binding.chipTakeAway.setVisibility(restaurant.isTakeAway() ? View.VISIBLE : View.GONE);

        binding.buttonAdress.setOnClickListener(v -> openMap(restaurant.getAddress()));
        binding.buttonPhone.setOnClickListener(v -> dialPhoneNumber(restaurant.getPhoneNumber()));
        binding.buttonWebsite.setOnClickListener(v -> openBrowser(restaurant.getWebsite()));

        // Navigation vers ReviewsFragment
        binding.tvLeaveReview.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new ReviewsFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void openMap(String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(requireActivity(),
                    R.string.maps_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(requireActivity(),
                    R.string.phone_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void openBrowser(String websiteUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(requireActivity(),
                    R.string.no_browser_found, Toast.LENGTH_SHORT).show();
        }
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }
}
