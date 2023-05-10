package com.example.todoapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.todoapp.database.Todo;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTodoFragment extends Fragment {

    private TodoModel todoModel;
    private TextInputEditText titleEditText;
    private TextInputEditText detailEditText;
    private Spinner prioritySpinner;
    private Button addButton;
    private Button cancelButton;


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
        addButton = view.findViewById(R.id.submit_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        prioritySpinner.setOnItemSelectedListener(new SpinnerActivity());

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

                Todo todo = new Todo();
                todo.setTitle(title);
                todo.setDetail(detail);
                todo.setPriority(priority);

                todoModel.addTodo(todo);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TodoListFragment.newInstance())
                        .commit();
            }
        });

        return view;
    }
}