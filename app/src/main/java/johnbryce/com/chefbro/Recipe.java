package johnbryce.com.chefbro;

import java.io.Serializable;

/**
 * Created by jbt on 6/5/2016.
 */
public class Recipe implements Serializable {
    private String mName;
    private String mYummy;

    public Recipe() {
    }

    public Recipe(String Name, String Yummy) {
        setName(Name);
        setYummy(Yummy);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getYummy() {
        return mYummy;
    }

    public void setYummy(String mYummy) {
        this.mYummy = mYummy;
    }

    @Override
    public String toString() {
        return getName() + " is " + getYummy();
    }
}
