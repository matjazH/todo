package com.boss.android.xtodo.tasks;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseActivity;
import com.boss.android.xtodo.data.source.TasksLoader;
import com.boss.android.xtodo.data.source.TasksRepository;
import com.boss.android.xtodo.data.source.local.TasksLocalDataSource;
import com.boss.android.xtodo.data.source.remote.TasksRemoteDataSource;
import com.boss.android.xtodo.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends BaseActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private TasksPresenter mTasksPresenter;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
        mTasksPresenter = new TasksPresenter(tasksFragment, repository, loaderManager, tasksLoader);

        ActivityUtils.addFragmentToActivity(manager, tasksFragment, R.id.contentFrame);

        if (savedInstanceState != null) {
            TasksFilterType tasksFilterType = (TasksFilterType)savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(tasksFilterType);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawerLayout.addDrawerListener(mDrawerListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawerLayout.removeDrawerListener(mDrawerListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_action_list:
                        break;
                    case R.id.menu_action_message:
                        break;
                    case R.id.menu_action_about:
                        break;
                    case R.id.menu_action_settings:
                        break;
                }
                Snackbar.make(mDrawerLayout, item.getTitle(), Snackbar.LENGTH_SHORT).show();
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @OnClick(R.id.fab_add_task)
    void addTask(){
        mTasksPresenter.addTask();
    }

    DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}
