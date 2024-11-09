package com.example.week5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";
    boolean player1Turn = true;
    byte[][] board = new byte[3][3];
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        table = findViewById(R.id.board);

        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                button.setOnClickListener(new Cellistener(i, j));
            }
        }
    }

    class Cellistener implements View.OnClickListener {
        int row, col;

        public Cellistener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (!isValidMove(row, col)) {
                Toast.makeText(MainActivity.this, "Cell is already occupied", Toast.LENGTH_LONG).show();
                return;
            }
            if (player1Turn) {
                ((Button) v).setText(PLAYER_1);
                board[row][col] = 1;
            } else {
                ((Button) v).setText(PLAYER_2);
                board[row][col] = 2;
            }

            int result = gameEnded(row, col);
            if (result == -1) {
                player1Turn = !player1Turn;
            } else if (result == 0) {
                Toast.makeText(MainActivity.this, "It's a Draw", Toast.LENGTH_LONG).show();
            } else if (result == 1) {
                Toast.makeText(MainActivity.this, "Player 1 wins", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Player 2 wins", Toast.LENGTH_LONG).show();
            }
        }

        public int gameEnded(int row, int col) {
            int symbol = board[row][col];
            boolean win = true;


            for (int i = 0; i < 3; i++) {
                if (board[i][col] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return symbol;



            return -1; // Game will continue
        }
    }

    public boolean isValidMove(int row, int col) {
        return board[row][col] == 0;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("player1Turn", player1Turn);

        byte[] boardSingle = new byte[9];
        for (int i = 0; i < 9; i++) {
            boardSingle[i] = board[i / 3][i % 3];
        }
        outState.putByteArray("board", boardSingle);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        player1Turn = savedInstanceState.getBoolean("player1Turn");
        byte[] boardSingle = savedInstanceState.getByteArray("board");

        if (boardSingle != null) {
            for (int i = 0; i < 9; i++) {
                board[i / 3][i % 3] = boardSingle[i];
            }
        }

        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                if (board[i][j] == 1) {
                    button.setText("X");
                } else if (board[i][j] == 2) {
                    button.setText("O");
                }
            }
        }
    }
}
