package com.kefan.blackstone.data.response;

import com.kefan.blackstone.common.CommonConstant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note: 基础响应bean
 * @author: 柯帆
 * @date: 2018/6/9 下午11:18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
