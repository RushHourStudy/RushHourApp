package com.example.rushhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Timer Variables


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
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
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


        });


        //Button Set
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    setTime(1800000);
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    setTime(1800000);
                }
                setTime(millisInput);
                mEditTextInput.setText("");
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
                        /*mButtonReward.setText("Reward");
                        mButtonReward.setVisibility(View.VISIBLE);*/
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
            public void openRewardsPage() {
                Intent intent = new Intent(MainActivity.this, RewardsPage.class);
                startActivity(intent);
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

                mTimeLeftInMillis = mStartTimeInMillis;
                updateCountDownText();
                updateButtons();

            }
       private void setTime(long milliseconds) {
            mStartTimeInMillis = milliseconds;
           resetTimer();
                closeKeyboard();
        }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



            private void updateCountDownText() {
            if(mTimeLeftInMillis==1800000) {
                int minuets = (int) mTimeLeftInMillis / 1000 / 60;
                int seconds = (int) mTimeLeftInMillis / 1000 % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minuets, seconds);

                mTextViewCountDown.setText(timeLeftFormatted);
            }
            else{
                    int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
                    int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
                    int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
                    String timeLeftFormatted;
                    if (hours > 0) {
                        timeLeftFormatted = String.format(Locale.getDefault(),
                                "%d:%02d:%02d", hours, minutes, seconds);
                    } else {
                        timeLeftFormatted = String.format(Locale.getDefault(),
                                "%02d:%02d", minutes, seconds);
                    }
                    mTextViewCountDown.setText(timeLeftFormatted);
                }
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
                    if (mTimeLeftInMillis < mTimeLeftInMillis) {
                        mButtonReset.setVisibility(View.VISIBLE);
                        mButtonReward.setVisibility(View.VISIBLE);
                    } else {
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReward.setVisibility(View.INVISIBLE);
                    }
                }
            }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateButtons();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }








}}


/* https://www.w3adda.com/android-tutorial/android-intents   - will help with the data access */