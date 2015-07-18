package au.com.confess.confess;

/**
 * Created by admin on 16/07/15.
 */
public class CollectDetails {

    /**
     Single Ton Instance used for setting username, email address, photourl
     */

    private static CollectDetails instance = null;

    protected CollectDetails() {
        // Exists only to defeat instantiation.
    }
    public static CollectDetails getInstance() {
        if(instance == null) {
            instance = new CollectDetails();
        }
        return instance;
    }

    private String userName;
    private String emailAddress;
    private String photoURL;

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setemailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getemailAddress()
    {
        return emailAddress;
    }

    public String getPhotoURL()
    {
        return photoURL;
    }

}
