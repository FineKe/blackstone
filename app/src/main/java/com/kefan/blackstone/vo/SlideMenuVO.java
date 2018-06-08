package com.kefan.blackstone.vo;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/5 上午12:09
 */
public final class SlideMenuVO {

    private int drawable;

    private String title;

    public SlideMenuVO() {
    }

    public SlideMenuVO(int drawable, String title) {
        this.drawable = drawable;
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
