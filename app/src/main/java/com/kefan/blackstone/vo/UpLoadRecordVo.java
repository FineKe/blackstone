package com.kefan.blackstone.vo;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/27 下午9:52
 */

public class UpLoadRecordVo {

    /**
     * 记录id
     */
    private Long id;

    public UpLoadRecordVo() {
    }

    public UpLoadRecordVo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
