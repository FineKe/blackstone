package com.kefan.blackstone.model;


/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/6 下午4:42
 */

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


    public User() {
    }

    public User(Long id, String mobile, String studentId, String name, String gender, String token, String password, Long expireAt, String avatar, boolean islogined) {
        this.id = id;
        this.mobile = mobile;
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.token = token;
        this.password = password;
        this.expireAt = expireAt;
        this.avatar = avatar;
        this.islogined = islogined;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIslogined() {
        return islogined;
    }

    public void setIslogined(boolean islogined) {
        this.islogined = islogined;
    }
}
