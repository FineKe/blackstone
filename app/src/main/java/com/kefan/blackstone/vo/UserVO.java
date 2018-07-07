package com.kefan.blackstone.vo;


/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:29
 */

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

    public UserVO() {
    }

    public UserVO(int id, String mobile, String studentId, String name, String gender, String avatar) {
        this.id = id;
        this.mobile = mobile;
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
