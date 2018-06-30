package com.kefan.blackstone.model;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午3:59
 */
public class NoteCount {

    /**
     * 物种类型
     */
    private String speciesType;

    private Integer count;

    public NoteCount() {
    }

    public NoteCount(String speciesType, Integer count) {
        this.speciesType = speciesType;
        this.count = count;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
