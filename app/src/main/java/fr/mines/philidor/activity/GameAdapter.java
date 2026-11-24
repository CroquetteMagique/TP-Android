package fr.mines.philidor.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.mines.philidor.R;
import fr.mines.philidor.databinding.ItemGameBinding;

public class GameAdapter extends ArrayAdapter<PlayedGame> {

    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

    public GameAdapter(Context context, ArrayList<PlayedGame> games) {
        super(context, 0, games);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ItemGameBinding item;
        if (convertView == null) {
            item = ItemGameBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = item.getRoot();
            convertView.setTag(item);
        } else {
            item = (ItemGameBinding) convertView.getTag();
        }

        PlayedGame game = getItem(position);
        Context c = getContext();

        int letter;
        int result;
        int badge;
        if (PlayedGame.WIN.equals(game.getResult())) {
            letter = R.string.lettre_victoire;
            result = R.string.victoire;
            badge = R.drawable.victory_round;
        } else if (PlayedGame.LOSS.equals(game.getResult())) {
            letter = R.string.lettre_defaite;
            result = R.string.defaite;
            badge = R.drawable.defeat_round;
        } else {
            letter = R.string.lettre_nulle;
            result = R.string.nulle;
            badge = R.drawable.nul_round;
        }

        int ending;
        if (PlayedGame.CHECKMATE.equals(game.getEnding())) {
            ending = R.string.fin_mat;
        } else if (PlayedGame.STALEMATE.equals(game.getEnding())) {
            ending = R.string.fin_pat;
        } else {
            ending = R.string.fin_abandon;
        }

        String color = c.getString(game.isWhite() ? R.string.blancs : R.string.noirs);
        String date = dateFormat.format(new Date(game.getDate()));
        String moves = c.getResources().getQuantityString(R.plurals.nb_coups,
                game.getMoveCount(), game.getMoveCount());

        item.badge.setText(letter);
        item.badge.setBackgroundResource(badge);
        item.title.setText(c.getString(R.string.ligne_titre, c.getString(result), color));
        item.details.setText(c.getString(R.string.ligne_detail, date, moves, c.getString(ending)));
        item.king.setText(game.isWhite() ? R.string.roi_blanc : R.string.roi_noir);

        return convertView;
    }
}