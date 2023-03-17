
package edu.lehigh.cse216.kel323.admin;

import spark.Spark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
// import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.util.logging.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger( App.class.getName() );

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create All Tables");
        System.out.println("  [D] Drop All Tables");
        System.out.println("  [1] Query for a specific in row in message table");
        System.out.println("  [*] Query for all rows in message table");
        System.out.println("  [-] Delete a Message");
        System.out.println("  [+] Insert a new Message");
        System.out.println("  [L] Like A Message");
        System.out.println("  [B] Dislike A Message");
        System.out.println("  [C] Get all Comments on A Message");
        System.out.println("  [o] Get a single comment on a message");
        System.out.println("  [W] Post a comment on a message");
        System.out.println("  [U] Update a comment on a message");
        System.out.println("  [X] Remove a Comment");
        System.out.println("  [P] Get Profile Information");
        System.out.println("  [R] Remove a Profile");
        System.out.println("  [z] List Documents and Original Owners");
        System.out.println("  [v] Remove Least Recently Used");
        System.out.println("  [n] Remove Least Recently Used");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }
    /*To add 
        Add a like
        Add a dislike
        Get comments on a post
        Get a comment on a post
        Insert a comment on a post
        Update a Comment
        Get Profile Information
        Remmove a Profile
        */

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~LBCoWUXPRzvnq?";

        // We repeat until a valid single-character option is selected        
        while (true) {
            menu();
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    
     public static void main(String[] argv) {
        //Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));

        // get the Postgres configuration from the environment
        setEnv("DATABASE_URL",
                "postgres://ytwhcceyowndem:67468aedb6de7dfc4dbb32e8c2c07d54ecbccbf3b506b7e79ab5ebac6091f4b2@ec2-34-224-226-38.compute-1.amazonaws.com:5432/dc1vnlqcg514jo");
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        String db_url = env.get("DATABASE_URL");

        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        // Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        else
            System.out.println("\nPOSTGRES DATABASE CONNECTED!\n");

        // Set up the location for serving static files. If the STATIC_LOCATION
        // environment variable is set, we will serve from it. Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        String cors_enabled = env.get("CORS_ENABLED");

        final String acceptCrossOriginRequestsFrom = "*";
        final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
        final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
        enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call

        /*To add 
        Add a like
        Add a dislike
        Get comments on a post
        Get a comment on a post
        Insert a comment on a post
        Update a Comment
        Get Profile Information
        */
       

            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                db.createTable();
                db.createLikeTable();
                db.createSesTable();
                db.createProfileTable();
                db.createComTable();
            } else if (action == 'D') {
                db.dropTable();
                db.dropLikeTable();
                db.dropSesTable();
                db.dropComTable();
                db.dropProfileTable();

            } else if (action == '1') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                DataRow res = db.selectOne(id);
                if (res != null) {
                    System.out.println("  [" + res.mId + "] " + res.mTitle);
                    System.out.println("  --> " + res.mContent);
                }
            } else if (action == '*') {
                ArrayList<DataRow> res = db.selectAll();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (DataRow rd : res) {
                    System.out.println("  [" + rd.mId + "] " + "Title: " + rd.mTitle);
                }
            } else if(action == 'z') {
                ArrayList<DataRow> res = db.selectAll();
                if (res == null)
                    continue;
                System.out.println("  Documents and Owners");
                System.out.println("  -------------------------");
                for (DataRow rd : res) {
                    System.out.println("  [" + rd.mId + "] " + "Filename: " + rd.mFileName + " File Link: " + rd.mFileLink + " Owner User Id: " + rd.mUserId);
                }
               
            } else if(action == 'v') {
                removeLRUContent();
                System.out.println("LRU Removed");
            } else if(action == 'n') {
                removeInappropriateContent();
                System.out.println("Inappropriate Content Removed");
            }else if (action == '-') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                String filename = getString(in, "Enter the filename");
                String filelink = getString(in, "Enter the filelink");
                Float filesize = (float)getInt(in, "Enter file size");
                if (subject.equals("") || message.equals(""))
                    continue;
                int res = db.insertRow(subject, message, 0, filename, filelink, filesize);
                System.out.println(res + " rows added");
            } else if (action == '~') {
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                String filename = getString(in, "Enter the filename");
                String filelink = getString(in, "Enter the filelink");
                Float filesize = (float)getInt(in, "Enter file size");
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int res = db.updateOne(id, newMessage, filename, filelink, filesize);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                //New add a Like
            }  else if (action == 'L') {
                int id = getInt(in, "Enter the message ID of the post to Like :> ");
                if (id == -1)
                    continue;
                db.addLike(id);
                int likes = db.getLikeCount(id);
                System.out.println("New Like Count: " + likes);
                //New add Dislike
            }  else if (action == 'B') {
                int id = getInt(in, "Enter the message ID of the post to Dislike :> ");
                if (id == -1)
                    continue;
                db.addDislike(id);
                int dislikes = db.getDislikeCount(id);
                System.out.println("New Dislike Count: " + dislikes);
                //Get All Comments
            }  else if (action == 'C') {
                int id = getInt(in, "Enter the message ID :> ");
                ArrayList<DataComRow> res = db.getComments(id);
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (DataComRow rd : res) {
                    System.out.println("  [" + rd.mcomId + "] " + "Comment: " + rd.mComment);
                }
                //Gets a singular comment
            }  else if (action == 'o') {
                int id = getInt(in, "Enter the comment ID :> ");
                DataComRow res = db.selectCommentOne(id);
                if (res == null)
                    continue;
                System.out.println("ComId: " + id + " Comment: " + res.mComment);
            }  
            //Posts a comment
            else if (action == 'W') {
                int id = getInt(in, "Enter the message ID :> ");
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                String filename = getString(in, "Enter the filename");
                String filelink = getString(in, "Enter the filelink");
                Float filesize = (float)getInt(in, "Enter file size");
                if (id == -1)
                    continue;
                String com = getString(in, "Enter the Comment: ");

                int res = db.insertComRow(com, 0, id, filename, filelink, filesize);
                if(res == 0)
                    continue;

                System.out.println(res + " Comment Successfully added");
                //Update a Comment
            }else if (action == 'U') {
                int id = getInt(in, "Enter the comment ID :> ");
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                String filename = getString(in, "Enter the filename");
                String filelink = getString(in, "Enter the filelink");
                Float filesize = (float)getInt(in, "Enter file size");
                if (id == -1)
                    continue;
                String com = getString(in, "Enter the New Comment: ");

                int res = db.changeComment(id, com, filename, filelink, filesize);
                if(res == 0)
                    continue;

                System.out.println(res + " Comment Successfully Changed");
            
                //Get a Profile
            }else if (action == 'X') {
                int id = getInt(in, "Enter the  comment ID :> ");
                if (id == -1)
                    continue;

                int res = db.deleteComRow(id);
                if(res == 0)
                    continue;

                System.out.println("User Deleted Successfully");
            }else if (action == 'P') {
                int id = getInt(in, "Enter the  user ID :> ");
                if (id == -1)
                    continue;

                ProfileRow prof = db.getProfile(id);
                if(prof == null)
                    continue;

                System.out.println("User Name: " + prof.username);
                System.out.println("User Email: " + prof.email);
                //Remove Profile 
            }else if (action == 'R') {
                int id = getInt(in, "Enter the  user ID :> ");
                if (id == -1)
                    continue;

                int res = db.deleteProfRow(id);
                if(res == 0)
                    continue;

                System.out.println("User Deleted Successfully");
            }      
        }
        
        // Always remember to disconnect from the database when the program 
        // exits
        db.disconnect();
    }
    
    static boolean isSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        return sameDay;
    }
    
    private static void removeLRUContent() {
        setEnv("DATABASE_URL",
                "postgres://ytwhcceyowndem:67468aedb6de7dfc4dbb32e8c2c07d54ecbccbf3b506b7e79ab5ebac6091f4b2@ec2-34-224-226-38.compute-1.amazonaws.com:5432/dc1vnlqcg514jo");
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        ArrayList<DataRow> list = db.selectAll();
        List <Date> dates = new ArrayList();
        for (int i = 0; i < list.size(); i++){
            dates.add(list.get(i).mCreated);
        }
        Date minDate = Collections.min(dates);
        for (int i = 0; i < list.size(); i++){
            if (isSameDay(list.get(i).mCreated, minDate)){
                db.deleteRow(list.get(i).mId);
            }
        }
    }

    private static void removeInappropriateContent(){
        setEnv("DATABASE_URL",
                "postgres://ytwhcceyowndem:67468aedb6de7dfc4dbb32e8c2c07d54ecbccbf3b506b7e79ab5ebac6091f4b2@ec2-34-224-226-38.compute-1.amazonaws.com:5432/dc1vnlqcg514jo");
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        ArrayList<DataRow> list = db.selectAll();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).mContent.contains("fuck") || list.get(i).mContent.contains("shit")){
                db.deleteRow(list.get(i).mId);
            }
        }

    }


/**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends. This only needs to be called once.
     * 
     * @param origin  The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any
        // get/post/put/delete. In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

    public static void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }
}