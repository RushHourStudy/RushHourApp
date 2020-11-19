package com.example.rushhour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class Rewards_Page extends AppCompatActivity {
    private String name;
    private int points;

    public void collectPoints() {
        points = points + 1;

    }


    public int getPoints() {
        return points;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards__page);

    }
}