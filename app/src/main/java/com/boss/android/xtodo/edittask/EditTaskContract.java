package com.boss.android.xtodo.edittask;

import com.boss.android.xtodo.base.BasePresenter;
import com.boss.android.xtodo.base.BaseView;

/**
 * Created by boss on 2016/4/16.
 */
public interface EditTaskContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter<View>{

    }
}
