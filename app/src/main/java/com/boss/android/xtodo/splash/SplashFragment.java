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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/24.
 */
public class SplashFragment extends BaseFragment implements SplashContract.View {

    private Unbinder mUnbinder;

    private SplashContract.Presenter mPresenter;

    @BindView(R.id.iv_splash_background)
    ImageView mSplashBackground;

    @BindView(R.id.iv_splash_main)
    ImageView mSplashMain;

    @BindView(R.id.tv_splash_time_tips)
    TextView mSplashTimeTips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash, container, false);

        mUnbinder = ButterKnife.bind(this, root);
        mPresenter.loadBackgroundResource();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.tv_splash_time_tips)
    public void stopSplash() {
        mPresenter.stopSplash();
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
