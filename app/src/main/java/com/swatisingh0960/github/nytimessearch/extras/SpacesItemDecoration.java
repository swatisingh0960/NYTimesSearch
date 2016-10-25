package com.swatisingh0960.github.nytimessearch.extras;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Swati on 10/23/2016.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    private final int mSpace;
    public SpacesItemDecoration(int space) {
        this.mSpace=space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        /* Rect holds four integer coordinates for a rectangle. The rectangle is
 * represented by the coordinates of its 4 edges (left, top, right bottom).
 * These fields can be accessed directly. Use width() and height() to retrieve
 * the rectangle's width and height. Note: most methods do not check to see that
 * the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
 * <p>
 * Note that the right and bottom coordinates are exclusive. This means a Rect
 * being drawn untransformed onto a {@link android.graphics.Canvas} will draw
 * into the column and row described by its left and top coordinates, but not
 * those of its bottom and right.
 */
        outRect.left = mSpace;
        outRect.right= mSpace;
        outRect.bottom=mSpace;
        // Add top margin only for  the first time to avoid double space between items
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = mSpace;
        }
    }
}
