package com.boss.android.xtodo.splash;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/24.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    /**
     * 显示延迟
     */
    private final static int SPLASH_DELAY_TIMES = 3;
    /**
     * 刷新间隔
     */
    private final static int SPLASH_DELAY_INTERVAL = 1000;

    private final static int MSG_SPLASH_DELAY = 0;
    private final static int MSG_SPLASH_END = 1;

    public SplashPresenter(@NonNull SplashContract.View view) {
        setView(view);
    }

    @Override
    public void setView(@NonNull SplashContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        Message message = new Message();
        message.what = MSG_SPLASH_DELAY;
        message.arg1 = SPLASH_DELAY_TIMES;
        mHandler.sendMessage(message);
    }

    @Override
    public void loadBackgroundResource() {
        Bitmap bitmap = null;
        // TODO
        mView.showSplashBackground(bitmap);
    }

    @Override
    public void stopSplash() {
        mView.showNextActivity();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SPLASH_DELAY:
                    int delayTimes = msg.arg1;
                    mView.showSplashTimeTips(delayTimes);
                    if (delayTimes == 0) {
                        handleSplashEnd();
                    } else {
                        handleSplashTimes(delayTimes - 1);
                    }
                    break;
                case MSG_SPLASH_END:
                    stopSplash();
                    break;
            }
        }
    };

    private void handleSplashEnd(){
        Message message = new Message();
        message.what = MSG_SPLASH_END;
        mHandler.sendMessage(message);
    }

    private void handleSplashTimes(int times){
        Message message = new Message();
        message.what = MSG_SPLASH_DELAY;
        message.arg1 = times;
        mHandler.sendMessageDelayed(message, SPLASH_DELAY_INTERVAL);
    }
}
