package com.kefan.blackstone.ui.activity;

import android.view.View;

/**
 * Created by MY SHIP on 2017/5/20.
 */

public interface OnItemRemoveRecordListener {
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


    /**
     * 上传
     * @param position
     */
    void onUploadClick(int position);
}
