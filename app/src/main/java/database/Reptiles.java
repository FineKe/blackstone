package database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/5/2.
 */

public class Reptiles extends DataSupport {
    private int singal;
    private String chineseName;
    private String latinName;
    private String order;
    private String orderLatin;
    private String family;
    private String familyLatin;
    private String genus;
    private String genusLatin;
    private ArrayList<String> imgs;
    private String resolution_feature;//辨识特征
    private String shape;//体型
    private String habitat;//栖息地
    private String major_color;//主色
    private String sub_color;//次色
    private String eating_pattern;//食性
    private String has_bigscale;//头顶有无大鳞
    private String vague_feature;//模糊特征
    private ArrayList<String> viewTables;//动态创建视图的字符信息
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


    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
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

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getMajor_color() {
        return major_color;
    }

    public void setMajor_color(String major_color) {
        this.major_color = major_color;
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

    public String getHas_bigscale() {
        return has_bigscale;
    }

    public void setHas_bigscale(String has_bigscale) {
        this.has_bigscale = has_bigscale;
    }

    public String getVague_feature() {
        return vague_feature;
    }

    public void setVague_feature(String vague_feature) {
        this.vague_feature = vague_feature;
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
        viewTables.add(habitat);
        viewTables.add(major_color);
        viewTables.add(sub_color);
        viewTables.add(eating_pattern);
        viewTables.add(has_bigscale);
        viewTables.add(vague_feature);
    }
}


