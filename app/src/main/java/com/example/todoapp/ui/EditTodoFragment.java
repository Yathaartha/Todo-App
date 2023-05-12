package com.example.todoapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.database.Todo;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * EditTodoFragment is used to edit an existing todo.
 * Implements DatePickerFragment.DatePickerListener to handle the date picker dialog.
 */
public class EditTodoFragment extends Fragment implements DatePickerFragment.DatePickerListener {

    private static final String ARG_TODO_ID = "todo_id";
    private TodoModel todoModel;
    private TextInputEditText titleEditText;
    private TextInputEditText detailEditText;
    private Spinner prioritySpinner;
    private Spinner categorySpinner;
    private Button dueDateInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch isCompleteSwitch;
    private Date dueDate;
    private Calendar cal;
    private Todo currentTodo;

    public EditTodoFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance creates a new instance of EditTodoFragment with the given todoId.
     * @param todoId the id of the todo to edit
     * @return a new instance of EditTodoFragment
     */
    public static EditTodoFragment newInstance(UUID todoId) {
        EditTodoFragment fragment = new EditTodoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO_ID, todoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the todoModel from the ViewModelProvider
        todoModel = new ViewModelProvider(this).get(TodoModel.class);
        cal = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get references to the views
        View view = inflater.inflate(R.layout.fragment_edit_todo, container, false);

        titleEditText = view.findViewById(R.id.titleInput);
        detailEditText = view.findViewById(R.id.detailInput);
        prioritySpinner = view.findViewById(R.id.prioritySelect);
        // Create an ArrayAdapter using the string array and a default spinner layout
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
        isCompleteSwitch = view.findViewById(R.id.is_complete_switch);

        Button editButton = view.findViewById(R.id.submit_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        UUID todoId = (UUID) getArguments().getSerializable(ARG_TODO_ID);
        Log.d("currentTodo", ""+todoId);
        todoModel.getTodo(todoId).observe(getViewLifecycleOwner(), new Observer<Todo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Todo todo) {
                // Update the textFields with the todo's data
                currentTodo = todo;
                titleEditText.setText(todo.getTitle());
                detailEditText.setText(todo.getDetail());
                prioritySpinner.setSelection(adapter.getPosition(todo.getPriority()));
                categorySpinner.setSelection(adapter2.getPosition(todo.getCategory()));
                cal.setTime(todo.getDueDate());
                dueDateInput.setText(cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
                isCompleteSwitch.setChecked(todo.getIsComplete());
            }
        });

        prioritySpinner.setOnItemSelectedListener(new SpinnerActivity());
        categorySpinner.setOnItemSelectedListener(new SpinnerActivity());

        dueDate = cal.getTime();
        dueDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(EditTodoFragment.this);
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String detail = detailEditText.getText().toString();
                String priority = prioritySpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();

                currentTodo.setId(currentTodo.getId());
                currentTodo.setTitle(title);
                currentTodo.setDetail(detail);
                currentTodo.setPriority(priority);
                currentTodo.setCategory(category);
                currentTodo.setDueDate(dueDate);
                currentTodo.setLastUpdated(new Date());
                currentTodo.setIsComplete(isCompleteSwitch.isChecked());

                if(isCompleteSwitch.isChecked()) {
                    currentTodo.setCompletedAt(new Date());
                } else {
                    currentTodo.setCompletedAt(null);
                }

                todoModel.updateTodo(currentTodo);

                Toast.makeText(getContext(), "Todo updated", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TodoListFragment.newInstance())
                        .commit();
            }
        });

        return view;
    }

    // DatePickerListener methods to handle the date picker dialog
    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(int year, int month, int day) {
        // Handle the selected date here
        // You have access to the year, month, and day values
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        dueDateInput.setText(month + "/" + day + "/" + year);
        dueDate = (Date) cal.getTime();
        Log.d("dueDate",""+dueDate);
    }
}