package com.example.fingerspeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView timerTextView;
    private TextView thousandTextView;
    private Button tapButton;
    private CountDownTimer countDownTimer;

    private final String REMANING_TIME_KEY = "remaining time key";
    private final String REMAING_TAP_KEY = "remaining taps key";

    private boolean isFirst = true;
    private long initialCundDownInMillis = 10_000l;
    private int timeIntervals = 1_000;
    private int remainingTime = (int)initialCundDownInMillis/1000;
    private int tapRemaming = 10;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(REMANING_TIME_KEY, remainingTime);
        outState.putInt(REMAING_TAP_KEY, tapRemaming);

        countDownTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerTextView = findViewById(R.id.timeTextView);
        thousandTextView = findViewById(R.id.thousandTextView);
        tapButton = findViewById(R.id.tapButton);

        timerTextView.setText(remainingTime+"");
        thousandTextView.setText(tapRemaming+"");

        if(savedInstanceState != null) {
            remainingTime = savedInstanceState.getInt(REMANING_TIME_KEY);
            tapRemaming = savedInstanceState.getInt(REMAING_TAP_KEY);

            restoreGame();
        }

        if(savedInstanceState == null) {
            countDownTimer = new CountDownTimer(initialCundDownInMillis, timeIntervals) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(remainingTime + "");
                }

                @Override
                public void onFinish() {
                    Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                    tapButton.setEnabled(false);
                    if (tapRemaming > 0 && remainingTime == 0) {
                        Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                        showAlert("Game Over", "Would you like to reset the game?");
                        isFirst = true;
                    }
                }
            };
        }
    }

    private void restoreGame() {
        int resortedRemainingTime  = remainingTime;
        int resortedTapsRemaining = tapRemaming;

        thousandTextView.setText(resortedTapsRemaining + "");
        timerTextView.setText(resortedRemainingTime+"");

        countDownTimer = new CountDownTimer((long)resortedRemainingTime*1000 , timeIntervals) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int)millisUntilFinished/1000;
                timerTextView.setText(remainingTime +"");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirst) {
                    countDownTimer.start();
                    isFirst = false;
                }
                tapRemaming--;
                thousandTextView.setText(tapRemaming+"");
                if(tapRemaming <= 0 && remainingTime > 0){
                    tapButton.setEnabled(false);
                    countDownTimer.cancel();
                    Toast.makeText(MainActivity.this, "Congratulations", Toast.LENGTH_SHORT).show();
                    isFirst = true;
                    showAlert("Finised", "Would you like to reset the game?");
                }



            }
        });
    }

    private void gameReset() {
        remainingTime = (int)initialCundDownInMillis/1000;
        tapRemaming = 10;
        tapButton.setEnabled(true);
        timerTextView.setText(remainingTime+"");
        thousandTextView.setText(tapRemaming+"");
    }

    private void showAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameReset();
                    }
                }).show();

        alertDialog.setCancelable(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item){
            Toast.makeText(MainActivity.this, BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}