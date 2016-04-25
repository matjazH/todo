package com.boss.android.xtodo.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.boss.android.xtodo.data.Task;

import java.util.List;

/**
 * Created by boss on 2016/4/16.
 */
public interface TasksDataSource {

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();

    }

    @Nullable
    List<Task> getTasks();

    @Nullable
    Task getTask(@NonNull String taskId);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Task task);

    void activateTask(@NonNull String taskId);

    void clearCompletedTasks();

    void refreshTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);
}
