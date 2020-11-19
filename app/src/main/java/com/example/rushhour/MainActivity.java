package com.example.rushhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Timer Variables
    private static final long START_TIME_IN_MILLIS = 1800000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    //Rewards Button
    private Button mButtonReward;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonReward = findViewById(R.id.button_reward); // Initializing Reward Button on 1st activity page

        //Uses method to open rewards page
        mButtonReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRewardsPage();
            }
        });


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }

            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();
    }

    //method to open rewards page
    public void openRewardsPage() {
        Intent intent = new Intent(this, Rewards_Page.class);
        startActivity(intent);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                mButtonReward.setText("Reward");
                mButtonReward.setVisibility(View.VISIBLE);

            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonReward.setText("Reward");
        mButtonReward.setVisibility(View.INVISIBLE);


    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning=false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
        mButtonReward.setText("Reward");
        mButtonReward.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {

        mTimeLeftInMillis=START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonReward.setVisibility(View.VISIBLE);

    }


    private void updateCountDownText() {
    int minuets = (int) mTimeLeftInMillis / 1000 / 60;
    int seconds = (int) mTimeLeftInMillis / 1000 % 60;

    String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minuets, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}


