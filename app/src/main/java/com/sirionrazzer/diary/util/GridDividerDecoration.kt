package com.sirionrazzer.diary.util

/**
 * Source:
 * https://github.com/devunwired/recyclerview-playground/blob/master/app/src/main/java/com/example/android/recyclerplayground/GridDividerDecoration.java
 * under MIT license, converted to kotlin using Android Studio
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View

import androidx.recyclerview.widget.RecyclerView

/**
 * ItemDecoration implementation that applies and inset margin
 * around each child of the RecyclerView. It also draws item dividers
 * that are expected from a vertical list implementation, such as
 * ListView.
 */
class GridDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable?
    private val mInsets: Int

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        val scale = context.resources.displayMetrics.density
        mInsets = (2 * scale + 0.5f).toInt()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
        drawHorizontal(c, parent)
    }

    /** Draw dividers at each expected grid interval.  */
    fun drawVertical(c: Canvas, parent: RecyclerView) {
        if (parent.childCount == 0) {
            return
        }

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.left - params.leftMargin - mInsets
            val right = child.right + params.rightMargin + mInsets
            val top = child.bottom + params.bottomMargin + mInsets
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    /** Draw dividers to the right of each child view.  */
    fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.right + params.rightMargin + mInsets
            val right = left + mDivider!!.intrinsicWidth
            val top = child.top - params.topMargin - mInsets
            val bottom = child.bottom + params.bottomMargin + mInsets
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // We can supply forced insets for each item view here in the Rect
        outRect.set(mInsets, mInsets, mInsets, mInsets)
    }

    companion object {

        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}