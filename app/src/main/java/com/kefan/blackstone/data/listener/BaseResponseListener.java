package com.kefan.blackstone.data.listener;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kefan.blackstone.data.response.BaseResponse;
import com.kefan.blackstone.util.GsonUtil;
import com.kefan.blackstone.vo.MainVo;
import com.kefan.blackstone.vo.TokenVO;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:35
 */
public  abstract class BaseResponseListener<T> implements Response.Listener<JSONObject> {

    private Class clazz;

    public BaseResponseListener(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

        BaseResponse<T> baseResponse = GsonUtil.fromJson(jsonObject.toString(),clazz);

        if (baseResponse.isSuccess()) {

            onSuccess(baseResponse.getData());

        } else {

            onFailed(baseResponse.getCode(),baseResponse.getMessage());

        }

        Logger.d(baseResponse);
    }


    protected void onFailed(int code, String message) {

    }

    protected void  onSuccess(T data) {

    }
}
