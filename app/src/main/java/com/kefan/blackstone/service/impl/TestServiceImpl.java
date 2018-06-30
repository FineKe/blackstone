package com.kefan.blackstone.service.impl;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.data.api.TestApi;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.service.TestService;
import com.kefan.blackstone.vo.TestVO;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:14
 */
public class TestServiceImpl extends BaseServiceImpl implements TestService {

    private TestApi testApi;

    public TestServiceImpl() {

        testApi = new TestApi(context);

    }

    @Override
    public void nextQuestion(String token, BaseResponseListener<TestVO> listener, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = testApi.nextQuestion(token, listener, errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }

    }

    @Override
    public void gameSumbit(String token, boolean isTrue,BaseResponseListener listener, BaseErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = testApi.gameSubmit(token, isTrue,listener, errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }
    }
}
