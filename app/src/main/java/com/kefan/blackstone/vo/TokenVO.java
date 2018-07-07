package com.kefan.blackstone.vo;



/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:27
 */

public class TokenVO {

    /**
     * uservo
     */
    private UserVO user;

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private long expireAt;


    public TokenVO(UserVO user, String token, long expireAt) {
        this.user = user;
        this.token = token;
        this.expireAt = expireAt;
    }

    public TokenVO() {
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }


}
