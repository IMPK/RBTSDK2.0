package com.onmobile.rbt.baseline.widget.cardswiper

import android.view.View
import com.onmobile.rbt.baseline.widget.cardswiper.StackLayoutManager.ScrollOrientation

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */
abstract class StackAnimation(scrollOrientation: ScrollOrientation, visibleCount: Int) {

    protected val mScrollOrientation = scrollOrientation
    protected var mVisibleCount = visibleCount

    internal fun setVisibleCount(visibleCount: Int) {
        mVisibleCount = visibleCount
    }

    /**
    * External callback for animation.
    * @param firstMovePercent The percentage of the first visible item movement, firstMovePercent is infinitely close to 1. when it is about to completely move out of the screen.
    * @param itemView The current itemView.
    * @param position The current position of the itemView, position = 0 until visibleCount.
    */
    abstract fun doAnimation(firstMovePercent: Float, itemView: View, position: Int)
}