package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.todoapp.database.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

public class TodoListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private TodoModel todoModel;
    private View view;
    private FloatingActionButton addTodoButton;

    public static TodoListFragment newInstance() {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TodoListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        recyclerView = view.findViewById(R.id.todo_list_rv);
        addTodoButton = view.findViewById(R.id.add_todo_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new TodoAdapter();
        todoModel.getTodosList().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                Log.d("onChanged: ", "" + todos);
                adapter.submitData(todos);
            }
        });
        recyclerView.setAdapter(adapter);

        addTodoButton.setOnClickListener(v -> {
//            Todo todo = new Todo();
//            todo.setTitle("New Todo");
//            todo.setDetail("New Todo Detail");
//            todo.setIsComplete(true);
//            todoModel.addTodo(todo);
//            Intent intent = TodoPagerActivity.makeIntent(getActivity(), todo.getId());
//            startActivity(intent);
            Fragment fragment = new AddTodoFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        todoModel = new ViewModelProvider(this).get(TodoModel.class);
    }
}