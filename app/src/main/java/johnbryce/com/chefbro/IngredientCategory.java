package johnbryce.com.chefbro;

/**
 * Created by jbt on 6/22/2016.
 */
public class IngredientCategory {
    private String mID;
    private String mName;

    public IngredientCategory() {
    }

    public IngredientCategory(String mName) {
        this.mName = mName;
    }

    public IngredientCategory(String mID, String mName) {
        this.mID = mID;
        this.mName = mName;
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

    public void setName(String Name) {
        this.mName = Name;
    }
}
