package fr.mines.philidor.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        boolean blancs = getIntent().getBooleanExtra(MainActivity.EXTRA_BLANCS, true);
        float creativite = getIntent().getFloatExtra(MainActivity.EXTRA_CREATIVITE, 0f);
        String couleur = getString(blancs ? R.string.blancs : R.string.noirs);
        binding.choix.setText(getString(R.string.choix_recus, couleur, creativite));
    }
}
