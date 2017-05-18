package JavaBean;

/**
 * Created by MY SHIP on 2017/5/17.
 * javaBean 物种类别
 */

public class SpeciesClasses {
    private int id;//用于区分是否处于同一header下
    private String title;
    private String className;//类别名称
    private int pictureId;

    public SpeciesClasses(int id, String title, String className, int pictureId) {
        this.id = id;
        this.title = title;
        this.className = className;
        this.pictureId = pictureId;
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

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
