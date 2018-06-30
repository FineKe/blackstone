package com.kefan.blackstone.data.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/30 上午12:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteReq {

    /**
     * 物种id
     */
    private Long speciesId;

    /**
     * 描述
     */
    private String remark;
}
