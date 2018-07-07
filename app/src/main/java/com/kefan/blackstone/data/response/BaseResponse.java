package com.kefan.blackstone.data.response;

import com.kefan.blackstone.common.CommonConstant;

/**
 * @note: 基础响应bean
 * @author: 柯帆
 * @date: 2018/6/9 下午11:18
 */


public class BaseResponse <T>{

    /**
     * 响应码
     */
    private int code;


    /**
     * 数据体
     */
    private T data;


    /**
     * message
     */
    private String message;

    /**
     * 判断请求是否成功
     * @return
     */
    public boolean isSuccess() {

        return code== CommonConstant.REQUEST_SUCCESS_CODE;

    }

    public BaseResponse() {
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
