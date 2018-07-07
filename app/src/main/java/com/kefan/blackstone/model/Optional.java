package com.kefan.blackstone.model;

/**
 * @note: 选项
 * @author: fine
 * @time: 2018/6/23 下午6:19
 */

public class Optional {

    private String name;

    private String img;

    private boolean isTrue;

    public Optional() {
    }

    public Optional(String name, String img, boolean isTrue) {
        this.name = name;
        this.img = img;
        this.isTrue = isTrue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}
