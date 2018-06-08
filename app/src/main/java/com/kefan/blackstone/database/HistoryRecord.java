package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

/**
 * Created by MY SHIP on 2017/6/25.
 */

public class HistoryRecord extends DataSupport {
    private int singal;
    private String speciesType;
    private String chineseName;

    public int getSingal() {
        return singal;
    }

    public void setSingal(int singal) {
        this.singal = singal;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
}
