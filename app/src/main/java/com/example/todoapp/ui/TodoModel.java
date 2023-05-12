package com.example.todoapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todoapp.database.Todo;
import com.example.todoapp.database.TodoRepository;

import java.util.List;
import java.util.UUID;

public class TodoModel extends AndroidViewModel {
    private TodoRepository repository;
    private LiveData<List<Todo>> todosList;

    public TodoModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);

        todosList = repository.getTodos();
    }

    LiveData<List<Todo>> getTodosList() {
        return todosList;
    }

    LiveData<List<Todo>> getTodosCreatedAtAsc() {
        return repository.getTodosCreatedAtAsc();
    }

    LiveData<List<Todo>> getTodosTitleAsc() {
        return repository.getTodosTitleAsc();
    }

    LiveData<List<Todo>> getTodosDueDateAsc() {
        return repository.getTodosDueDateAsc();
    }

    LiveData<List<Todo>> getTodosDueDateDesc() {
        return repository.getTodosDueDateDesc();
    }

    LiveData<Integer> getCompletedTodoCount() { return repository.getCompletedTodoCount(); }

    LiveData<Integer> getIncompleteTodoCount() { return repository.getIncompleteTodoCount(); }

    LiveData<Todo> getTodo(UUID id) {
        return repository.getTodo(id);
    }

    void addTodo(Todo todo) {
        repository.insert(todo);
    }

    void updateTodo(Todo todo) {repository.update(todo);}

    void delete(UUID id) {repository.delete(id);}
}