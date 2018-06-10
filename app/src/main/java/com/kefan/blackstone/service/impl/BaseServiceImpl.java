package com.kefan.blackstone.service.impl;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.BlackStoneApplication;
import com.kefan.blackstone.service.BaseService;
import com.kefan.blackstone.util.ToastUtil;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:21
 */
public class BaseServiceImpl implements BaseService{


    protected Context context = BlackStoneApplication.getContext();

    protected RequestQueue requestQueue = BlackStoneApplication.getRequestQueue();

    /**
     * 判断请求是否创建成功
     * @param context
     * @param objectRequest
     * @return
     */
    @Override
    public boolean checkRequest(Context context, JsonObjectRequest objectRequest) {

        if (objectRequest == null) {

            ToastUtil.showCreateRequestError(context);

            return false;
        }

        return true;

    }
}
