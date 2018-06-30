package com.kefan.blackstone.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/14 下午11:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoriesBean {
        /**
         * name : 两栖类
         * img : http://img.blackstone.ebirdnote.cn/Foa36KgDoamKC2tHTkebyHiqQ4a5
         * speciesType : amphibia
         */

        private String name;
        private String img;
        private String speciesType;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
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
    }


    @AllArgsConstructor
    @Data
    @NoArgsConstructor
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


    }
}
