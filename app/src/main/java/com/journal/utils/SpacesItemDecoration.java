package com.journal.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类名称：com.exing.utils
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.27
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
//        if(parent.getChildAdapterPosition(view)==0){
//            outRect.top=space;
//        }
    }
}
