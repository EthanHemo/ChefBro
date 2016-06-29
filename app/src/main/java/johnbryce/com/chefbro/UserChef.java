package johnbryce.com.chefbro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbt on 6/8/2016.
 */
public class UserChef implements Serializable {
    private String mUid;
    private String mFullName;
    private String mEmail;
    private String mPictureName;
    private List<String> mRecipiesCreated;
    private List<String> mRecipiesDone;

    public UserChef() {
        mRecipiesCreated = new ArrayList<>();
        mRecipiesDone = new ArrayList<>();

    }

    public UserChef(String mUid,String mEmail) {
        this.mEmail = mEmail;
        this.mUid = mUid;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String Email) {
        this.mEmail = Email;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public void setFullName(String fullName)
    {
        mFullName = fullName;
    }

    public String getFullName()
    {
        return mFullName;
    }

    public String getPictureName() {
        if(mPictureName==null)
            return "unknown.jpg";
        return mPictureName;
    }

    public void setPictureName(String PictureName) {
        this.mPictureName = PictureName;
    }

    public List<String> getRecipiesCreated() {
        return mRecipiesCreated;
    }

    public void setRecipiesCreated(List<String> RecipiesCreated) {
        this.mRecipiesCreated.addAll(RecipiesCreated);
    }

    public List<String> getRecipiesDone() {
        return mRecipiesDone;
    }

    public void setRecipiesDone(List<String> RecipiesDone) {
        this.mRecipiesDone.addAll(RecipiesDone);
    }
}
