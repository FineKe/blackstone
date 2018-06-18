package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY SHIP on 2017/5/11.
 */

public class Record extends DataSupport {

    /**
     * 记录id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 时间戳
     */
    private Long time;


    /**
     * 纬度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 加入观察列表
     */
    private Boolean addToObservedList;

    /**
     * 笔记列表
     */
    private List<Note> notes=new ArrayList<Note>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Boolean getAddToObservedList() {
        return addToObservedList;
    }

    public void setAddToObservedList(Boolean addToObservedList) {
        this.addToObservedList = addToObservedList;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
