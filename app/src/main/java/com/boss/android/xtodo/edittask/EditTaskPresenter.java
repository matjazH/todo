package com.boss.android.xtodo.edittask;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/16.
 */
public class EditTaskPresenter implements EditTaskContract.Presenter {

    private EditTaskContract.View mView;

    public EditTaskPresenter(@NonNull EditTaskContract.View view) {
        checkNotNull(view);
        setView(view);
    }

    @Override
    public void setView(EditTaskContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
