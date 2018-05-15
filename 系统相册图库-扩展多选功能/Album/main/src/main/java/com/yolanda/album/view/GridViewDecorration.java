/*
 * AUTHOR：YOLANDA
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yolanda.album.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p>RecyclerView的GridLayoutManager的分割线。</p>
 * Created in Aug 27, 2015 5:36:08 PM
 *
 * @author YOLANDA
 */
public class GridViewDecorration extends RecyclerView.ItemDecoration {

    private int space;

    public GridViewDecorration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        int index = parent.getChildAdapterPosition(view);
        if (index < 2) {
            outRect.top = space;
        }
    }

}
