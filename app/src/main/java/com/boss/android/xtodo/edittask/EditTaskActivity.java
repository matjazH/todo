package com.boss.android.xtodo.edittask;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseActivity;
import com.boss.android.xtodo.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends BaseActivity {

    public static final int REQUEST_ADD_TASK = 1;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        ButterKnife.bind(this);

        setupViews();

        // Init Main Fragment
        FragmentManager manager = getFragmentManager();

        // Fragment
        EditTaskFragment fragment = new EditTaskFragment();
        EditTaskPresenter presenter = new EditTaskPresenter(fragment);

        ActivityUtils.addFragmentToActivity(manager, fragment, R.id.contentFrame);
    }

    private void setupViews(){
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra(EditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
            actionBar.setTitle(R.string.title_edit_task);

        } else {
            actionBar.setTitle(R.string.title_add_task);
        }
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
