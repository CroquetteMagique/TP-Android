package fr.mines.philidor.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import fr.mines.philidor.R;
import fr.mines.philidor.moteur.Partie;

public class ChessboardView extends View {

    public interface OnSquareTouchListener {
        void onSquareTouched(int column, int row);
    }

    private static final String PIECES = "KQRBNPkqrbnp";

    private final Paint lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint capturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint piecePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final String[] symbols;

    private Partie game;
    private boolean flipped;
    private int selectedColumn = -1;
    private int selectedRow = -1;
    private List<String> targets = new ArrayList<>();
    private OnSquareTouchListener listener;
    private float squareSize;

    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        lightPaint.setColor(ContextCompat.getColor(context, R.color.case_claire));
        darkPaint.setColor(ContextCompat.getColor(context, R.color.case_foncee));
        highlightPaint.setColor(ContextCompat.getColor(context, R.color.case_surlignee));
        targetPaint.setColor(ContextCompat.getColor(context, R.color.cible));
        capturePaint.setColor(ContextCompat.getColor(context, R.color.cible));
        capturePaint.setStyle(Paint.Style.STROKE);
        piecePaint.setColor(ContextCompat.getColor(context, R.color.piece));
        piecePaint.setTextAlign(Paint.Align.CENTER);
        symbols = context.getResources().getStringArray(R.array.pieces);
    }

    public static String square(int column, int row) {
        return "" + (char) ('a' + column) + (char) ('1' + row);
    }

    public void setGame(Partie game, boolean flipped, int selectedColumn, int selectedRow, List<String> targets) {
        this.game = game;
        this.flipped = flipped;
        this.selectedColumn = selectedColumn;
        this.selectedRow = selectedRow;
        this.targets = targets;
        invalidate();
    }

    public void setOnSquareTouchListener(OnSquareTouchListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        squareSize = w / 8f;
        piecePaint.setTextSize(squareSize * 0.8f);
        capturePaint.setStrokeWidth(squareSize * 0.08f);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        String last = game == null ? null : game.dernierCoup();
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                float left = (flipped ? 7 - column : column) * squareSize;
                float top = (flipped ? row : 7 - row) * squareSize;
                float right = left + squareSize;
                float bottom = top + squareSize;
                canvas.drawRect(left, top, right, bottom, (column + row) % 2 == 0 ? darkPaint : lightPaint);

                if (isLastMove(last, column, row) || (column == selectedColumn && row == selectedRow)) {
                    canvas.drawRect(left, top, right, bottom, highlightPaint);
                }
                if (game == null) {
                    continue;
                }

                float centerX = left + squareSize / 2;
                float centerY = top + squareSize / 2;
                char piece = game.piece(column, row);
                if (piece != ' ') {
                    float baseline = centerY - (piecePaint.descent() + piecePaint.ascent()) / 2;
                    canvas.drawText(symbols[PIECES.indexOf(piece)], centerX, baseline, piecePaint);
                }
                if (targets.contains(square(column, row))) {
                    if (piece == ' ') {
                        canvas.drawCircle(centerX, centerY, squareSize * 0.15f, targetPaint);
                    } else {
                        canvas.drawCircle(centerX, centerY, squareSize * 0.45f, capturePaint);
                    }
                }
            }
        }
    }

    private boolean isLastMove(String move, int column, int row) {
        if (move == null) {
            return false;
        }
        boolean from = move.charAt(0) - 'a' == column && move.charAt(1) - '1' == row;
        boolean to = move.charAt(2) - 'a' == column && move.charAt(3) - '1' == row;
        return from || to;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int screenColumn = (int) (event.getX() / squareSize);
                int screenRow = (int) (event.getY() / squareSize);
                if (listener != null && screenColumn >= 0 && screenColumn < 8 && screenRow >= 0 && screenRow < 8) {
                    int column = flipped ? 7 - screenColumn : screenColumn;
                    int row = flipped ? screenRow : 7 - screenRow;
                    listener.onSquareTouched(column, row);
                }
                performClick();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
