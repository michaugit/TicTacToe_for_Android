package myApp.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private Button buttonRandomTurn;
    private boolean startTurnPlayer;
    private boolean playerOTurn;
    private int roundCount;

    private int playerOPoints;
    private int playerXPoints;

    private int playerOColor = Color.parseColor("#33CCFF");
    private int playerXColor = Color.parseColor("#FF0000");

    private TextView textViewPlayerO;
    private TextView textViewPlayerX;
    private TextView textViewTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayerO = findViewById(R.id.text_view_p1);
        textViewPlayerX = findViewById(R.id.text_view_p2);
        textViewTurn = findViewById(R.id.turn_option);

        generateRandomTurn();
        setListners();
    }
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        buttonRandomTurn.setVisibility(View.INVISIBLE);
        if (playerOTurn) {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(playerOColor);
        } else {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(playerXColor);
        }
        roundCount++;
        if (checkForWin()) {
            if (playerOTurn) {
                playerOWins();
            } else {
                plaeyerXWins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            playerOTurn = !playerOTurn;
            setTurnText();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("playerOPoints", playerOPoints);
        outState.putInt("playerXPoints", playerXPoints);
        outState.putBoolean("playerOTurn",playerOTurn);
        outState.putBoolean("startTurnPlayer",startTurnPlayer);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        playerOPoints = savedInstanceState.getInt("playerOPoints");
        playerXPoints = savedInstanceState.getInt("playerXPoints");
        playerOTurn = savedInstanceState.getBoolean("playerOTurn");
        startTurnPlayer = savedInstanceState.getBoolean("startTurnPlayer");

        int orientation= getResources().getConfiguration().orientation;

        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            for(int i = 0; i < 3; ++i){
                for(int j = 0; j < 3; ++j){
                    buttons[i][j].setPadding(0,0,0,0);
                }
            }
        }
        else{
            for(int i = 0; i < 3; ++i){
                for(int j = 0; j < 3; ++j){
                    buttons[i][j].setPaddingRelative(0,-25,0,0);
                }
            }
        }

    }

    private void setListners(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        buttonRandomTurn = findViewById(R.id.random_turn);
        buttonRandomTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true){ generateRandomTurn();}
            }
        });
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                ArrayList<Button> arrayToHighlight = new ArrayList<>();
                arrayToHighlight.add(buttons[i][0]);
                arrayToHighlight.add(buttons[i][1]);
                arrayToHighlight.add(buttons[i][2]);
                highlightWinsPattern(arrayToHighlight);
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                ArrayList<Button> arrayToHighlight = new ArrayList<>();
                arrayToHighlight.add(buttons[0][i]);
                arrayToHighlight.add(buttons[1][i]);
                arrayToHighlight.add(buttons[2][i]);
                highlightWinsPattern(arrayToHighlight);
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            ArrayList<Button> arrayToHighlight = new ArrayList<>();
            arrayToHighlight.add(buttons[0][0]);
            arrayToHighlight.add(buttons[1][1]);
            arrayToHighlight.add(buttons[2][2]);
            highlightWinsPattern(arrayToHighlight);
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            ArrayList<Button> arrayToHighlight = new ArrayList<>();
            arrayToHighlight.add(buttons[0][2]);
            arrayToHighlight.add(buttons[1][1]);
            arrayToHighlight.add(buttons[2][0]);
            highlightWinsPattern(arrayToHighlight);
            return true;
        }
        return false;
    }

    private void playerOWins() {
        playerOPoints++;
        Toast.makeText(this, "Player O wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        //resetBoard();
    }

    private void plaeyerXWins() {
        playerXPoints++;
        Toast.makeText(this, "Player X wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        //resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayerO.setText("Player O: " + playerOPoints);
        textViewPlayerX.setText("Player X: " + playerXPoints);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        startTurnPlayer= !startTurnPlayer;
        playerOTurn= startTurnPlayer;
        setTurnText();
        buttonRandomTurn.setVisibility(View.VISIBLE);
    }

    private void generateRandomTurn(){
        Random rand = new Random();
        startTurnPlayer=rand.nextBoolean();
        playerOTurn=startTurnPlayer;
        setTurnText();
    }

    private void setTurnText(){
        if(playerOTurn){
            textViewTurn.setText("O");
            textViewTurn.setTextColor(playerOColor);
        }
        else{
            textViewTurn.setText("X");
            textViewTurn.setTextColor(playerXColor);
        }
    }

    private void resetGame(){
        playerOPoints=0;
        playerXPoints=0;
        updatePointsText();
        resetBoard();
    }

    private void highlightWinsPattern(ArrayList<Button> winButtons) {

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                resetBoard();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        winButtons.forEach((button) -> button.startAnimation(anim));
    }
}