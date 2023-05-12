package com.example.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.todoapp.R;

/**
 * MainActivity is the entry point of the app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example: Adding a fragment to the activity
        Fragment fragment = new TodoListFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.container, fragment)// Add the fragment to the 'container' FrameLayout
                .commit();// Commit the transaction
    }
}