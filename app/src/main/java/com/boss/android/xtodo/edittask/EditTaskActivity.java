package com.boss.android.xtodo.edittask;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.os.Bundle;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseActivity;
import com.boss.android.xtodo.data.source.TasksLoader;
import com.boss.android.xtodo.data.source.TasksRepository;
import com.boss.android.xtodo.data.source.local.TasksLocalDataSource;
import com.boss.android.xtodo.data.source.remote.TasksRemoteDataSource;
import com.boss.android.xtodo.tasks.TasksPresenter;
import com.boss.android.xtodo.util.ActivityUtils;

public class EditTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Init Main Fragment
        FragmentManager manager = getFragmentManager();

        // Fragment
        EditTaskFragment fragment = new EditTaskFragment();
        EditTaskPresenter presenter = new EditTaskPresenter(fragment);

        ActivityUtils.addFragmentToActivity(manager, fragment, R.id.contentFrame);
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
