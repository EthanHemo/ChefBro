package johnbryce.com.chefbro;

/**
 * Created by jbt on 6/8/2016.
 */
public class UserChef {
    private String mEmail;
    private String mUid;
    private String mPictureName;

    public UserChef() {
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

    public String getPictureName() {
        return mPictureName;
    }

    public void setPictureName(String PictureName) {
        this.mPictureName = PictureName;
    }
}
