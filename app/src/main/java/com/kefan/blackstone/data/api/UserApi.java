package com.kefan.blackstone.data.api;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.LoginReq;
import com.kefan.blackstone.util.JsonUtil;
import com.kefan.blackstone.vo.TokenVO;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:38
 */
public final class UserApi extends BaseApi{

    private Context context;

    public UserApi(Context context) {
        this.context = context;
    }

    public JsonObjectRequest login(LoginReq loginReq, BaseResponseListener<TokenVO> listener, Response.ErrorListener errorListener) {

        return baseJsonObjectPostRequest(context, APIManager.LOGIN_URL,JsonUtil.convertObject(loginReq),listener,errorListener);

    }
}
