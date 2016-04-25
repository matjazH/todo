package com.boss.android.xtodo.data.source;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;

import com.boss.android.xtodo.data.Task;

import java.util.List;

/**
 * Created by boss on 2016/4/16.
 */
public class TasksLoader extends AsyncTaskLoader<List<Task>> implements TasksRepository.TasksRepositoryObserver {

    private TasksRepository mRepository;

    public TasksLoader(@NonNull Context context, @NonNull TasksRepository repository) {
        super(context);

        mRepository = repository;

    }

    @Override
    public List<Task> loadInBackground() {
        return mRepository.getTasks();
    }

    @Override
    public void deliverResult(List<Task> data) {
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
            deliverResult(mRepository.getCachedTasks());
        }

        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedTasksAvailable()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void onTasksChanged() {
        if(isStarted()) {
            forceLoad();
        }
    }
}
