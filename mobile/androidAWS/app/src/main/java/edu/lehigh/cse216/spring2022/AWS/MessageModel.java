package edu.lehigh.cse216.spring2022.AWS;

import java.util.ArrayList;
import java.util.Map;

public class MessageModel {
    private String message;
    private int status;
    private String usr_name;
    private ArrayList<Map<String,String>> commentsInfoArray;
    //private Map<String,String> commentsInfo;

    public MessageModel(){
        this.message = "";
        this.status = 0;
        this.usr_name = "";
        this.commentsInfoArray = new ArrayList<Map<String, String>>();
    }

    public MessageModel(String message, int status, String name, ArrayList<Map<String,String>> commentsInfoArray) {
        this.message = message;
        this.status = status;
        this.usr_name = name;
        this.commentsInfoArray = commentsInfoArray;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return usr_name;
    }
    public void setName(String name) {
        this.usr_name = name;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Map<String,String>> getCommentsInfo(){ return  commentsInfoArray;}

    public void setCommentsInfo(Map<String, String> commentsInfo) {
        for(int i = 0; i < commentsInfoArray.size(); i++){
            this.commentsInfoArray.set(i,commentsInfo);
        }
    }
}
