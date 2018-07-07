package com.kefan.blackstone.vo;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:09
 */

public class TestVO {

    /**
     * 图片
     */
    private String img;

    /**
     *物种id
     */
    private int speciesId;

    /**
     * 物种类型
     */
    private String speciesType;

    public TestVO() {
    }

    public TestVO(String img, int speciesId, String speciesType) {
        this.img = img;
        this.speciesId = speciesId;
        this.speciesType = speciesType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }
}
