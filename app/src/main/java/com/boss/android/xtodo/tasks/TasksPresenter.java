package com.boss.android.xtodo.tasks;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.boss.android.xtodo.data.Task;
import com.boss.android.xtodo.data.source.TasksLoader;
import com.boss.android.xtodo.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/16.
 */
public class TasksPresenter implements TasksContract.Presenter{

    private final static int TASKS_QUERY = 1;

    private TasksContract.View mView;

    private TasksRepository mRepository;

    private LoaderManager mLoaderManager;

    private TasksLoader mLoader;

    private List<Task> mCurrentTasks;

    private boolean mFirstLoad = false;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    public TasksPresenter(@NonNull TasksContract.View view, @NonNull TasksRepository repository, @NonNull LoaderManager loaderManager,
                          @NonNull TasksLoader loader){
        setView(view);

        mRepository = repository;

        mLoaderManager = loaderManager;

        mLoader = loader;
    }

    @Override
    public void setView(TasksContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(TASKS_QUERY, null, mLoaderCallbacks);
    }

    @Override
    public void addTask() {
        mView.showAddTask();
    }

    @Override
    public void editTask(@NonNull Task task) {
        checkNotNull(task, "editTask cannot be null!");
        mView.showEditTask(task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task, "completeTask cannot be null!");

        mRepository.completeTask(task);
        mView.showTaskMarkedComplete();

        loadTasks(false);
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task, "activateTask cannot be null!");

        mRepository.activateTask(task);
        mView.showTaskMarkedActivate();

        loadTasks(false);
    }

    @Override
    public void clearCompletedTasks() {
        mRepository.clearCompletedTasks();
        mView.showCompletedTasksCleared();

        loadTasks(false);
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad) {
            mFirstLoad = false;
            mRepository.refreshTasks();
        } else {
            showFilteredTasks();
        }
    }

    private void showFilteredTasks() {
        List<Task> tasksToDisplay = new ArrayList<>();
        if (mCurrentTasks != null) {
           for (Task task : mCurrentTasks) {
               switch (mCurrentFiltering) {
                   case ACTIVE_TASKS:
                       if (task.isActive()) {
                           tasksToDisplay.add(task);
                       }
                       break;
                   case COMPLETED_TASKS:
                       if (task.isCompleted()) {
                           tasksToDisplay.add(task);
                       }
                       break;
                   case ALL_TASKS:
                       tasksToDisplay.add(task);
                       break;
                   default:
                       tasksToDisplay.add(task);
                       break;
               }
           }
        }
        processTasks(tasksToDisplay);
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            processEmptyTasks();
        } else {
            mView.showTasks(tasks);
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mView.showNoCompletedTasks();
                break;
            default:
                mView.showNoTasks();
                break;
        }
    }

    LoaderManager.LoaderCallbacks<List<Task>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Task>>() {
        @Override
        public Loader<List<Task>> onCreateLoader(int i, Bundle bundle) {
            mView.setLoadingIndicator(true);
            return mLoader;
        }

        @Override
        public void onLoadFinished(Loader<List<Task>> loader, List<Task> tasks) {
            mView.setLoadingIndicator(false);

            mCurrentTasks = tasks;
            if (mCurrentTasks == null) {
                mView.showLoadingTasksError();
            } else {
                mView.showTasks(tasks);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Task>> loader) {

        }
    };
}
