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

    public void insert(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.insert(todo);
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
