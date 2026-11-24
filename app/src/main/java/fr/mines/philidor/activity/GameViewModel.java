package fr.mines.philidor.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.mines.philidor.moteur.Partie;

public class GameViewModel extends ViewModel {

    private final Partie game = new Partie();
    private final MutableLiveData<Partie> state = new MutableLiveData<>(game);
    private final ArrayList<String> targets = new ArrayList<>();
    private int selectedColumn = -1;
    private int selectedRow = -1;

    public LiveData<Partie> getState() { return state; }
    public int getSelectedColumn() { return selectedColumn; }
    public int getSelectedRow() { return selectedRow; }
    public List<String> getTargets() { return targets; }

    public void touch(int column, int row) {
        if (game.etat() != Partie.Etat.EN_COURS) {
            return;
        }
        String square = ChessboardView.square(column, row);
        if (selectedColumn >= 0 && targets.contains(square)) {
            String move = ChessboardView.square(selectedColumn, selectedRow) + square;
            if (!game.jouer(move)) {
                game.jouer(move + "q");
            }
            clearSelection();
        } else if (canMove(game.piece(column, row))) {
            selectedColumn = column;
            selectedRow = row;
            targets.clear();
            for (String uci : game.coupsDepuis(column, row)) {
                targets.add(uci.substring(2, 4));
            }
        } else {
            clearSelection();
        }
        state.setValue(game);
    }

    public void undo() {
        game.annuler();
        clearSelection();
        state.setValue(game);
    }

    private boolean canMove(char piece) {
        return piece != ' ' && Character.isUpperCase(piece) == game.traitAuxBlancs();
    }

    private void clearSelection() {
        selectedColumn = -1;
        selectedRow = -1;
        targets.clear();
    }
}
