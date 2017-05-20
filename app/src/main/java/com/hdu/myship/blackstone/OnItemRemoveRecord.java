package com.hdu.myship.blackstone;

import android.view.View;

/**
 * Created by MY SHIP on 2017/5/20.
 */

public interface OnItemRemoveRecord {
    /**
     * item点击回调
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position);
}
