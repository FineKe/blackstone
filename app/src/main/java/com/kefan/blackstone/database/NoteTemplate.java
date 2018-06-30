package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/17 下午3:40
 */
public class NoteTemplate extends DataSupport{


    /**
     * 笔记模板id
     */
    private Long id;
    /**
     * 物种id
     */
    private Long speciesId;

    /**
     * 物种名
     */
    private String speciesName;

    /**
     * 是否勾选
     */
    private boolean isChekced;

    /**
     * 物种类别
     */
    private String speciesType;

    /**
     * 笔记
     */
    private String remark;

    /**
     * 科
     */
    private String family;


    public NoteTemplate(Long speciesId, String speciesName, boolean isChekced, String speciesType, String remark,String family) {

        this.speciesId = speciesId;
        this.speciesName = speciesName;
        this.isChekced = isChekced;
        this.speciesType = speciesType;
        this.remark = remark;
        this.family=family;
    }

    public NoteTemplate() {
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public boolean isChekced() {
        return isChekced;
    }

    public void setChekced(boolean chekced) {
        isChekced = chekced;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public boolean isMarked() {

        if (remark == null || remark.length() <= 0) {
            return false;
        }
        return true;

    }
}
