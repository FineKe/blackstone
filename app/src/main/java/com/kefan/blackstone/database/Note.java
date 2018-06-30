package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

/**
 * @note: 笔记
 * @author: fine
 * @time: 2018/6/17 下午3:23
 */
public class Note extends DataSupport{

    /**
     * 笔记id
     */
    private Long id;

    /**
     * 笔记服务器端id
     */
    private Long netId;

    /**
     * 物种id
     */
    private Long speciesId;

    /**
     * 描述
     */
    private String remark;


    /**
     * 五种类型
     */
    private String speciesType;

    /**
     * 科
     */
    private String family;

    /**
     * 中文名
     */
    private String chineseName;

    public Note() {
    }

    public Note(Long speciesId,Long netId, String remark,String speciesType,String family,String chineseName) {
        this.netId=netId;
        this.speciesId = speciesId;
        this.remark = remark;
        this.speciesType=speciesType;
        this.family=family;
        this.chineseName=chineseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public Long getNetId() {
        return netId;
    }

    public void setNetId(Long netId) {
        this.netId = netId;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", netId=" + netId +
                ", speciesId=" + speciesId +
                ", remark='" + remark + '\'' +
                ", speciesType='" + speciesType + '\'' +
                ", family='" + family + '\'' +
                ", chineseName='" + chineseName + '\'' +
                '}';
    }
}
