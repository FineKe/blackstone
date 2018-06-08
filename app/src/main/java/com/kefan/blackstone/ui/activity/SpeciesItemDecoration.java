package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MY SHIP on 2017/3/30.
 */

public class SpeciesItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] attrs = {android.R.attr.listDivider};
    private Drawable drawable;

    public SpeciesItemDecoration(Context context) {
        TypedArray a = context.obtainStyledAttributes(attrs);//获取系统提供的分隔条对象
        drawable = a.getDrawable(0);
        a.recycle();//回收TypedArray所占用的空间
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft() + 300;//获取列表项距离左边缘的距离；
        int right = parent.getWidth() - parent.getPaddingRight();

        int child = parent.getChildCount();//获取列表总数

        for (int i = 0; i < child; i++) {
            View view = parent.getChildAt(i);//获取当前项
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();//获取当前列表项的布局参数信息
            int top = view.getBottom() + params.bottomMargin;//左上角的纵坐标
            int bottom = top + drawable.getIntrinsicHeight();//右下角纵坐标
            drawable.setBounds(left, top, right, bottom);//设置分割线的位置
            drawable.draw(c);
        }
    }

}
