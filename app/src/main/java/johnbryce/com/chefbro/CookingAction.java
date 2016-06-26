package johnbryce.com.chefbro;

/**
 * Created by jbt on 6/26/2016.
 */
public class CookingAction {
    private String mID;
    private String mCategory;
    private String mName;

    public CookingAction() {
    }

    public CookingAction(String category, String name) {
        mCategory = category;
        mName = name;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
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
