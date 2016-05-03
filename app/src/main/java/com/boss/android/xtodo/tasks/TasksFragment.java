package com.boss.android.xtodo.tasks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseFragment;
import com.boss.android.xtodo.data.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/16.
 */
public class TasksFragment extends BaseFragment implements TasksContract.View {

    private Unbinder mUnbinder;

    private TasksContract.Presenter mPresenter;

    private TasksAdapter mTasksAdapter;

    @BindView(R.id.tasks_list)
    ListView mListView;

    @BindView(R.id.tasksLL)
    View mTasksView;

    @BindView(R.id.noTasks)
    View mNoTaskView;

    @BindView(R.id.noTasksIcon)
    ImageView mNoTaskIcon;

    @BindView(R.id.noTasksMain)
    TextView mNoTaskMainView;

    @BindView(R.id.noTasksAdd)
    View mNoTaskAddView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(new ArrayList<Task>(0), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        mUnbinder = ButterKnife.bind(this, root);

        mListView.setAdapter(mTasksAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.noTasksAdd)
    void addTask() {
        mPresenter.addTask();
    }

    @Override
    public void setPresenter(@NonNull TasksContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTasksAdapter.replaceData(tasks);

        mTasksView.setVisibility(View.VISIBLE);
        mNoTaskView.setVisibility(View.GONE);
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false);
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false);
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false);
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView){
        mTasksView.setVisibility(View.GONE);
        mNoTaskView.setVisibility(View.VISIBLE);

        mNoTaskMainView.setText(mainText);
        mNoTaskIcon.setImageResource(iconRes);
        mNoTaskAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAddTask() {
    }

    @Override
    public void showEditTask(Task task) {

    }

    @Override
    public void showTaskMarkedComplete() {

    }

    @Override
    public void showTaskMarkedActivate() {

    }

    @Override
    public void showCompletedTasksCleared() {

    }

    @Override
    public boolean onBackPressed() {
        Snackbar.make(getView(), "on back pressed in " + getClass().getName(), Snackbar.LENGTH_LONG).show();
        return true;
    }

    private static class TasksAdapter extends BaseAdapter {

        private List<Task> mTasks;

        private TaskItemListener mItemListener;

        public TasksAdapter(@Nullable List<Task> tasks, TaskItemListener listener) {
            super();
            setData(tasks);
            mItemListener = listener;
        }

        public void replaceData(List<Task> tasks) {
            setData(tasks);
            notifyDataSetChanged();
        }

        private void setData(List<Task> tasks){
            mTasks = checkNotNull(tasks);
        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Task getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                rowView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_task_item, viewGroup, false);
            }
            final Task task = getItem(i);
            TextView titleTV = (TextView) rowView.findViewById(R.id.title);
            titleTV.setText(task.getTitleForList());

            CheckBox completeCB = (CheckBox) rowView.findViewById(R.id.complete);

            // Active/Completed task
            completeCB.setChecked(task.isCompleted());
            if (task.isCompleted()) {
                rowView.setBackgroundResource(R.drawable.list_completed_touch_feedback);
            } else {
                rowView.setBackgroundResource(R.drawable.touch_feedback);
            }
            completeCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!task.isCompleted()) {
                        mItemListener.onCompleteTaskClick(task);
                    } else {
                        mItemListener.onActivateTaskClick(task);
                    }
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onTaskClick(task);
                }
            });
            return rowView;
        }
    }

    public interface TaskItemListener {

        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);

    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    TaskItemListener mItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.editTask(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };
}