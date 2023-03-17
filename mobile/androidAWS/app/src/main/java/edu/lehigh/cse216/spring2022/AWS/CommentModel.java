package edu.lehigh.cse216.spring2022.AWS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentModel{
        private String comment;
        private String usr_name;
        private Map<String,String> commentsInfo;

        public CommentModel(){
            this.comment = "";
            this.usr_name = "";
            this.commentsInfo = new HashMap<String, String>();
        }

        public CommentModel(String comment, String usr_name) {
            this.comment= comment;
            this.usr_name = usr_name;
            commentsInfo.put(usr_name,comment);
        }

        public String getComment() {
            return comment;
        }
        public void setComment(String message) {
            this.comment = message;
        }

        public String getName() {
            return usr_name;
        }
        public void setName(String name) {
            this.usr_name = name;
        }


        public Map<String,String> getCommentsInfo(){
            return commentsInfo;
        }

        public void setCommentsInfo(String usr_name,String comment) {
            commentsInfo.put(usr_name,comment);
        }


}
