package com.kefan.blackstone.listener;

import android.view.View;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/18 下午11:53
 */
public interface AlertPromptOnClickListener {

    /**
     * 左边点击
     * @param view
     */
    public void leftOnClick(View view);

    /**
     * 右边点击
     * @param view
     */
    public void rightOnClick(View view);
}
