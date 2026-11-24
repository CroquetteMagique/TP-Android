package fr.mines.philidor.activity;

public class PlayedGame {

    public static final String WIN = "win";
    public static final String LOSS = "loss";
    public static final String DRAW = "draw";

    public static final String CHECKMATE = "checkmate";
    public static final String STALEMATE = "stalemate";
    public static final String RESIGNATION = "resignation";

    private final long date;
    private final boolean white;
    private final String result;
    private final String ending;
    private final int moveCount;

    public PlayedGame(long date, boolean white, String result, String ending, int moveCount) {
        this.date = date;
        this.white = white;
        this.result = result;
        this.ending = ending;
        this.moveCount = moveCount;
    }

    public long getDate() { return date; }
    public boolean isWhite() { return white; }
    public String getResult() { return result; }
    public String getEnding() { return ending; }
    public int getMoveCount() { return moveCount; }
}