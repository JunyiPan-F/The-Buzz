package edu.lehigh.cse216.kel323.admin;

public class User {
    String userId;
    String email;
    boolean emailVerified;
    String name;
    String pictureUrl;
    String locale;
    String familyName;
    String givenName;
    String AccessKey;

    public User(String userId, String email, boolean emailVerified, String name, String pictureUrl, String locale, String familyName, String givenName, String AccessKey){
        this.userId = userId;
        this.email = email;
        this.emailVerified = emailVerified;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.locale = locale;
        this.familyName  = familyName;
        this.givenName = givenName;
        this.AccessKey = AccessKey;
    }
    
}
