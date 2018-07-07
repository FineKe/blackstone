package com.kefan.blackstone.model;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:18
 */

public class Question {


    /**
     * 图片
     */
    private String img;

    private Optional A;

    private Optional B;

    private Optional C;

    private Optional D;

    public Question() {
    }

    public Question(String img, Optional a, Optional b, Optional c, Optional d) {
        this.img = img;
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Optional getA() {
        return A;
    }

    public void setA(Optional a) {
        A = a;
    }

    public Optional getB() {
        return B;
    }

    public void setB(Optional b) {
        B = b;
    }

    public Optional getC() {
        return C;
    }

    public void setC(Optional c) {
        C = c;
    }

    public Optional getD() {
        return D;
    }

    public void setD(Optional d) {
        D = d;
    }
}
