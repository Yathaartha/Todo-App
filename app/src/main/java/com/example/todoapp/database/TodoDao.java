package com.example.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM TODO ORDER BY createdAt DESC")
    LiveData<List<Todo>> getTodos();

    @Query("SELECT * FROM TODO ORDER BY createdAt ASC")
    LiveData<List<Todo>> getTodosCreatedAtAsc();

    @Query("SELECT * FROM TODO ORDER BY title ASC")
    LiveData<List<Todo>> getTodosTitleAsc();

    @Query("SELECT * FROM TODO ORDER BY dueDate ASC")
    LiveData<List<Todo>> getTodosDueDateAsc();

    @Query("SELECT * FROM TODO ORDER BY dueDate DESC")
    LiveData<List<Todo>> getTodosDueDateDesc();

    @Query("SELECT COUNT(*) FROM TODO WHERE isComplete = 1")
    LiveData<Integer> getCompletedTodoCount();

    @Query("SELECT COUNT(*) FROM TODO WHERE isComplete = 0")
    LiveData<Integer> getIncompleteTodoCount();

    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Query("DELETE FROM TODO")
    void deleteAll();

    @Query("SELECT * FROM TODO WHERE id = :id")
    LiveData<Todo> getTodoById(UUID id);

    @Query("DELETE FROM TODO WHERE id = :id")
    void delete(UUID id);
}
