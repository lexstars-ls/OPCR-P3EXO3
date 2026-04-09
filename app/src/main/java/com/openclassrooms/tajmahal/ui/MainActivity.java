package com.openclassrooms.tajmahal.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.databinding.ActivityMainBinding;
import com.openclassrooms.tajmahal.ui.restaurant.DetailsFragment;

import dagger.hilt.android.AndroidEntryPoint;
/**
 * Activité principale de l'application, responsable de l'initialisation de l'interface
 * et du chargement du fragment de détails du restaurant.
 *
 * Cette activité utilise :
 *
 *  - ViewBinding pour gérer l'inflation de la vue de manière sûre et sans findViewById.
 *  - Hilt (@AndroidEntryPoint) pour permettre l'injection de dépendances dans les fragments.
 *  - Le FragmentManager pour afficher le DetailsFragment au lancement de l'application.
 *
 * Le fragment est chargé uniquement lorsque savedInstanceState est null, afin d'éviter
 * de recréer le fragment lors des rotations d'écran ou des recréations automatiques
 * de l'activité par Android.
 *
 * MainActivity sert donc de conteneur principal et de point d'entrée de l'application,
 * tandis que la logique métier et l'affichage détaillé sont délégués aux fragments.
 */

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance())
                    .commitNow();
        }
    }

}