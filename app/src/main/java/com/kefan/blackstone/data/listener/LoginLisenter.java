package com.kefan.blackstone.data.listener;

import com.kefan.blackstone.vo.TokenVO;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/10 下午12:44
 */
public class LoginLisenter extends BaseResponseListener<TokenVO>{

    @Override
    protected void onFailed(int code, String message) {

    }

    @Override
    protected void onSuccess(TokenVO data) {

    }
}
