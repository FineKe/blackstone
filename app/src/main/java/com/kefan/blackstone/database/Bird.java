package com.kefan.blackstone.database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/5/2.
 */

public class Bird extends DataSupport {
    private int singal;//id
    private String chineseName;
    private String englishName;
    private String latinName;
    private String order;
    private String orderLatin;
    private String family;
    private String familyLatin;
    private String genusLatin;
    private ArrayList<String> imgs;

    private String tail_shape;
    private String habitat;
    private String shape;
    private String tone;
    private String beak_shape;
    private String eating_pattern;
    private String tweet_image;
    private String tweet;
    private String tweet_name;
    private String body_long;
    private String fly_pattern;
    private String speciesType;
    private String audio;
    private String vague_feature;
    private String audioPicture;
    private ArrayList<String> viewTables;//动态创建视图的字符信息
    private boolean collected;//是否收藏

    public int getSingal() {
        return singal;
    }

    public void setSingal(int singal) {
        this.singal = singal;
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

    public String getOrderLatin() {
        return orderLatin;
    }

    public void setOrderLatin(String orderLatin) {
        this.orderLatin = orderLatin;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFamilyLatin() {
        return familyLatin;
    }

    public void setFamilyLatin(String familyLatin) {
        this.familyLatin = familyLatin;
    }

    public String getGenusLatin() {
        return genusLatin;
    }

    public void setGenusLatin(String genusLatin) {
        this.genusLatin = genusLatin;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getBeak_shape() {
        return beak_shape;
    }

    public void setBeak_shape(String beak_shape) {
        this.beak_shape = beak_shape;
    }

    public String getEating_pattern() {
        return eating_pattern;
    }

    public void setEating_pattern(String eating_pattern) {
        this.eating_pattern = eating_pattern;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getFly_pattern() {
        return fly_pattern;
    }

    public void setFly_pattern(String fly_pattern) {
        this.fly_pattern = fly_pattern;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudioPicture() {
        return audioPicture;
    }

    public void setAudioPicture(String audioPicture) {
        this.audioPicture = audioPicture;
    }

    public String getTail_shape() {
        return tail_shape;
    }

    public void setTail_shape(String tail_shape) {
        this.tail_shape = tail_shape;
    }

    public String getTweet_image() {
        return tweet_image;
    }

    public void setTweet_image(String tweet_image) {
        this.tweet_image = tweet_image;
    }

    public String getTweet_name() {
        return tweet_name;
    }

    public void setTweet_name(String tweet_name) {
        this.tweet_name = tweet_name;
    }

    public String getBody_long() {
        return body_long;
    }

    public void setBody_long(String body_long) {
        this.body_long = body_long;
    }

    public ArrayList<String> getViewTables() {
        return viewTables;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getVague_feature() {
        return vague_feature;
    }

    public void setVague_feature(String vague_feature) {
        this.vague_feature = vague_feature;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void setViewTables() {
        viewTables = new ArrayList<>();
        viewTables.add(getShape());//体型
        viewTables.add(getHabitat());//栖息地
        viewTables.add(getTone());//色调
        viewTables.add(getBeak_shape());//喙型喙长
        viewTables.add(getFly_pattern());//飞行类型
        viewTables.add(getTail_shape());//尾型
        viewTables.add(getEating_pattern());//食性
        viewTables.add(getVague_feature());//模糊特征
        viewTables.add(getTweet());//鸣声描述
    }

}
