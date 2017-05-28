package database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by MY SHIP on 2017/5/2.
 */

public class Insect extends DataSupport{
    private int singal;
    private String chineseName;
    private String latinName;
    private String order;
    private String orderLatin;
    private ArrayList<String> imgs;
    private String rough_feature;//大体特征
    private String mouthparts;//口器
    private String ognathus;//口式
    private String tentacle;//触角
    private String wing;//翅
    private String leg;//足
    private String life_cycle;//生活史
    private String young_feature;//低龄虫态特征
    private String common_family;//黑石顶常见科
    private String speciesType;
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

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
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
        viewTables.add(rough_feature);
        viewTables.add(mouthparts);
        viewTables.add(ognathus);
        viewTables.add(tentacle);
        viewTables.add(wing);
        viewTables.add(leg);
        viewTables.add(life_cycle);
        viewTables.add(young_feature);
        viewTables.add(common_family);
    }
}
