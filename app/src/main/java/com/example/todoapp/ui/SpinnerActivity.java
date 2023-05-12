package com.example.todoapp.ui;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * SpinnerActivity
 * This class is used to handle the spinner in the AddTodoFragment.
 * Implements AdapterView.OnItemSelectedListener to handle the spinner.
 */
public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
