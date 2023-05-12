package com.example.todoapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.UUID;

/**
 * Repository for the Todo Entity
 */
public class TodoRepository {
    private final TodoDao todoDao;
    private LiveData<List<Todo>> todos;

    public TodoRepository(Application application) {
        TodoDatabase database = TodoDatabase.getDatabase(application);
        // get the TodoDao from the database
        todoDao = database.todoDao();
    }

    public LiveData<List<Todo>> getTodos() {
        return todoDao.getTodos();
    }

    public LiveData<List<Todo>> getTodosCreatedAtAsc() {
        return todoDao.getTodosCreatedAtAsc();
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
        TodoDatabase.databaseWriteExecutor.execute(todoDao::deleteAll);
    }
}
