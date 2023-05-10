package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todoapp.database.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoPagerActivity extends AppCompatActivity {

    public static final String EXTRA_TODO_ID = "todo_id";

    private ViewPager2 viewPager;
    private TodoModel todoModel;
    private List<Todo> todosList = new ArrayList<>();
    public static Intent makeIntent(Context context, UUID todoId) {
        Intent intent = new Intent(context, TodoPagerActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_pager);

        Intent intent = getIntent();
        UUID todoId = (UUID) intent.getSerializableExtra(EXTRA_TODO_ID);

        todoModel = new ViewModelProvider(this).get(TodoModel.class);
        todoModel.getTodosList().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                todosList = todos;
                for (int i = 0; i < todosList.size(); i++) {
                    if (todosList.get(i).getId().equals(todoId)) {
                        viewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        });

        Log.d("todosList: ", "" + todosList);

        viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                if (todosList != null)
                    return todosList.size();
                else return 0;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Todo todo = todosList.get(position);
                return TodoFragment.newInstance(todo.getId());
            }
        });

    }
}
