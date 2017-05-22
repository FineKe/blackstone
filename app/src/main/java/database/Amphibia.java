package database;

import org.litepal.crud.DataSupport;

import java.security.PrivateKey;
import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/4/28.
 */

public class Amphibia extends DataSupport {
    private int singal;
    private String chineseName;//中文名
    private String latinName;//拉丁名
    private String order;//
    private String orderLatin;//
    private String family;//科
    private String familyLatin;

    public String getFamilyLatin() {
        return familyLatin;
    }

    public void setFamilyLatin(String familyLatin) {
        this.familyLatin = familyLatin;
    }

    private String genus;//属
    private String genusLatin;
    private String mainPhoto;//主图片
    private ArrayList<String> imgs;//图片数组
    private String shape;//体型
    private String subColor;//颜色
    private String populationStatus;//常见分布地区
    private String majorColor;//主颜色
    private String tweet;//
    private String distribution;//分布地区
    private String vocalSac;//
    private String web;//
    private String vagueFeature;//
    private String biotope;//
    private String digitalFormula;//
    private String nuptial;

    public int getSingal() {
        return singal;
    }

    public void setSingal(int singal) {
        this.singal = singal;
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

    public String getGenusLatin() {
        return genusLatin;
    }

    public void setGenusLatin(String genusLatin) {
        this.genusLatin = genusLatin;
    }

    private String speciesType;



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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
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

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getSubColor() {
        return subColor;
    }

    public void setSubColor(String subColor) {
        this.subColor = subColor;
    }

    public String getPopulationStatus() {
        return populationStatus;
    }

    public void setPopulationStatus(String populationStatus) {
        this.populationStatus = populationStatus;
    }

    public String getMajorColor() {
        return majorColor;
    }

    public void setMajorColor(String majorColor) {
        this.majorColor = majorColor;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getVocalSac() {
        return vocalSac;
    }

    public void setVocalSac(String vocalSac) {
        this.vocalSac = vocalSac;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getVagueFeature() {
        return vagueFeature;
    }

    public void setVagueFeature(String vagueFeature) {
        this.vagueFeature = vagueFeature;
    }

    public String getBiotope() {
        return biotope;
    }

    public void setBiotope(String biotope) {
        this.biotope = biotope;
    }

    public String getDigitalFormula() {
        return digitalFormula;
    }

    public void setDigitalFormula(String digitalFormula) {
        this.digitalFormula = digitalFormula;
    }

    public String getNuptial() {
        return nuptial;
    }

    public void setNuptial(String nuptial) {
        this.nuptial = nuptial;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }
}
