package com.kefan.blackstone.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.kefan.blackstone.BlackStoneApplication;
import com.kefan.blackstone.ui.activity.UpdateToken;

import org.json.JSONObject;

import java.util.Map;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 上午12:03
 */
public class VolleyRequest {

    /**
     * 不带token 的请求
     * @param method
     * @param url
     * @param data
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest baseRequest(int method, String url, JSONObject data, Response.Listener listener, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(method,url,data,listener,errorListener);

        return jsonRequest;
    }

    /**
     * 带token 的 请求
     * @param method 方法
     * @param url
     * @param data
     * @param listener
     * @param errorListener
     * @param header
     * @return
     */
    public static JsonObjectRequest baseRequestWithHeader(int method, String url, JSONObject data, Response.Listener listener, Response.ErrorListener errorListener, final Map<String,String> header) {


        if (!UserSharePreferenceUtil.tokenisExpired(BlackStoneApplication.getContext())) {

            new UpdateToken(BlackStoneApplication.getContext()).updateToken();

            header.put("token",UserSharePreferenceUtil.getUser(BlackStoneApplication.getContext()).getToken());
        }



        JsonObjectRequest jsonRequest = new JsonObjectRequest(method,url,data,listener,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return header;
            }
        };
        return jsonRequest;

    }




}
