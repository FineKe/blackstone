package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note: 小测试记录
 * @author: fine
 * @time: 2018/6/24 下午4:13
 */
public class TestRecord extends DataSupport{

    /**
     * 记录id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 时间
     */
    private Long time;

    /**
     * 分数
     */
    private Integer score;

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
