package com.kefan.blackstone.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/14 下午11:52
 */

public class MainVo {


    /**
     * bannerImg : ["http://img.blackstone.ebirdnote.cn/FkxDfHgmEEgrIZPZJ3ldx5bEA39E","http://img.blackstone.ebirdnote.cn/FioqgxTDkddHFlnqpnNI_NsvGD1_","http://img.blackstone.ebirdnote.cn/FkBQQRHSjQqyEzPstBn2kNubZ8Pt"]
     * categories : [{"name":"两栖类","img":"http://img.blackstone.ebirdnote.cn/Foa36KgDoamKC2tHTkebyHiqQ4a5","speciesType":"amphibia"},{"name":"爬行类","img":"http://img.blackstone.ebirdnote.cn/FqcWn35rpUxlgZQE9LRXmUP-BYLa","speciesType":"reptiles"},{"name":"鸟类","img":"http://img.blackstone.ebirdnote.cn/FrB8yFe1jrZ29DFMxu-4t78rxbiA","speciesType":"bird"},{"name":"昆虫","img":"http://img.blackstone.ebirdnote.cn/Fu9jYRfqfOSyn2Gb5QVfxF0eZbgv","speciesType":"insect"}]
     * test : null
     * collections : [{"id":1,"chineseName":"小白鹭","englishName":"Little Egret","latinName":"Egretta garzetta","order":"鹳形目","family":"鹭科","mainPhoto":"http://img.blackstone.ebirdnote.cn/FlTb34Ks3GC7V9WqhW3vCGpC3xoG","speciesType":"bird"},{"id":126,"chineseName":"石蛃目","latinName":"Archaeognatha","order":"石蛃目","mainPhoto":"http://img.blackstone.ebirdnote.cn/FmVg_xeNBAfxw2jDu-gy_g8GRwi2","speciesType":"insect"}]
     */

    private Test test;
    private ArrayList<String> bannerImg;
    private List<CategoriesBean> categories;
    private List<CollectionsBean> collections;


    public MainVo() {
    }

    public MainVo(Test test, ArrayList<String> bannerImg, List<CategoriesBean> categories, List<CollectionsBean> collections) {
        this.test = test;
        this.bannerImg = bannerImg;
        this.categories = categories;
        this.collections = collections;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public ArrayList<String> getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(ArrayList<String> bannerImg) {
        this.bannerImg = bannerImg;
    }

    public List<CategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesBean> categories) {
        this.categories = categories;
    }

    public List<CollectionsBean> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionsBean> collections) {
        this.collections = collections;
    }

    public static class CategoriesBean {
        /**
         * name : 两栖类
         * img : http://img.blackstone.ebirdnote.cn/Foa36KgDoamKC2tHTkebyHiqQ4a5
         * speciesType : amphibia
         */

        private String name;
        private String img;
        private String speciesType;

        public CategoriesBean() {
        }

        public CategoriesBean(String name, String img, String speciesType) {
            this.name = name;
            this.img = img;
            this.speciesType = speciesType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }
    }


    public static class CollectionsBean {
        /**
         * id : 1
         * chineseName : 小白鹭
         * englishName : Little Egret
         * latinName : Egretta garzetta
         * order : 鹳形目
         * family : 鹭科
         * mainPhoto : http://img.blackstone.ebirdnote.cn/FlTb34Ks3GC7V9WqhW3vCGpC3xoG
         * speciesType : bird
         */

        private int id;
        private String chineseName;
        private String englishName;
        private String latinName;
        private String order;
        private String family;
        private String mainPhoto;
        private String speciesType;

        public CollectionsBean() {
        }

        public CollectionsBean(int id, String chineseName, String englishName, String latinName, String order, String family, String mainPhoto, String speciesType) {
            this.id = id;
            this.chineseName = chineseName;
            this.englishName = englishName;
            this.latinName = latinName;
            this.order = order;
            this.family = family;
            this.mainPhoto = mainPhoto;
            this.speciesType = speciesType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getLatinName() {
            return latinName;
        }

        public void setLatinName(String latinName) {
            this.latinName = latinName;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getMainPhoto() {
            return mainPhoto;
        }

        public void setMainPhoto(String mainPhoto) {
            this.mainPhoto = mainPhoto;
        }

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }
    }



    public static class Test{

        /**
         *答题总数
         */
        private int totalCount;

        /**
         * 正确率
         */
        private double ratio;

        /**
         * 排名
         */
        private int rank;

        public Test() {
        }

        public Test(int totalCount, double ratio, int rank) {
            this.totalCount = totalCount;
            this.ratio = ratio;
            this.rank = rank;
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
    }
}
