package JavaBean;

/**
 * Created by MY SHIP on 2017/5/17.
 * javaBean 物种类别
 */

public class SpeciesClasses {
    private int id;//用于区分是否处于同一header下
    private String title;
    private String className;//类别名称
    private String mainPhoto;

    public SpeciesClasses(int id, String title, String className, String mainPhoto) {
        this.id = id;
        this.title = title;
        this.className = className;
        this.mainPhoto=mainPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }
}
