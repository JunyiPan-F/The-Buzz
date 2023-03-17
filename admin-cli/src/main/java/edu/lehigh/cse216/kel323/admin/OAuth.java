package edu.lehigh.cse216.kel323.admin;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Hashtable;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;



import java.util.logging.*;

public class OAuth {
    public static final String CLIENT_ID = "26342651488-daqciiut6qikektlcu6oj6h21iij33cr.apps.googleusercontent.com";
    private static final Logger LOGGER = Logger.getLogger( OAuth.class.getName() );
    public static Hashtable<String,String> users_hashtable = new Hashtable<String, String>();
    
    public static String fakeHash(String email){
        char[] emailChars = email.toCharArray();
        for (int i = 0; i < email.length(); i++) {
            emailChars[i]+=1;
        }
        return(toString(emailChars));
    }

    public static String toString(char[] a){
        String string = new String(a);
        return string;
    }
    
    public static User OAuthAuthorize(String id_token) throws GeneralSecurityException, IOException{
        JsonFactory jsonFactory = new GsonFactory();
        LOGGER.log( Level.WARNING, "reached line 37");
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Arrays.asList(CLIENT_ID)).build();
        GoogleIdToken idToken = verifier.verify(id_token);
        if (idToken != null){
            Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl= (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            String sessionKey = fakeHash(id_token); // hash id token to generate seesion key, what we store in hashtable
            if (!users_hashtable.containsKey(sessionKey)) users_hashtable.put(sessionKey, email); // check if they are in hashtable
            System.out.println(users_hashtable);
            return new User(userId, email, emailVerified, name, pictureUrl, locale, familyName, givenName, id_token);
        }
        else return null;
        
        
    }
}
