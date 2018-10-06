package com.codangcoding.kmovieapp.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class VerticalLinearLayoutOffsetItemDecoration(
    private val offset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) % 2 == 0)
            outRect.set(0, 0, 0, 0)
        else
            outRect.set(0, offset, 0, offset)
    }
}