package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

/**
 * Created by MY SHIP on 2017/5/17.
 * javaBean 物种类别
 */

public class SpeciesClasses extends DataSupport {
    private int flag;//用于区分是否处于同一header下
    private String title;
    private String classesName;//类别名称
    private String mainPhoto;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }
}
