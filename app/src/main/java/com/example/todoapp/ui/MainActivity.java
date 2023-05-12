package com.example.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.todoapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new TodoListFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}