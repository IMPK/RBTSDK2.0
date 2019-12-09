package com.onmobile.rbt.baseline.listener;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by Shahbaz Akhtar on 02/04/2019.
 *
 * @author Shahbaz Akhtar
 */

public interface LifeCycleCallback {
    default void onLifeCycleCreate(@Nullable Bundle savedInstanceState) {

    }

    void onLifeCycleStart();

    default void onLifeCycleResume() {

    }

    default void onLifeCyclePause() {

    }

    void onLifeCycleStop();

    default void onLifeCycleDestroy(){

    }
}
