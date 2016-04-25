package com.boss.android.xtodo.base;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by boss on 2016/4/16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 处理返回事件
     * @return true 在Fragment中已处理， false
     */
    public boolean onBackPressed() {
        return false;
    }

}
