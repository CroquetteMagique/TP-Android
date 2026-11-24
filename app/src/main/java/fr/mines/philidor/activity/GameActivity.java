package fr.mines.philidor.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ActivityGameBinding;
import fr.mines.philidor.moteur.Partie;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private GameViewModel viewModel;
    private boolean white;

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

        white = getIntent().getBooleanExtra(MainActivity.EXTRA_BLANCS, true);
        binding.topBanner.setText(white ? R.string.noirs : R.string.blancs);
        binding.bottomBanner.setText(white ? R.string.blancs : R.string.noirs);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        binding.board.setOnSquareTouchListener((column, row) -> viewModel.touch(column, row));
        binding.undo.setOnClickListener(v -> viewModel.undo());
        viewModel.getState().observe(this, this::show);
    }

    private void show(Partie game) {
        binding.board.setGame(game, !white, viewModel.getSelectedColumn(),
                viewModel.getSelectedRow(), viewModel.getTargets());
        binding.status.setText(status(game));
        binding.moves.setText(numberedMoves(game.coupsSan()));
        binding.undo.setEnabled(!game.coups().isEmpty());
    }

    private String numberedMoves(List<String> moves) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                text.append(getString(R.string.numero_coup, i / 2 + 1)).append(' ');
            }
            text.append(moves.get(i)).append(' ');
        }
        return text.toString().trim();
    }
    private String status(Partie game) {
        Partie.Etat state = game.etat();
        if (state == Partie.Etat.MAT) {
            return getString(game.traitAuxBlancs() ? R.string.mat_noirs : R.string.mat_blancs);
        } else if (state == Partie.Etat.PAT) {
            return getString(R.string.pat);
        } else if (state != Partie.Etat.EN_COURS) {
            return getString(R.string.partie_nulle);
        }
        String turn = getString(game.traitAuxBlancs() ? R.string.trait_blancs : R.string.trait_noirs);
        return game.enEchec() ? getString(R.string.echec, turn) : turn;
    }
}
