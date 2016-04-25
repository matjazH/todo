package com.boss.android.xtodo.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.boss.android.xtodo.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/16.
 */
public class TasksRepository implements TasksDataSource{

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

    private List<TasksRepositoryObserver> mObservers = new ArrayList<TasksRepositoryObserver>();

    private Map<String, Task> mCachedTasks = new LinkedHashMap<String, Task>();

    boolean mCacheIsDirty;

    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource,
                                              TasksDataSource tasksLocalDataSource){
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return  INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private TasksRepository(@NonNull TasksDataSource tasksRemoteDataSource,
                            @NonNull TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = tasksRemoteDataSource;
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public void addContentObserver(TasksRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(TasksRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private  void notifyContentObserver() {
        for (TasksRepositoryObserver observer : mObservers) {
            observer.onTasksChanged();
        }
    }

    @Nullable
    @Override
    public List<Task> getTasks() {

        List<Task> tasks = null;

        if (!mCacheIsDirty) {
            if (mCachedTasks != null) {
                tasks = getCachedTasks();
                return tasks;
            } else {
                tasks = mTasksLocalDataSource.getTasks();
            }
        }

        if (tasks == null || tasks.isEmpty()) {
           tasks = mTasksRemoteDataSource.getTasks();
            saveTasksInLocalDataSource(tasks);
        }

        processLoadedTasks(tasks);

        return getCachedTasks();
    }

    public boolean cachedTasksAvailable() {
        return mCachedTasks != null && !mCacheIsDirty;
    }

    public List<Task> getCachedTasks() {
        return mCachedTasks == null ? null : new ArrayList<Task>(mCachedTasks.values());
    }

    public Task getCachedTask(String taskId) {
        return mCachedTasks.get(taskId);
    }

    private void saveTasksInLocalDataSource(List<Task> tasks) {
        if (tasks != null) {
            for (Task task : tasks) {
                mTasksLocalDataSource.saveTask(task);
            }
        }
    }

    private void processLoadedTasks(List<Task> tasks) {
        if (tasks == null) {
            mCacheIsDirty = false;
            return;
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    @Nullable
    @Override
    public Task getTask(@NonNull String taskId) {
        checkNotNull(taskId);

        Task cachedTask = getTaskWithId(taskId);

        if (cachedTask != null) {
            return cachedTask;
        }

        Task task = mTasksLocalDataSource.getTask(taskId);
        if (task == null) {
            task = mTasksRemoteDataSource.getTask(taskId);
        }
        return task;
    }

    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);

        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);

        mCachedTasks.put(task.getId(), task);

        notifyContentObserver();
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksRemoteDataSource.completeTask(task);
        mTasksLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        mCachedTasks.put(task.getId(), completedTask);

        notifyContentObserver();
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);

        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksRemoteDataSource.activateTask(task);
        mTasksLocalDataSource.activateTask(task);

        Task activateTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        mCachedTasks.put(task.getId(), activateTask);

        notifyContentObserver();
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);

        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksLocalDataSource.clearCompletedTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<String, Task>();
        }

        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }

        notifyContentObserver();
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    @Override
    public void deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();

        mCachedTasks.clear();

        notifyContentObserver();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksRemoteDataSource.deleteTask(checkNotNull(taskId));
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));

        mCachedTasks.remove(taskId);

        notifyContentObserver();
    }

    public interface TasksRepositoryObserver {

        void onTasksChanged();

    }
}
