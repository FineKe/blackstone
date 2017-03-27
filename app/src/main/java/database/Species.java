package database;

import android.nfc.Tag;

import org.litepal.crud.DataSupport;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class Species extends DataSupport{

    private int id;
    private String chineseName;
    private String latinName;
    private String order_;
    private String family;
    private String mainPhoto;
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
        return order_;
    }

    public void setOrder(String order) {
        this.order_ = order;
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
