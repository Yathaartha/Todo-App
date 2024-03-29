package com.example.todoapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoapp.util.BooleanTypeConverter;
import com.example.todoapp.util.DateTypeConverter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database for this app
 */
@Database(entities={Todo.class}, version=1, exportSchema = false)
@TypeConverters({DateTypeConverter.class, BooleanTypeConverter.class})
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();

    private static final int NUMBER_OF_THREADS = 4;

    // singleton to avoid multiple instances of the DB
    private static volatile TodoDatabase INSTANCE;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TodoDatabase getDatabase(final Context context) {

        if (INSTANCE == null){
            synchronized ( (TodoDatabase.class)) {
                if ( INSTANCE == null) {
                    // create DB
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TodoDatabase.class, "todo_database")
                            .addCallback(sRoomDatabaseCallback) // insert test data
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
//                  super.onCreate(db);

                    // If you want to keep the data through app restarts,
                    // comment out the following line.
//                    new PopulateDbAsync(INSTANCE);
                }
            };

    /**
     * Populate the database in the background with initial test data
     */
    private static class PopulateDbAsync {

        private final TodoDao mDao;

        PopulateDbAsync(TodoDatabase db) {
            mDao = TodoDatabase.INSTANCE.todoDao();

            databaseWriteExecutor.execute(() -> {
                mDao.deleteAll();

                Todo todo = new Todo();
                todo.setTitle("Wake up!");
                todo.setDetail("either set 2 alarm clocks or none");
                todo.setPriority("low");
                todo.setDueDate(new Date());
                todo.setCategory("home");
                mDao.insert(todo);

                Todo todo1 = new Todo();
                todo1.setTitle("Drink coffee!");
                todo1.setDetail("Use the liter mugs");
                todo1.setPriority("high");
                todo1.setDueDate(new Date());
                todo1.setCategory("work");
                mDao.insert(todo1);

                Todo todo2 = new Todo();
                todo2.setTitle("Ponder the duality of existence!");
                todo2.setDetail("and plant trees");
                todo2.setPriority("medium");
                todo2.setDueDate(new Date());
                todo2.setCategory("home");
                mDao.insert(todo2);

                Todo todo3 = new Todo();
                todo3.setTitle("make someone laugh");
                todo3.setDetail("read a comic");
                todo3.setPriority("urgent");
                todo3.setDueDate(new Date());
                todo3.setCategory("work");
                mDao.insert(todo3);
            });
        }

    }

}
