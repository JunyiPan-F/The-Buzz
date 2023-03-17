package edu.lehigh.cse216.spring2022.AWS;

public class MessageModel {
    private String message;
    private int status;

    public MessageModel(){
        this.message = "";
    }

    public MessageModel(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
