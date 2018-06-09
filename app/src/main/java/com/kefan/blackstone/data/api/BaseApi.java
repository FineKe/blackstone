package com.kefan.blackstone.data.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.common.CommonConstant;
import com.kefan.blackstone.util.NetWorkUtil;
import com.kefan.blackstone.util.VolleyRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:39
 */
public class BaseApi {

    private JsonObjectRequest baseJsonObjectRequest(Context context, int method, String url, JSONObject data, Response.Listener listener, Response.ErrorListener errorListener) {

        if (!NetWorkUtil.isConnected(context)) {
            Toast.makeText(context, CommonConstant.NETWORK_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
            return null;
        }

        return VolleyRequest.baseRequest(method, url, data, listener, errorListener);
    }


    private JsonObjectRequest baseJsonObjectRequestWithHeader(Context context, int method, String url, JSONObject data, Response.Listener listener, Response.ErrorListener errorListener, Map<String,String> header) {

        if (!NetWorkUtil.isConnected(context)) {
            Toast.makeText(context, CommonConstant.NETWORK_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
            return null;
        }

        return VolleyRequest.baseRequestWithHeader(method, url, data, listener, errorListener,header);
    }

    /**
     * post 请求
     * @param context
     * @param url
     * @param data
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest baseJsonObjectPostRequest(Context context, String url, JSONObject data, Response.Listener listener, Response.ErrorListener errorListener) {
        return baseJsonObjectRequest(context, Request.Method.POST,url,data,listener,errorListener);
    }

    /**
     * get请求
     * @param context
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public JsonObjectRequest baseJsonObjectGetRequest(Context context, String url, Response.Listener listener, Response.ErrorListener errorListener) {
        return baseJsonObjectRequest(context,Request.Method.GET,url,null,listener,errorListener);
    }

    /**
     * 带header post
     * @param context
     * @param url
     * @param data
     * @param listener
     * @param errorListener
     * @param header
     * @return
     */
    public JsonObjectRequest baseJsonObjectPostRequestWithHeader(Context context, String url, JSONObject data, Response.Listener listener, Response.ErrorListener errorListener,Map<String,String> header) {
        return baseJsonObjectRequestWithHeader(context, Request.Method.POST,url,data,listener,errorListener,header);
    }

    /**
     * 带header get
     * @param context
     * @param url
     * @param listener
     * @param errorListener
     * @param header
     * @return
     */
    public JsonObjectRequest baseJsonObjectGetRequestWithHeader(Context context, String url, Response.Listener listener, Response.ErrorListener errorListener,Map<String,String> header) {
        return baseJsonObjectRequestWithHeader(context,Request.Method.GET,url,null,listener,errorListener,header);
    }


}
