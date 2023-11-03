// Define the package and import necessary classes
package com.ousllab.projecttry.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ousllab.projecttry.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

// Define a class that extends SQLiteOpenHelper
public class DatabaseHelper extends SQLiteOpenHelper {

    // Define constants for the database name, version, table name, and column names
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_TASK_NAME = "taskName";
    private static final String COLUMN_NAME_LATITUDE = "lat";
    private static final String COLUMN_NAME_LONGITUDE = "long";

    // Define a constructor that takes a Context object and calls the super constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Override the onCreate method to create the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_TASK_NAME + " TEXT, " +
                COLUMN_NAME_LATITUDE + " REAL, " +
                COLUMN_NAME_LONGITUDE + " REAL)";
        db.execSQL(SQL_CREATE_TABLE);
    }

    // Override the onUpgrade method to handle database upgrades
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Currently does nothing
    }

    // Define a method to insert a task into the database
    public void insertTask(String taskName, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();

        String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_NAME_TASK_NAME + ", " +
                COLUMN_NAME_LATITUDE + ", " +
                COLUMN_NAME_LONGITUDE + ") VALUES (?, ?, ?)";

        // Execute the insert query and pass in the task name, latitude, and longitude as parameters
        db.execSQL(SQL_INSERT, new Object[] { taskName, latitude, longitude });
        db.close();
    }

    // Define a method to get all tasks from the database
    public List<TaskModel> getAllTasks() {
        SQLiteDatabase db = getReadableDatabase();
        String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

        // Execute the select query and get a Cursor object
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL, null);
        List<TaskModel> tasks = new ArrayList<>();

        // Iterate over the cursor and create TaskModel objects for each row
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
            String taskName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK_NAME));
            double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_LONGITUDE));

            TaskModel taskModel = new TaskModel(id, taskName, latitude, longitude);
            tasks.add(taskModel);
        }

        cursor.close();
        db.close();

        // Return the list of TaskModel objects
        return tasks;
    }

    // Define a method to delete a task by its id

    public void deleteTaskById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String WHERE_CLAUSE = COLUMN_NAME_ID + " = ?";
        String[] WHERE_ARGS = {String.valueOf(id)};
        db.delete(TABLE_NAME, WHERE_CLAUSE, WHERE_ARGS);

        db.close();
    }
    // Define a method to update a task by its id
    public void updateTask(TaskModel task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TASK_NAME, task.getTaskName());
        values.put(COLUMN_NAME_LATITUDE, task.getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, task.getLongitude());
        String WHERE_CLAUSE = COLUMN_NAME_ID + " = ?";
        String[] WHERE_ARGS = { String.valueOf(task.getId()) };
        db.update(TABLE_NAME, values, WHERE_CLAUSE, WHERE_ARGS);

        db.close();
    }

}
