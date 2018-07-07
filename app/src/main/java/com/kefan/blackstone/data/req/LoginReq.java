package com.kefan.blackstone.data.req;

/**
 * @note: 登录请求体
 * @author: 柯帆
 * @date: 2018/6/10 上午11:53
 */
public class LoginReq {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String pwd;

    public LoginReq() {
    }

    public LoginReq(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
