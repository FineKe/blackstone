package com.kefan.blackstone.service;

import com.android.volley.Response;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.vo.MainVo;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/15 上午12:02
 */
public interface HomeService {

    /**
     * 首页信息获取
     * @param token
     * @param listener
     * @param errorListener
     */
    public void home(String token, BaseResponseListener<MainVo> listener,Response.ErrorListener errorListener);
}
