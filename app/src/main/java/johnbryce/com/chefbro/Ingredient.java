package johnbryce.com.chefbro;

import java.io.Serializable;

/**
 * Created by Shane on 08/06/2016.
 */
public class Ingredient implements Serializable {
    private String mDetails;

    public Ingredient() {
    }

    public Ingredient(String Details) {
        this.mDetails = Details;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String Details) {
        this.mDetails = Details;
    }
}
