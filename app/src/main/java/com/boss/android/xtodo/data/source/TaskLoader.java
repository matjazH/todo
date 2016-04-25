package com.boss.android.xtodo.data.source;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;

import com.boss.android.xtodo.data.Task;

/**
 * Created by boss on 2016/4/16.
 */
public class TaskLoader extends AsyncTaskLoader<Task> implements TasksRepository.TasksRepositoryObserver {

    private final String mTaskId;

    private TasksRepository mRepository;

    public TaskLoader(String taskId, Context context, @NonNull TasksRepository repository) {
        super(context);

        mTaskId = taskId;
        mRepository = repository;

    }

    @Override
    public Task loadInBackground() {
        return mRepository.getTask(mTaskId);
    }

    @Override
    public void deliverResult(Task data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mRepository.cachedTasksAvailable()) {
            deliverResult(mRepository.getCachedTask(mTaskId));
        }

        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedTasksAvailable()) {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void onTasksChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
