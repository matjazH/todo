package com.boss.android.xtodo.base;

/**
 * Created by boss on 2016/4/16.
 */
public interface BasePresenter<T> {

    void setView(T view);

    void start();

}
