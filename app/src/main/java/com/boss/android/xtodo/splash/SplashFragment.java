package com.boss.android.xtodo.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boss.android.xtodo.R;
import com.boss.android.xtodo.base.BaseFragment;
import com.boss.android.xtodo.tasks.TasksActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/24.
 */
public class SplashFragment extends BaseFragment implements SplashContract.View {

    private SplashContract.Presenter mPresenter;

    private ImageView mSplashBackground;

    private ImageView mSplashMain;

    private TextView mSplashTimeTips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash, container, false);
        mSplashBackground = (ImageView) root.findViewById(R.id.iv_splash_background);
        mSplashMain = (ImageView) root.findViewById(R.id.iv_splash_main);
        mSplashTimeTips = (TextView) root.findViewById(R.id.tv_splash_time_tips);
        mSplashTimeTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.stopSplash();
            }
        });
        mPresenter.loadBackgroundResource();
        return root;
    }

    @Override
    public void setPresenter(@NonNull SplashContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean onBackPressed() {
        Snackbar.make(getView(), "on back pressed in " + getClass().getName(), Snackbar.LENGTH_LONG).show();
        return super.onBackPressed();
    }

    @Override
    public void showSplashTimeTips(int leftTime) {
        mSplashTimeTips.setText(getString(R.string.splash_time_tips, leftTime));
    }

    @Override
    public void showSplashBackground(Bitmap bitmap) {
        if (bitmap != null) {
            mSplashBackground.setImageBitmap(bitmap);
        } else {
            mSplashBackground.setImageResource(R.drawable.bg_splash_default);
        }
    }

    @Override
    public void showNextActivity() {
        Intent intent = new Intent(getActivity(), TasksActivity.class);
        startActivity(intent);
    }
}
