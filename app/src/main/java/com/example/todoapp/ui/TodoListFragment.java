package com.example.todoapp.ui;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.todoapp.R;
import com.example.todoapp.database.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class TodoListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private TodoModel todoModel;
    private View view;
    private FloatingActionButton addTodoButton;
    private TextView completeCount;
    private TextView incompleteCount;
    private TextView totalCount;

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
        completeCount = view.findViewById(R.id.completeTodos);
        incompleteCount = view.findViewById(R.id.incompleteTodos);
        totalCount = view.findViewById(R.id.totalTodo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new TodoAdapter();

        todoModel.getCompletedTodoCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                completeCount.setText(""+count);
            }
        });

        todoModel.getIncompleteTodoCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                incompleteCount.setText(""+count);
            }
        });

        todoModel.getTodosList().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                Log.d("onChanged: ", "" + todos);
                totalCount.setText("Total Tasks: "+todos.size());
                if(todos.size() == 0) {
                    Todo todo = new Todo();
                    todo.setTitle("No todos yet");
                    todo.setDetail("Press the add icon to create one");
                    todo.setIsComplete(true);
                    todo.setPriority("urgent");
                    List<Todo> emptyList = Arrays.asList(todo);
                    adapter.submitData(emptyList);
                }else {
                    adapter.submitData(todos);
                }

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.todo_menu, menu);
        MenuItem item = menu.findItem(R.id.sort_todos);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_todos_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = parent.getItemAtPosition(pos).toString();
                Log.d("onItemSelected: ", selected);
                if(selected.equals("Created At (Latest to Oldest)")) {
                    todoModel.getTodosList().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
                        @Override
                        public void onChanged(List<Todo> todos) {
                            adapter.submitData(todos);
                        }
                    });
                }else if(selected.equals("Created At (Oldest to Latest)")) {
                    todoModel.getTodosCreatedAtAsc().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
                        @Override
                        public void onChanged(List<Todo> todos) {
                            adapter.submitData(todos);
                        }
                    });
                }else if(selected.equals("Alphabetical")) {
                    todoModel.getTodosTitleAsc().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
                        @Override
                        public void onChanged(List<Todo> todos) {
                            adapter.submitData(todos);
                        }
                    });
                }else if(selected.equals("Due Date (Closest to Farthest)")) {
                    todoModel.getTodosDueDateAsc().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
                        @Override
                        public void onChanged(List<Todo> todos) {
                            adapter.submitData(todos);
                        }
                    });
                } else if(selected.equals("Due Date (Farthest to Closest)")) {
                    todoModel.getTodosDueDateDesc().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
                        @Override
                        public void onChanged(List<Todo> todos) {
                            adapter.submitData(todos);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(spinnerAdapter);
    }
}