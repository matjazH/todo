package com.boss.android.xtodo.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;

import com.boss.android.xtodo.base.BaseFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/16.
 */
public class ActivityUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, fragment, fragment.getClass().getName());
        // 将 fragment id 作为 stack name 传入
        fragmentTransaction.addToBackStack(String.valueOf(fragment.getId()));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static boolean onActivityBackPressed(@NonNull FragmentManager fragmentManager) {
        checkNotNull(fragmentManager);
        int entryCount = fragmentManager.getBackStackEntryCount();
        if(entryCount > 0){
            int lastEntryIndex = entryCount - 1;
            FragmentManager.BackStackEntry topEntry = fragmentManager.getBackStackEntryAt(lastEntryIndex);
            Fragment fragment = fragmentManager.findFragmentById(Integer.valueOf(topEntry.getName()));
            if (fragment != null && fragment instanceof BaseFragment) {
                boolean processBackPressed = ((BaseFragment) fragment).onBackPressed();
                if (!processBackPressed) {
                    fragmentManager.popBackStack();
                }

                if (lastEntryIndex == 0) {
                    return processBackPressed;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
