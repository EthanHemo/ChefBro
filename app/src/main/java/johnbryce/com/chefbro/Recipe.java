package johnbryce.com.chefbro;

import java.io.Serializable;
import java.util.ArrayList;


public class Recipe implements Serializable {
    private String mName;
    private String mOwnerUID;
    private ArrayList<Ingredient> mIngredients;

    public Recipe() {
        mIngredients = new ArrayList<Ingredient>();
    }

    public Recipe(String Name, String OwnerUID, ArrayList<Ingredient> ingredients) {
        mIngredients = new ArrayList<Ingredient>();

        setName(Name);
        setOwnerUID(OwnerUID);
        setIngredients(ingredients);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getOwnerUID() {
        return mOwnerUID;
    }

    public void setOwnerUID(String OwnerUID) {
        this.mOwnerUID = OwnerUID;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.mIngredients.addAll(ingredients);
    }
}
