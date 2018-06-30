package com.kefan.blackstone.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/23 下午6:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestVO {

    /**
     * 图片
     */
    private String img;

    /**
     *物种id
     */
    private int speciesId;

    /**
     * 物种类型
     */
    private String speciesType;
}
