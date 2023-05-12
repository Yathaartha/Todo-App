package com.example.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for the Todo Entity
 */
@Dao
public interface TodoDao {

    /**
     * Get all todos from the database
     * @return
     * LiveData list of all todos
     */
    @Query("SELECT * FROM TODO ORDER BY createdAt DESC")
    LiveData<List<Todo>> getTodos();

    /**
     * Get all todos from the database sorted by createdAt date ascending
     * @return
     * LiveData list of all todos
     */
    @Query("SELECT * FROM TODO ORDER BY createdAt ASC")
    LiveData<List<Todo>> getTodosCreatedAtAsc();

    /**
     * Get all todos from the database sorted by title ascending
     * @return
     * LiveData list of all todos
     */
    @Query("SELECT * FROM TODO ORDER BY title ASC")
    LiveData<List<Todo>> getTodosTitleAsc();

    /**
     * Get all todos from the database sorted by dueDate ascending
     * @return
     * LiveData list of all todos
     */
    @Query("SELECT * FROM TODO ORDER BY dueDate ASC")
    LiveData<List<Todo>> getTodosDueDateAsc();

    /**
     * Get all todos from the database sorted by dueDate descending
     * @return
     * LiveData list of all todos
     */
    @Query("SELECT * FROM TODO ORDER BY dueDate DESC")
    LiveData<List<Todo>> getTodosDueDateDesc();

    /**
     * get count of completed todos
     * @return
     * LiveData integer of completed todos
     */
    @Query("SELECT COUNT(*) FROM TODO WHERE isComplete = 1")
    LiveData<Integer> getCompletedTodoCount();

    /**
     * get count of incomplete todos
     * @return
     * LiveData integer of incomplete todos
     */
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
