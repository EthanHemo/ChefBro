package johnbryce.com.chefbro;

import java.io.Serializable;
import java.util.ArrayList;


public class Recipe implements Serializable {
    private String mName;
    private String mID;
    private String mOwnerUID;
    private ArrayList<Ingredient> mIngredients;
    private String mDateCreated;
    private String mDateLastModify;
    private ArrayList<String> mRecipeCategory;

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

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        this.mID = ID;
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

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String DateCreated) {
        this.mDateCreated = DateCreated;
    }

    public String getDateLastModify() {
        return mDateLastModify;
    }

    public void setDateLastModify(String DateLastModify) {
        this.mDateLastModify = DateLastModify;
    }

    public ArrayList<String> getRecipeCategory() {
        return mRecipeCategory;
    }

    public void setRecipeCategory(ArrayList<String> RecipeCategory) {
        this.mRecipeCategory = RecipeCategory;
    }
}
