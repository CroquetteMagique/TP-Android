package fr.mines.philidor;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import fr.mines.philidor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Valeur de la créativité (0,6), mise à jour quand le curseur bouge
        afficherCreativite(binding.creativite.getValue());
        binding.creativite.addOnChangeListener((slider, valeur, parUtilisateur) ->
                afficherCreativite(valeur));

        marquerCouleur(binding.couleur.getCheckedButtonId());
        binding.couleur.addOnButtonCheckedListener((groupe, idBouton, coche) -> {
            if (!coche) return; // appel pour le bouton décoché : ignoré
            marquerCouleur(idBouton);
        });
    }

    private void afficherCreativite(float valeur) {
        binding.valeur.setText(getString(R.string.valeur_creativite, valeur));
    }

    private void marquerCouleur(int idCoche) {
        MaterialButton[] boutons = { binding.blancs, binding.noirs, binding.hasard };
        for (MaterialButton b : boutons) {
            boolean choisi = b.getId() == idCoche;
            if (choisi) {
                b.setIconResource(R.drawable.ic_coche);
            } else {
                b.setIcon(null);
            }
            b.setTypeface(null, choisi ? Typeface.BOLD : Typeface.NORMAL);
        }
    }
}