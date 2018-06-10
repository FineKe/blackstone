package com.kefan.blackstone.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
