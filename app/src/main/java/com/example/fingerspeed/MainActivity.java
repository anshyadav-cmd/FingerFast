package com.example.fingerspeed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

    private boolean isFirst = true;
    private long initialCundDownInMillis = 10_000l;
    private int timeIntervals = 1_000;
    private int remainingTime = (int)initialCundDownInMillis/1000;
    private int tapRemaming = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerTextView = findViewById(R.id.timeTextView);
        thousandTextView = findViewById(R.id.thousandTextView);
        tapButton = findViewById(R.id.tapButton);

        timerTextView.setText(remainingTime+"");
        thousandTextView.setText(tapRemaming+"");

        countDownTimer = new CountDownTimer(initialCundDownInMillis, timeIntervals) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int)millisUntilFinished/1000;
                timerTextView.setText(remainingTime+ "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                tapButton.setEnabled(false);
                if(tapRemaming > 0 && remainingTime == 0 ){
                    Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                    showAlert("Game Over", "Would you like to reset the game?");
                    isFirst = true;
                }
            }
        };
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
}