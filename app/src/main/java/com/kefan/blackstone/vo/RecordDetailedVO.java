package com.kefan.blackstone.vo;

import java.util.List;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/27 下午10:47
 */
public class RecordDetailedVO {


    /**
     * id : 325
     * userId : 16
     * userNickname : 王二
     * time : 2346174867000
     * lat : 2
     * lon : 2
     * observationPalName : adasdas
     * notes : [{"id":1234,"species":{"id":1,"chineseName":"小白鹭","englishName":"Little Egret","latinName":"Egretta garzetta","family":"鹭科","speciesType":"bird"},"remark":"423423432"}]
     */

    private Long id;
    private Long userId;
    private String userNickname;
    private Long time;
    private Double lat;
    private Double lon;
    private String observationPalName;
    private List<NotesBean> notes;


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

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getObservationPalName() {
        return observationPalName;
    }

    public void setObservationPalName(String observationPalName) {
        this.observationPalName = observationPalName;
    }

    public List<NotesBean> getNotes() {
        return notes;
    }

    public void setNotes(List<NotesBean> notes) {
        this.notes = notes;
    }

    public static class NotesBean {
        /**
         * id : 1234
         * species : {"id":1,"chineseName":"小白鹭","englishName":"Little Egret","latinName":"Egretta garzetta","family":"鹭科","speciesType":"bird"}
         * remark : 423423432
         */

        private Long id;
        private SpeciesBean species;
        private String remark;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public SpeciesBean getSpecies() {
            return species;
        }

        public void setSpecies(SpeciesBean species) {
            this.species = species;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public static class SpeciesBean {
            /**
             * id : 1
             * chineseName : 小白鹭
             * englishName : Little Egret
             * latinName : Egretta garzetta
             * family : 鹭科
             * speciesType : bird
             */

            private Long id;
            private String chineseName;
            private String speciesType;
            private String family;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getChineseName() {
                return chineseName;
            }

            public void setChineseName(String chineseName) {
                this.chineseName = chineseName;
            }

            public String getSpeciesType() {
                return speciesType;
            }

            public void setSpeciesType(String speciesType) {
                this.speciesType = speciesType;
            }

            public String getFamily() {
                return family;
            }

            public void setFamily(String family) {
                this.family = family;
            }
        }
    }
}
