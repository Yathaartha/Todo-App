package com.example.todoapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.todoapp.database.Todo;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {

    private static final String ARG_TODO_ID = "todo_id";
    private static final String DATE_PATTERN = "MM/dd/yyyy";

    private Todo currentTodo;
    private TodoModel todoModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance(UUID todoId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO_ID, todoId);
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todoModel = new ViewModelProvider(this).get(TodoModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        ConstraintLayout layout = view.findViewById(R.id.todoFragmentLayout);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        TextView titleTextView = view.findViewById(R.id.titleText);
        TextView detailTextView = view.findViewById(R.id.detail);
        TextView categoryTextView = view.findViewById(R.id.category);
        TextView priorityTextView = view.findViewById(R.id.priority);
        TextView dueDateTextView = view.findViewById(R.id.dueDate);
//        TextView isCompleteTextView = view.findViewById(R.id.isComplete);
        TextView createdAtTextView = view.findViewById(R.id.createdAt);
        TextView lastUpdatedTextView = view.findViewById(R.id.lastUpdated);
        TextView completedAtTextView = view.findViewById(R.id.completedAt);

        Button toggleCompletion = view.findViewById(R.id.toggleCompletion);

        UUID todoId = (UUID) getArguments().getSerializable(ARG_TODO_ID);
        Log.d("currentTodo", ""+todoId);
        todoModel.getTodo(todoId).observe(getViewLifecycleOwner(), new Observer<Todo>() {
            @Override
            public void onChanged(Todo todo) {

                currentTodo = todo;
                titleTextView.setText(currentTodo.getTitle());
                detailTextView.setText(currentTodo.getDetail());
                categoryTextView.setText(currentTodo.getCategory());
                priorityTextView.setText(currentTodo.getPriority());
                dueDateTextView.setText(dateFormat.format(currentTodo.getDueDate()));
                createdAtTextView.setText(dateFormat.format(currentTodo.getCreatedAt()));
                if(currentTodo.getLastUpdated() != null){
                    lastUpdatedTextView.setText(dateFormat.format(currentTodo.getLastUpdated()));
                } else {
                    lastUpdatedTextView.setText("Never");
                }
                if(currentTodo.getCompletedAt() != null){
                    completedAtTextView.setText(dateFormat.format(currentTodo.getCompletedAt()));
                } else {
                    completedAtTextView.setText("-");
                }

                if(currentTodo.getIsComplete()){
                    toggleCompletion.setText("Mark Incomplete");
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
                    toggleCompletion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentTodo.setIsComplete(false);
                            currentTodo.setCompletedAt(null);
                            currentTodo.setLastUpdated(new Date());
                            todoModel.updateTodo(currentTodo);
                        }
                    });
                } else {
                    toggleCompletion.setText("Mark Complete");
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.danger));
                    toggleCompletion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentTodo.setIsComplete(true);
                            currentTodo.setCompletedAt(new Date());
                            currentTodo.setLastUpdated(new Date());
                            todoModel.updateTodo(currentTodo);
                        }
                    });
                }
            }
        });


        return view;
    }
}