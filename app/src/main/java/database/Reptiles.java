package database;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class Reptiles {
    private int id;
    private String chinesName;
    private String latinName;
    private String mainPhoto;
    private String speciesType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChinesName() {
        return chinesName;
    }

    public void setChinesName(String chinesName) {
        this.chinesName = chinesName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
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
