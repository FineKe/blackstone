package com.kefan.blackstone.vo;

import com.kefan.blackstone.model.NoteCount;

import java.util.List;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午3:53
 */
public class RecordVo {

    /**
     * 记录id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 时间
     */
    private Long time;

    /**
     * 观察伙伴
     */
    private String observationPalName;

    /**
     * 记录；列表
     */
    private List<NoteCount> noteCounts;

    public RecordVo() {
    }

    public RecordVo(Long id, Long userId, String userNickname, Long time, String observationPalName, List<NoteCount> noteCounts) {
        this.id = id;
        this.userId = userId;
        this.userNickname = userNickname;
        this.time = time;
        this.observationPalName = observationPalName;
        this.noteCounts = noteCounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getObservationPalName() {
        return observationPalName;
    }

    public void setObservationPalName(String observationPalName) {
        this.observationPalName = observationPalName;
    }

    public List<NoteCount> getNoteCounts() {
        return noteCounts;
    }

    public void setNoteCounts(List<NoteCount> noteCounts) {
        this.noteCounts = noteCounts;
    }

    @Override
    public String toString() {
        return "RecordVo{" +
                "id=" + id +
                ", userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", time=" + time +
                ", observationPalName='" + observationPalName + '\'' +
                ", noteCounts=" + noteCounts +
                '}';
    }
}
