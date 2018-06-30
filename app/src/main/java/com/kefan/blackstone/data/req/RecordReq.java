package com.kefan.blackstone.data.req;

import com.kefan.blackstone.database.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/22 下午9:40
 */
public class RecordReq {

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
     * 观察伙伴
     */
    private String observationPalName;

    /**
     * 笔记列表
     */
    private List<Note> notes=new ArrayList<Note>();



    public RecordReq() {
    }

    public RecordReq(Long time, Double lat, Double lon, Boolean addToObservedList, String observationPalName, List<Note> notes) {
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.addToObservedList = addToObservedList;
        this.observationPalName = observationPalName;
        this.notes = notes;
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

    public String getObservationPalName() {
        return observationPalName;
    }

    public void setObservationPalName(String observationPalName) {
        this.observationPalName = observationPalName;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
