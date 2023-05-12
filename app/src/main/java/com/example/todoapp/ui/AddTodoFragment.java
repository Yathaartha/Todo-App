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
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
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
    private Button addButton;
    private Button cancelButton;
    private Calendar cal;

    private int daySet;
    private int monthSet;
    private int yearSet;


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
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        titleEditText = view.findViewById(R.id.titleInput);
        detailEditText = view.findViewById(R.id.detailInput);
        prioritySpinner = view.findViewById(R.id.prioritySelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        categorySpinner = view.findViewById(R.id.categorySelect);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter2);
        dueDateInput = view.findViewById(R.id.dueDateInput);

        addButton = view.findViewById(R.id.submit_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        prioritySpinner.setOnItemSelectedListener(new SpinnerActivity());
        categorySpinner.setOnItemSelectedListener(new SpinnerActivity());

        dueDate = cal.getTime();
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Toast.makeText(getContext(), "Todo created", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TodoListFragment.newInstance())
                        .commit();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(int year, int month, int day) {
        // Handle the selected date here
        // You have access to the year, month, and day values
        daySet = day;
        monthSet = month;
        yearSet = year;
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dueDateInput.setText("Due Date: " +month + "/" + day + "/" + year);
        dueDate = (Date) cal.getTime();
    }
}