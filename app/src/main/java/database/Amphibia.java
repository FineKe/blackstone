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
    private String order;//科
    private String orderLatin;//
    private String family;//目
    private String familyLatin;
    private String genus;//属
    private String genusLatin;
    private ArrayList<String> imgs;//图片数组
    private String resolution_feature;//辨识特征
    private String shape;//体型
    private String tone;//色调
    private String vocal_sac;//声囊
    private String nuptial;//婚墊婚刺
    private String digital_formula;//指序与趾序
    private String web;//蹼与吸盘
    private String vague_feature;//模糊特征
    private String biotope;//生境与习性
    private String distribution;//分布范围
    private String population_status;//种群状况
    private ArrayList<String> viewTables;//动态创建视图的字符信息
    private boolean collected;//是否收藏
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

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getResolution_feature() {
        return resolution_feature;
    }

    public void setResolution_feature(String resolution_feature) {
        this.resolution_feature = resolution_feature;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getVocal_sac() {
        return vocal_sac;
    }

    public void setVocal_sac(String vocal_sac) {
        this.vocal_sac = vocal_sac;
    }

    public String getNuptial() {
        return nuptial;
    }

    public void setNuptial(String nuptial) {
        this.nuptial = nuptial;
    }

    public String getDigital_formula() {
        return digital_formula;
    }

    public void setDigital_formula(String digital_formula) {
        this.digital_formula = digital_formula;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getVague_feature() {
        return vague_feature;
    }

    public void setVague_feature(String vague_feature) {
        this.vague_feature = vague_feature;
    }

    public String getBiotope() {
        return biotope;
    }

    public void setBiotope(String biotope) {
        this.biotope = biotope;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getPopulation_status() {
        return population_status;
    }

    public void setPopulation_status(String population_status) {
        this.population_status = population_status;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

    public ArrayList<String> getViewTables() {
        return viewTables;
    }

    public void setViewTables(ArrayList<String> viewTables) {
        this.viewTables = viewTables;
    }

    public void setViewTables()
    {
        viewTables=new ArrayList<>();
        viewTables.add(resolution_feature);
        viewTables.add(shape);
        viewTables.add(tone);
        viewTables.add(vocal_sac);
        viewTables.add(nuptial);
        viewTables.add(digital_formula);
        viewTables.add(web);
        viewTables.add(vague_feature);
        viewTables.add(biotope);
        viewTables.add(distribution);
        viewTables.add(population_status);
    }
}
