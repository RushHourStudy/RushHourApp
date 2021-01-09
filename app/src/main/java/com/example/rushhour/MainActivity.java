package com.example.rushhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Timer Variables
    private static final long START_TIME_IN_MILLIS = 1800000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    //User input Set button
    private Button mButtonSet;
    private EditText mEditTextInput;

    //Rewards Button
    private Button mButtonReward;


    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonReward = findViewById(R.id.button_reward); // Initializing Reward Button on 1st activity page
        mButtonSet = findViewById(R.id.button_set); //Initialzing Set Button on 1st activity page

        //Uses method to open rewards page
        mButtonReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRewardsPage();
            }
            //method to open rewards page
            public void openRewardsPage() {
                Intent intent = new Intent(MainActivity.this, RewardsPage.class);
                startActivity(intent);
            }

        });


        //Uses method to recognize
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
            }});



                mButtonStartPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTimerRunning) {
                            pauseTimer();
                        } else {
                            startTimer();
                        }
                        mButtonReward.setText("Reward");
                        mButtonReward.setVisibility(View.VISIBLE);
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


            private void startTimer() {
                mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        updateCountDownText();
                        mButtonReward.setText("Reward");
                        mButtonReward.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        mTimerRunning = false;
                        updateButtons();
                        //Need to call method to collect points


                    }
                }.start();
                mTimerRunning = true;
                updateButtons();


            }

            private void pauseTimer() {
                mCountDownTimer.cancel();
                mTimerRunning = false;
                updateButtons();
            }

            private void resetTimer() {

                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                updateCountDownText();
                updateButtons();

            }


            private void updateCountDownText() {
                int minuets = (int) mTimeLeftInMillis / 1000 / 60;
                int seconds = (int) mTimeLeftInMillis / 1000 % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minuets, seconds);

                mTextViewCountDown.setText(timeLeftFormatted);
            }

            //Makes sure that when rotating device, timer does not change
            private void updateButtons() {
                if (mTimerRunning) {
                    mButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setText("Pause");
                    mButtonReward.setVisibility(View.INVISIBLE);
                } else {
                    mButtonStartPause.setText("Start");
                    if (mTimeLeftInMillis < 1000) {
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                    } else {
                        mButtonStartPause.setVisibility(View.VISIBLE);
                    }
                    if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                        mButtonReset.setVisibility(View.VISIBLE);
                        mButtonReward.setVisibility(View.VISIBLE);
                    } else {
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReward.setVisibility(View.INVISIBLE);
                    }
                }
            }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
    }
            @Override
            protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                outState.putLong("millisLeft", mTimeLeftInMillis);
                outState.putBoolean("timerRunning", mTimerRunning);
                outState.putLong("endTime", mEndTime);
            }

            @Override
            protected void onRestoreInstanceState(Bundle savedInstanceState) {
                super.onRestoreInstanceState(savedInstanceState);
                mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
                mTimerRunning = savedInstanceState.getBoolean("timerRunning");
                updateCountDownText();
                updateButtons();
                    if (mTimerRunning) {
                        mEndTime = savedInstanceState.getLong("endTime");
                        mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
                        startTimer();
                    }
            }



}


/* https://www.w3adda.com/android-tutorial/android-intents   - will help with the data access */