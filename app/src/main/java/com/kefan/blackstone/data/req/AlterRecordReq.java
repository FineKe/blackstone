package com.kefan.blackstone.data.req;

import com.kefan.blackstone.database.Note;

import java.util.List;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午3:33
 */
public class AlterRecordReq {

    private Long id;

    private Long time;

    private List<NoteReq> notes;


    public AlterRecordReq() {
    }

    public AlterRecordReq(Long id, Long time, List<NoteReq> notes) {
        this.id = id;
        this.time = time;
        this.notes = notes;
    }

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

    public List<NoteReq> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteReq> notes) {
        this.notes = notes;
    }


}
