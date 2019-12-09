package com.onmobile.rbt.baseline.widget.cardswiper

import android.view.View
import com.onmobile.rbt.baseline.widget.cardswiper.StackLayoutManager.ScrollOrientation

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */
class DefaultAnimation(scrollOrientation: ScrollOrientation, visibleCount: Int) : StackAnimation(scrollOrientation, visibleCount) {

    private var mScale = 0.95f
    private var mOutScale = 1.0f
    private var mOutRotation: Int

    init {
        mOutRotation = when (scrollOrientation) {
            ScrollOrientation.LEFT_TO_RIGHT, ScrollOrientation.RIGHT_TO_LEFT -> 10
            else -> 0
        }
    }

    /**
     * Set the item scale.
     * @param scale scaling, default is 0.95f.
     */
    fun setItemScaleRate(scale: Float) {
        mScale = scale
    }

    /**
     * Get the item scale.
     * @return item scale, the default is 0.95f.
     */
    fun getItemScaleRate(): Float {
        return mScale
    }

    /**
     * Set the zoom ratio of the itemView when leaving the screen.
     * @param scale scaling, default is 0.8f.
     */
    fun setOutScale(scale: Float) {
        mOutScale = scale
    }

    /**
     * Get the zoom ratio of the itemView when it leaves the screen.
     * @return scaling, default is 0.8f.
     */
    fun getOutScale(): Float {
        return mOutScale
    }

    /**
      * Set the rotation angle of the itemView when it leaves the screen.
      * @param rotation The rotation angle, the default is 30.
      */
    fun setOutRotation(rotation: Int) {
        mOutRotation = rotation
    }

    /**
     * Get the rotation angle of the itemView when leaving the screen
     * @return rotation angle, the default is 30
     */
    fun getOutRotation(): Int {
        return mOutRotation
    }

    override fun doAnimation(firstMovePercent: Float, itemView: View, position: Int) {
        val scale: Float
        var alpha = 1.0f
        val rotation: Float
        if (position == 0) {
            scale = 1 - ((1 - mOutScale) * firstMovePercent)
            rotation = mOutRotation * firstMovePercent
        } else {
            val minScale = (Math.pow(mScale.toDouble(), position.toDouble())).toFloat()
            val maxScale = (Math.pow(mScale.toDouble(), (position - 1).toDouble())).toFloat()
            scale = minScale + (maxScale - minScale) * firstMovePercent
            // Only change the transparency of the last item
            if (position == mVisibleCount) {
                alpha = firstMovePercent
            }
            rotation = 0f
        }

        setItemPivotXY(mScrollOrientation, itemView)
        rotationFirstVisibleItem(mScrollOrientation, itemView, rotation)
        itemView.scaleX = scale
        itemView.scaleY = scale
        itemView.alpha = alpha
    }

    private fun setItemPivotXY(scrollOrientation: ScrollOrientation, view: View) {
        when (scrollOrientation) {
            ScrollOrientation.RIGHT_TO_LEFT -> {
                view.pivotX = view.measuredWidth.toFloat()
                view.pivotY = view.measuredHeight.toFloat() / 2
            }
            ScrollOrientation.LEFT_TO_RIGHT -> {
                view.pivotX = 0f
                view.pivotY = view.measuredHeight.toFloat() / 2
            }
            ScrollOrientation.BOTTOM_TO_TOP -> {
                view.pivotX = view.measuredWidth.toFloat() / 2
                view.pivotY = view.measuredHeight.toFloat()
            }
            ScrollOrientation.TOP_TO_BOTTOM -> {
                view.pivotX = view.measuredWidth.toFloat() / 2
                view.pivotY = 0f
            }
        }
    }

    private fun rotationFirstVisibleItem(scrollOrientation: ScrollOrientation, view: View, rotation: Float) {
        when (scrollOrientation) {
            ScrollOrientation.RIGHT_TO_LEFT -> view.rotationY = rotation
            ScrollOrientation.LEFT_TO_RIGHT -> view.rotationY = -rotation
            ScrollOrientation.BOTTOM_TO_TOP -> view.rotationX = -rotation
            ScrollOrientation.TOP_TO_BOTTOM -> view.rotationX = rotation
        }
    }
}