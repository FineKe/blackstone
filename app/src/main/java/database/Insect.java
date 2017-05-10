package database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/5/2.
 */

public class Insect extends DataSupport{
    private int id;
    private String chineseName;
    private String latinName;
    private String order;
    private String orderLatin;
    private String mainPhoto;
    private ArrayList<String> imgs;
    private String description;
    private String rough_feature;
    private String tentacle;
    private String life_cycle;
    private String mouthparts;
    private String common_family;
    private String ognathus;
    private String wing;
    private String young_feature;
    private String leg;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRough_feature() {
        return rough_feature;
    }

    public void setRough_feature(String rough_feature) {
        this.rough_feature = rough_feature;
    }

    public String getTentacle() {
        return tentacle;
    }

    public void setTentacle(String tentacle) {
        this.tentacle = tentacle;
    }

    public String getLife_cycle() {
        return life_cycle;
    }

    public void setLife_cycle(String life_cycle) {
        this.life_cycle = life_cycle;
    }

    public String getMouthparts() {
        return mouthparts;
    }

    public void setMouthparts(String mouthparts) {
        this.mouthparts = mouthparts;
    }

    public String getCommon_family() {
        return common_family;
    }

    public void setCommon_family(String common_family) {
        this.common_family = common_family;
    }

    public String getOgnathus() {
        return ognathus;
    }

    public void setOgnathus(String ognathus) {
        this.ognathus = ognathus;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getYoung_feature() {
        return young_feature;
    }

    public void setYoung_feature(String young_feature) {
        this.young_feature = young_feature;
    }

    public String getLeg() {
        return leg;
    }

    public void setLeg(String leg) {
        this.leg = leg;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }
}
