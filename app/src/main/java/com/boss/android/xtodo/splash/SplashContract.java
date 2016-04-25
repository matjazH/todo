package com.boss.android.xtodo.splash;

import android.graphics.Bitmap;

import com.boss.android.xtodo.base.BasePresenter;
import com.boss.android.xtodo.base.BaseView;

/**
 * Created by boss on 2016/4/24.
 */
public interface SplashContract {

    interface View extends BaseView<Presenter> {
        /**
         * @param leftTime 剩余显示时间, 秒
         */
        void showSplashTimeTips(int leftTime);

        /**
         * 用于显示背景
         * @param bitmap
         */
        void showSplashBackground(Bitmap bitmap);

        void showNextActivity();
    }

    interface Presenter extends BasePresenter<View> {

        void loadBackgroundResource();

        void stopSplash();

    }
}
