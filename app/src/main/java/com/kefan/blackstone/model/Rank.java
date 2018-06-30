package com.kefan.blackstone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/24 下午2:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rank {


    /**
     * id : null
     * user : {"id":16,"name":"王二","avatar":"http://img.blackstone.ebirdnote.cn/FsX3JDFkkmhKwzfB3j8AdfNWLFQm"}
     * totalCount : 37
     * ratio : 0.3514
     * rank : 1
     */

    private String id;
    private UserBean user;
    private int totalCount;
    private double ratio;
    private int rank;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserBean {
        /**
         * id : 16
         * name : 王二
         * avatar : http://img.blackstone.ebirdnote.cn/FsX3JDFkkmhKwzfB3j8AdfNWLFQm
         */

        private int id;
        private String name;
        private String avatar;
    }
}
