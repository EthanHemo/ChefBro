package johnbryce.com.chefbro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane on 08/06/2016.
 */
public class Ingredient implements Serializable {
    private String mID;
    private String mName;
    private List<IngredientCategory> mCategory;

    public Ingredient() {
        mCategory = new ArrayList<>();
    }

    public Ingredient(String Name) {
        mCategory = new ArrayList<>();
        this.mName = Name;
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

    public List<IngredientCategory> getCategory() {
        return mCategory;
    }

    public void setCategory(List<IngredientCategory> Category) {
        this.mCategory = Category;
    }
}
