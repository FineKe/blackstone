package com.kefan.blackstone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/6 下午4:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    private String mobile;

    private String studentId;

    private String name;

    private String gender;

    private String token;

    private String password;

    private Long expireAt;

    private String avatar;

    private boolean islogined;


}
