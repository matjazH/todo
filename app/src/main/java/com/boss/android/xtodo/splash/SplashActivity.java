package com.boss.android.xtodo.splash;

import android.app.FragmentManager;
import android.os.Bundle;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseActivity;
import com.boss.android.xtodo.util.ActivityUtils;

/**
 * Created by boss on 2016/4/24.
 */
public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FragmentManager manager = getFragmentManager();
        SplashFragment fragment = new SplashFragment();
        SplashContract.Presenter presenter = new SplashPresenter(fragment);

        ActivityUtils.addFragmentToActivity(manager, fragment, R.id.contentFrame);
    }
}
