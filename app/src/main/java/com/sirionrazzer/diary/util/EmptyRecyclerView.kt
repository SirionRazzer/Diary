package com.sirionrazzer.diary.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Source: http://akbaribrahim.com/empty-view-for-androids-recyclerview/
 */
class EmptyRecyclerView : RecyclerView {

    var emptyView: View? = null

    private val observer = object : AdapterDataObserver() {
        override fun onChanged() = checkIfEmpty()

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkIfEmpty()

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkIfEmpty()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    )

    fun checkIfEmpty() {
        val isEmpty = adapter?.let { it.itemCount == 0 } ?: return
        emptyView?.let {
            it.visibility = if (isEmpty) View.VISIBLE else View.GONE
            visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        getAdapter()?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)
        checkIfEmpty()
    }
}
