package fr.mines.philidor.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Random;

public class HistoryViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<PlayedGame>> games = new MutableLiveData<>();

    public HistoryViewModel() {
        games.setValue(generate(100));
    }

    public LiveData<ArrayList<PlayedGame>> getGames() {
        return games;
    }

    public void delete(int position) {
        ArrayList<PlayedGame> list = games.getValue();
        if (list == null || position < 0 || position >= list.size()) {
            return;
        }
        list.remove(position);
        games.setValue(list);
    }

    private ArrayList<PlayedGame> generate(int count) {
        ArrayList<PlayedGame> list = new ArrayList<>();
        Random random = new Random();
        String[] results = { PlayedGame.WIN, PlayedGame.LOSS, PlayedGame.DRAW };
        long now = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String result = results[random.nextInt(results.length)];
            String ending;
            if (PlayedGame.DRAW.equals(result)) {
                ending = PlayedGame.STALEMATE;
            } else if (PlayedGame.LOSS.equals(result) && random.nextBoolean()) {
                ending = PlayedGame.RESIGNATION;
            } else {
                ending = PlayedGame.CHECKMATE;
            }
            long date = now - i * 43_200_000L;
            list.add(new PlayedGame(date, random.nextBoolean(), result, ending, 10 + random.nextInt(60)));
        }
        return list;
    }
}
