package com.example.todoapp.database;

import androidx.room.TypeConverter;

public class BooleanTypeConverter {
    @TypeConverter
    public static Boolean toBoolean(Integer value) {
        return value == null ? null : value == 1;
    }

    @TypeConverter
    public static Integer toInteger(Boolean value) {
        return value == null ? null : value ? 1 : 0;
    }
}
