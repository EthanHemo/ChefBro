package johnbryce.com.chefbro;

/**
 * Created by jbt on 6/26/2016.
 */
public class ActionCategory {
    private String mID;
    private String mName;

    public ActionCategory() {
    }

    public ActionCategory(String name) {
        mName = name;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        this.mID = ID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
