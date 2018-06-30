package com.kefan.blackstone.service;

import com.android.volley.Response;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.vo.TestVO;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:12
 */
public interface TestService {

    /**
     * 题目
     *
     * @param token
     * @param listener
     * @param errorListener
     */
    public void nextQuestion(String token, BaseResponseListener<TestVO> listener, Response.ErrorListener errorListener);

    /**
     * 分数提交
     * @param token
     * @param listener
     * @param isTrue 是否正确
     * @param errorListener
     */
    public void gameSumbit(String token,boolean isTrue, BaseResponseListener listener, BaseErrorListener errorListener);
}