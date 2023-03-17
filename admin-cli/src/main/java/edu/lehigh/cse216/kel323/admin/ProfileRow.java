package edu.lehigh.cse216.kel323.admin;

public class ProfileRow {
    public final int userId;
    public final String username;
    public final String email;

    ProfileRow(int userId, String username, String email){
        this.userId = userId;
        this.username  = username;
        this.email = email;
    }

    ProfileRow(){
        this.userId = 0;
        this.username  = "";
        this.email = "";
    }
}
