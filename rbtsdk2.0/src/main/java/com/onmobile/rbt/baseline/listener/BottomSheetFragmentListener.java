package com.onmobile.rbt.baseline.listener;

/**
 * Created by Shahbaz Akhtar on 16/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public interface BottomSheetFragmentListener<T, D> {
    void previous(T fragment, D data);

    void next(T fragment, D data);

    void done(T fragment, D data);

    void cancel(T fragment, D data);

    void dismissWithReason(T fragment, Object data);

    void updatePeekHeight(T fragment);
}
