package com.kefan.blackstone.data.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:49
 */
public abstract class BaseErrorListener implements Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        volleyError.printStackTrace();

        onError(volleyError);

    }

    protected void onError(VolleyError volleyError) {

    }


}
