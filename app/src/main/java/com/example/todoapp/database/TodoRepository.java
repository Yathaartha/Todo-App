package com.example.todoapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.UUID;

public class TodoRepository {
    private TodoDao todoDao;
    private TodoDatabase database;
    private LiveData<List<Todo>> todos;

    public TodoRepository(Application application) {
        database = TodoDatabase.getDatabase(application);
        todoDao = database.todoDao();
    }

    public LiveData<List<Todo>> getTodos() {
        return todoDao.getTodos();
    }

    public LiveData<List<Todo>> getTodosCreatedAtDesc() {
        return todoDao.getTodosCreatedAtDesc();
    }

    public LiveData<List<Todo>> getTodosTitleAsc() {
        return todoDao.getTodosTitleAsc();
    }

    public LiveData<List<Todo>> getTodosDueDateAsc() {
        return todoDao.getTodosDueDateAsc();
    }

    public LiveData<List<Todo>> getTodosDueDateDesc() {
        return todoDao.getTodosDueDateDesc();
    }

    public LiveData<Integer> getCompletedTodoCount() {
        return todoDao.getCompletedTodoCount();
    };

    public LiveData<Integer> getIncompleteTodoCount() {
        return todoDao.getIncompleteTodoCount();
    };

    public void insert(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.insert(todo);
        });
    }

    public void update(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.update(todo);
        });
    }

    public LiveData<Todo> getTodo(UUID id) {
        return todoDao.getTodoById(id);
    }

    public void delete(UUID id) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.delete(id);
        });
    }

    public void deleteAll() {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.deleteAll();
        });
    }
}
