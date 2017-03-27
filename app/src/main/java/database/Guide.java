package database;

import org.litepal.crud.DataSupport;

/**
 * Created by MY SHIP on 2017/3/25.
 */

public class Guide extends DataSupport{
    private String introduction;
    private String base;
    private String meaning;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
