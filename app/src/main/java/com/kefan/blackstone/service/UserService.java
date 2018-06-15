package com.kefan.blackstone.service;

import com.android.volley.Response;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.vo.TokenVO;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:09
 */
public interface UserService {

    /**
     * 登录
     * @param username
     * @param pwd
     * @param listener
     * @param errorListener
     */
    public void login(String username, String pwd, BaseResponseListener<TokenVO> listener,
                      Response.ErrorListener errorListener);


    public void saveUser(TokenVO tokenVO, String pwd);


    /**
     * 登出
     */
    public void logout();

    /**
     * 获取token
     * @return
     */
    public String getToken();

    /**
     * 头像
     * @return
     */
    public String icon();
}
