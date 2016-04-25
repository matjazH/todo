package com.boss.android.xtodo.edittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseFragment;

/**
 * Created by boss on 2016/4/16.
 */
public class EditTaskFragment extends BaseFragment implements EditTaskContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_task, container, false);

        return root;
    }

    @Override
    public void setPresenter(EditTaskContract.Presenter presenter) {

    }
}
