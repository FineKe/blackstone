package database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/5/2.
 */

public class Reptiles extends DataSupport {
    private int id;
    private String chineseName;
    private String latinName;
    private String order;
    private String orderLatin;
    private String family;
    private String familyLatin;
    private String genus;
    private String genusLatin;
    private String mainPhoto;
    private ArrayList<String> imgs;
    private String has_bigscale;
    private String habitat;
    private String shape;
    private String resolution_feature;
    private String vague_feature;
    private String sub_color;
    private String eating_pattern;
    private String major_color;
    private String speciesType;

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

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
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

    public String getHas_bigscale() {
        return has_bigscale;
    }

    public void setHas_bigscale(String has_bigscale) {
        this.has_bigscale = has_bigscale;
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

    public String getResolution_feature() {
        return resolution_feature;
    }

    public void setResolution_feature(String resolution_feature) {
        this.resolution_feature = resolution_feature;
    }

    public String getVague_feature() {
        return vague_feature;
    }

    public void setVague_feature(String vague_feature) {
        this.vague_feature = vague_feature;
    }

    public String getSub_color() {
        return sub_color;
    }

    public void setSub_color(String sub_color) {
        this.sub_color = sub_color;
    }

    public String getEating_pattern() {
        return eating_pattern;
    }

    public void setEating_pattern(String eating_pattern) {
        this.eating_pattern = eating_pattern;
    }

    public String getMajor_color() {
        return major_color;
    }

    public void setMajor_color(String major_color) {
        this.major_color = major_color;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }
}
