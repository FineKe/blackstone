package com.kefan.blackstone.data.api;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.vo.MainVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fine
 */
public class HomeApi extends BaseApi{


    public HomeApi(Context context) {
        super(context);
    }

    /**
     * 首页 数据获取
     * @param token
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest main(String token, BaseResponseListener<MainVo> listener, Response.ErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            //设置token
            Map<String, String> header = new HashMap<>();
            header.put("token", token);

            return baseJsonObjectGetRequestWithHeader(context, APIManager.HOME_MAIN_URL, listener, errorListener, header);

        } else {

            return baseJsonObjectGetRequest(context,APIManager.HOME_MAIN_URL,listener,errorListener);
        }
    }
}
