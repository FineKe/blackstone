package database;

import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/5/2.
 */

public class Bird extends DataSupport{
    private int singal;//id
    private String chineseName;
    private String englishName;
    private String latinName;
    private String order;
    private String orderLatin;
    private String family;
    private String familyLatin;
    private String genusLatin;
    private String mainPhoto;
    private ArrayList<String> imgs;
    private String habitat;
    private String shape;
    private String beak_shape;
    private String eating_pattern;
    private String sub_color;
    private String major_color;
    private String tweet;
    private String fly_pattern;
    private String speciesType;

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

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
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

    public String getSub_color() {
        return sub_color;
    }

    public void setSub_color(String sub_color) {
        this.sub_color = sub_color;
    }

    public String getMajor_color() {
        return major_color;
    }

    public void setMajor_color(String major_color) {
        this.major_color = major_color;
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
}
