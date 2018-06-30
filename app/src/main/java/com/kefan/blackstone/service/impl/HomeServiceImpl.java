package com.kefan.blackstone.service.impl;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.data.api.HomeApi;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.service.HomeService;
import com.kefan.blackstone.vo.MainVo;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/15 上午12:04
 */
public class HomeServiceImpl extends BaseServiceImpl implements HomeService {

    private HomeApi homeApi;

    public HomeServiceImpl() {

        homeApi=new HomeApi(context);

    }

    @Override
    public void home(String token, BaseResponseListener<MainVo> listener, Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest=homeApi.main(token,listener,errorListener);

        if (checkRequest(context, jsonObjectRequest)) {

            requestQueue.add(jsonObjectRequest);

        }

    }
}
