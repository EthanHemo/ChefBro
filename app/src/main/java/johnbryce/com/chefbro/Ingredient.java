package johnbryce.com.chefbro;

import java.io.Serializable;

/**
 * Created by Shane on 08/06/2016.
 */
public class Ingredient implements Serializable {
    private String mName;
    private String mCategory;

    public Ingredient() {
    }

    public Ingredient(String Name) {
        this.mName = Name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String Name) {
        this.mName = Name;
    }
}
