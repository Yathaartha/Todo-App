package com.example.todoapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.database.Todo;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Calendar;

/**
 * AddToFragment
 * This fragment is used to add a new todo to the database.
 * Implements DatePickerFragment.DatePickerListener to handle the date picker dialog.
 */
public class AddTodoFragment extends Fragment implements DatePickerFragment.DatePickerListener {

    private TodoModel todoModel;
    private TextInputEditText titleEditText;
    private TextInputEditText detailEditText;
    private Spinner prioritySpinner;
    private Spinner categorySpinner;
    private Button dueDateInput;
    private DatePickerFragment datePicker;
    private Date dueDate;
    private Calendar cal;


    public AddTodoFragment() {
        // Required empty public constructor
    }

    public static AddTodoFragment newInstance() {
        AddTodoFragment fragment = new AddTodoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todoModel = new ViewModelProvider(this).get(TodoModel.class);
        cal = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        titleEditText = view.findViewById(R.id.titleInput);
        detailEditText = view.findViewById(R.id.detailInput);
        prioritySpinner = view.findViewById(R.id.prioritySelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        categorySpinner = view.findViewById(R.id.categorySelect);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter2);
        dueDateInput = view.findViewById(R.id.dueDateInput);

        Button addButton = view.findViewById(R.id.submit_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        prioritySpinner.setOnItemSelectedListener(new SpinnerActivity());
        categorySpinner.setOnItemSelectedListener(new SpinnerActivity());

        dueDate = cal.getTime();
        // Set onClickListener for dueDateInput to show the date picker dialog
        dueDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(AddTodoFragment.this);
                newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TodoListFragment.newInstance())
                        .commit();
            }
        });

        // Set onClickListener for addButton to add the todo to the database
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the input fields
                String title = titleEditText.getText().toString();
                String detail = detailEditText.getText().toString();
                String priority = prioritySpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();

                Todo todo = new Todo();
                todo.setTitle(title);
                todo.setDetail(detail);
                todo.setPriority(priority);
                todo.setCategory(category);
                todo.setDueDate(dueDate);

                todoModel.addTodo(todo);

                // Display a toast to indicate that the todo was added
                Toast.makeText(getContext(), "Todo created", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TodoListFragment.newInstance())
                        .commit();
            }
        });

        return view;
    }

    /**
     * onDateSelected sets the dueDate to the date selected by the user
     * @param year The year selected
     * @param month The month selected
     * @param day The day selected
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(int year, int month, int day) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        dueDateInput.setText("Due Date: " +month + "/" + day + "/" + year);
        dueDate = (Date) cal.getTime();
    }
}