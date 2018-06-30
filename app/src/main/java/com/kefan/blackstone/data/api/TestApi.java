package com.kefan.blackstone.data.api;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.vo.MainVo;
import com.kefan.blackstone.vo.TestVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:06
 */
public class TestApi extends BaseApi {

    public TestApi(Context context) {
        super(context);
    }

    public JsonObjectRequest nextQuestion(String token, BaseResponseListener<TestVO> listener, Response.ErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            //设置token
            Map<String, String> header = new HashMap<>();
            header.put("token", token);

            return baseJsonObjectGetRequestWithHeader(context, APIManager.NETX_QUESTION, listener, errorListener, header);

        } else {

            return baseJsonObjectGetRequest(context,APIManager.NETX_QUESTION,listener,errorListener);
        }
    }

    public JsonObjectRequest gameSubmit(String token,boolean isTrue, BaseResponseListener listener, BaseErrorListener errorListener) {

        if (token != null && token.length() > 0) {

            //设置token
            Map<String, String> header = new HashMap<>();
            header.put("token", token);

            if (isTrue) {
                return baseJsonObjectGetRequestWithHeader(context, APIManager.GAME_SUBMIT + "?correct=1", listener, errorListener, header);
            } else {
                return baseJsonObjectGetRequestWithHeader(context, APIManager.GAME_SUBMIT, listener, errorListener, header);
            }



        }
        return null;

    }
}
