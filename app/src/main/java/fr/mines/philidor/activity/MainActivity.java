package fr.mines.philidor.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Random;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_BLANCS = "fr.mines.philidor.BLANCS";
    public static final String EXTRA_CREATIVITE = "fr.mines.philidor.CREATIVITE";

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

        afficherCreativite(binding.creativite.getValue());
        binding.creativite.addOnChangeListener((slider, valeur, parUtilisateur) ->
                afficherCreativite(valeur));

        marquerCouleur(binding.couleur.getCheckedButtonId());
        binding.couleur.addOnButtonCheckedListener((groupe, idBouton, coche) -> {
            if (!coche) return;
            marquerCouleur(idBouton);
        });

        binding.nouvellePartie.setOnClickListener(v -> lancerPartie());
        binding.historique.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void lancerPartie() {
        int id = binding.couleur.getCheckedButtonId();
        boolean blancs;
        if (id == R.id.blancs) {
            blancs = true;
        } else if (id == R.id.noirs) {
            blancs = false;
        } else {
            blancs = new Random().nextBoolean();
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_BLANCS, blancs);
        intent.putExtra(EXTRA_CREATIVITE, binding.creativite.getValue());
        startActivity(intent);
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