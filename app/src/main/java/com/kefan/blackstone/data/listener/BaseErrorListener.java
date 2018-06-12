package com.kefan.blackstone.data.listener;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.kefan.blackstone.util.ToastUtil;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:49
 */
public abstract class BaseErrorListener implements Response.ErrorListener {

    private Context context;

    public BaseErrorListener(Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        volleyError.printStackTrace();

        onError(volleyError);

    }

    protected void onError(VolleyError volleyError) {
        if (volleyError instanceof TimeoutError) {
            ToastUtil.showToast(context,"网络超时");
        }
    }


}
