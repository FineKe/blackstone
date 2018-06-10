package com.kefan.blackstone.data.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note: 登录请求体
 * @author: 柯帆
 * @date: 2018/6/10 上午11:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String pwd;
}
