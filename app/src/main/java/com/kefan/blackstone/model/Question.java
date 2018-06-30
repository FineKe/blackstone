package com.kefan.blackstone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Question {


    /**
     * 图片
     */
    private String img;

    private Optional A;

    private Optional B;

    private Optional C;

    private Optional D;
}
