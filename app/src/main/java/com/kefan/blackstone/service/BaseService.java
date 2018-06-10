package com.kefan.blackstone.service;

import android.content.Context;

import com.android.volley.toolbox.JsonObjectRequest;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:20
 */
public interface BaseService {

    public boolean checkRequest(Context context, JsonObjectRequest objectRequest);
}
