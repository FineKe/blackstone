package com.kefan.blackstone.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    /**
     * id
     */
    private int id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 名字
     */
    private String name;


    /**
     * 性别
     */
    private String gender;

    /**
     * 头像
     */
    private String avatar;

}
