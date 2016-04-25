package com.boss.android.xtodo.tasks;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.os.Bundle;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseActivity;
import com.boss.android.xtodo.data.source.TasksLoader;
import com.boss.android.xtodo.data.source.TasksRepository;
import com.boss.android.xtodo.data.source.local.TasksLocalDataSource;
import com.boss.android.xtodo.data.source.remote.TasksRemoteDataSource;
import com.boss.android.xtodo.util.ActivityUtils;

public class TasksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Init Main Fragment
        LoaderManager loaderManager = getLoaderManager();
        FragmentManager manager = getFragmentManager();

        // Fragment
        TasksFragment tasksFragment = new TasksFragment();
        TasksRepository repository = TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(getApplicationContext()));
        TasksLoader tasksLoader = new TasksLoader(getApplication(), repository);
        TasksPresenter tasksPresenter = new TasksPresenter(tasksFragment, repository, loaderManager, tasksLoader);

        ActivityUtils.addFragmentToActivity(manager, tasksFragment, R.id.contentFrame);
    }

    @Override
    public void onBackPressed() {
        // 处理Fragment中的返回键事件
        if (ActivityUtils.onActivityBackPressed(getFragmentManager())) {
            return;
        }
        super.onBackPressed();
    }
}
