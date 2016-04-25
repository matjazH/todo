package com.boss.android.xtodo.tasks;

import android.support.annotation.NonNull;

import com.boss.android.xtodo.base.BasePresenter;
import com.boss.android.xtodo.base.BaseView;
import com.boss.android.xtodo.data.Task;

import java.util.List;

/**
 * Created by bojia on 2016/4/16.
 */
public interface TasksContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingTasksError();

        void showTasks(List<Task> tasks);

        void showNoTasks();

        void showNoCompletedTasks();

        void showNoActiveTasks();

        void showAddTask();

        void showEditTask(Task task);

        void showTaskMarkedComplete();

        void showTaskMarkedActivate();

        void showCompletedTasksCleared();

    }

    interface Presenter extends BasePresenter<View> {

        void addTask();

        void editTask(@NonNull Task task);

        void completeTask(@NonNull Task task);

        void activateTask(@NonNull Task task);

        void clearCompletedTasks();

        void loadTasks(boolean forceUpdate);
    }
}
