package com.boss.android.xtodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.boss.android.xtodo.data.Task;
import com.boss.android.xtodo.data.source.TasksDataSource;
import com.boss.android.xtodo.data.source.local.TasksPersistenceContract.TaskEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/16.
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private TasksDBHelper mDBHelper;

    private SQLiteDatabase mDb;

    private TasksLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDBHelper = new TasksDBHelper(context);
        mDb = mDBHelper.getWritableDatabase();
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null){
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>();
        try {
            String[] projection = {
                    TaskEntry.COLUMN_NAME_ENTRY_ID,
                    TaskEntry.COLUMN_NAME_TITLE,
                    TaskEntry.COLUMN_NAME_DESCRIPTION,
                    TaskEntry.COLUMN_NAME_COMPLETED,
            };

            Cursor c = mDb.query(
                    TasksPersistenceContract.TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String itemId = c
                            .getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                    String title = c
                            .getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                    String description =
                            c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                    boolean completed =
                            c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                    Task task = new Task(title, description, itemId, completed);
                    tasks.add(task);
                }
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e){

        }
        return tasks;
    }

    @Nullable
    @Override
    public Task getTask(@NonNull String taskId) {
        try {
            String[] projection = {
                    TaskEntry.COLUMN_NAME_ENTRY_ID,
                    TaskEntry.COLUMN_NAME_TITLE,
                    TaskEntry.COLUMN_NAME_DESCRIPTION,
                    TaskEntry.COLUMN_NAME_COMPLETED
            };

            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {taskId};

            Cursor c = mDb.query(
                    TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

            Task task = null;

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                String itemId = c
                        .getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                task = new Task(title, description, itemId, completed);
            }
            if (c != null) {
                c.close();
            }

            return task;
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
        return null;
    }

    @Override
    public void saveTask(@NonNull Task task) {
        try {
            checkNotNull(task);

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
            values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
            values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

            mDb.insert(TaskEntry.TABLE_NAME, null, values);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void completeTask(@NonNull Task task) {
        try {
            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {task.getId()};

            mDb.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {
        try {
            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {task.getId()};

            mDb.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {
        try {
            String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
            String[] selectionArgs = {"1"};

            mDb.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        try {
            mDb.delete(TaskEntry.TABLE_NAME, null, null);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        try {
            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {taskId};

            mDb.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }
}
