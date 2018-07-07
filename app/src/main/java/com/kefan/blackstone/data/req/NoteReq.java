package com.kefan.blackstone.data.req;



/**
 * @note:
 * @author: fine
 * @time: 2018/6/30 上午12:03
 */

public class NoteReq {

    /**
     * 物种id
     */
    private Long speciesId;

    /**
     * 描述
     */
    private String remark;

    public NoteReq() {
    }

    public NoteReq(Long speciesId, String remark) {
        this.speciesId = speciesId;
        this.remark = remark;
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
}
