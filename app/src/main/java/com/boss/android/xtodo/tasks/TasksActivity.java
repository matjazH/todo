package com.boss.android.xtodo.tasks;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseActivity;
import com.boss.android.xtodo.data.source.TasksLoader;
import com.boss.android.xtodo.data.source.TasksRepository;
import com.boss.android.xtodo.data.source.local.TasksLocalDataSource;
import com.boss.android.xtodo.data.source.remote.TasksRemoteDataSource;
import com.boss.android.xtodo.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Nullable
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButterKnife.bind(this);

        setupViews();

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

    private void setupViews(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_list:
                        break;
                    case R.id.action_message:
                        break;
                    case R.id.action_about:
                        break;
                    case R.id.action_settings:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                Snackbar.make(mDrawerLayout, item.getTitle(), Snackbar.LENGTH_SHORT).show();
                item.setChecked(true);
                return true;
            }
        });
    }
}
