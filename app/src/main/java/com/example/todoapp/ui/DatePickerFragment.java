package com.example.todoapp.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * DatePickerFragment is used to display a date picker dialog.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private DatePickerListener datePickerListener;

    public static DatePickerFragment newInstance(DatePickerListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.datePickerListener = listener;
        return fragment;
    }

    /**
     * onCreateDialog is called by the FragmentManager to obtain an instance of the Dialog.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return A new DatePickerDialog instance.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    /**
     * onDateSet is called when the user selects a date.
     * @param view The DatePicker associated with this listener.
     * @param year The year that was set.
     * @param month The month that was set (0-11) for compatibility with Calendar.
     * @param day The day of the month that was set.
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (datePickerListener != null) {
            datePickerListener.onDateSelected(year, month, day);
        }
    }

    /**
     * DatePickerListener is used to handle the date selected by the user.
     */
    public interface DatePickerListener {
        void onDateSelected(int year, int month, int day);
    }
}