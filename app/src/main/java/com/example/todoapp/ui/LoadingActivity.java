package com.example.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.todoapp.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Perform initialization or data loading tasks here

        // Example: Simulating a delay of 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to the next activity
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                finish();
            }
        }, 2000); // Delay of 2 seconds
    }
}