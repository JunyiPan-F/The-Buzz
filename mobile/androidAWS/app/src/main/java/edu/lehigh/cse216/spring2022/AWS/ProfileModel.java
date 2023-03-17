package edu.lehigh.cse216.spring2022.AWS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileModel{
    private String id;
    private String name;
    private String email;

    public ProfileModel(){
        this.email = "";
        this.id = "";
        this.name = "";
    }

    public ProfileModel(String id, String name, String email) {
        this.id= id;
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
