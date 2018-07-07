package com.kefan.blackstone.model;


/**
 * @note:
 * @author: fine
 * @time: 2018/6/24 下午2:19
 */

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Rank() {
    }

    public Rank(String id, UserBean user, int totalCount, double ratio, int rank) {
        this.id = id;
        this.user = user;
        this.totalCount = totalCount;
        this.ratio = ratio;
        this.rank = rank;
    }

    public static class UserBean {
        /**
         * id : 16
         * name : 王二
         * avatar : http://img.blackstone.ebirdnote.cn/FsX3JDFkkmhKwzfB3j8AdfNWLFQm
         */

        private int id;
        private String name;
        private String avatar;

        public UserBean() {
        }

        public UserBean(int id, String name, String avatar) {
            this.id = id;
            this.name = name;
            this.avatar = avatar;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
