package edu.lehigh.cse216.kel323.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

import com.google.gson.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
// import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.util.logging.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.util.List;
import java.net.InetSocketAddress;

/**GENERAL TO DO
 * Update SelectAll and SelectOne to include comments
 * Fix likeStatus information to work correctly
 * How does ouath API work?
 * Table Creation Attempts
 */

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
/**
 * The main routine runs a loop that gets a request from the user and
 * processes it
 * 
 * @param argv Command-line options. Ignored by this program.
 */

public class App {
    private static final Logger LOGGER = Logger.getLogger( App.class.getName() );
    public static void main(String[] args) throws GeneralSecurityException, IOException {

        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));
        
        // get the Postgres configuration from the environment
        //setEnv("MEMCACHIER_SERVER", "mc4.dev.ec2.memcachier.com:11211");
        //setEnv("MEMCACHIER_USERNAME", "87E88B");
        //setEnv("MEMCACHIER_PASSWORD", "F03E52DAC6EDCB528992736DDF961F39");
        //setEnv("DATABASE_URL",
          //      "postgres://ytwhcceyowndem:67468aedb6de7dfc4dbb32e8c2c07d54ecbccbf3b506b7e79ab5ebac6091f4b2@ec2-34-224-226-38.compute-1.amazonaws.com:5432/dc1vnlqcg514jo");
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        String db_url = env.get("DATABASE_URL");
        
        //Get a fully-configured connection to the database, or exit
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
        
        
        // cache connection
        List<InetSocketAddress> servers = AddrUtil.getAddresses(System.getenv("MEMCACHIER_SERVER").replace(",", " "));
        AuthInfo authInfo = AuthInfo.plain(System.getenv("MEMCACHIER_USERNAME"), System.getenv("MEMCACHIER_PASSWORD"));
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);
        for(InetSocketAddress server : servers) {
            builder.addAuthInfo(server, authInfo);
        }
        builder.setCommandFactory(new BinaryCommandFactory());
        builder.setConnectTimeout(1000);
        builder.setEnableHealSession(true);
        builder.setHealSessionInterval(2000);
        try {
            MemcachedClient mc = builder.build();
            try {
            mc.set("foo", 0, "bar");
            String val = mc.get("foo");
            System.out.println(val);
            } catch (TimeoutException te) {
                System.err.println("Timeout during set or get: " + te.getMessage());
            } catch (InterruptedException ie) {
                System.err.println("Interrupt during set or get: " + ie.getMessage());
            } catch (MemcachedException me) {
                System.err.println("Memcached error during get or set: " + me.getMessage());
            }
        } catch (IOException ioe) {
            System.err.println("Couldn't create a connection to MemCachier: " + ioe.getMessage());
        }
        
        // google drive for file uplaod
        DriveQuickstart init = new DriveQuickstart();
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        //Drive service = new Drive.Builder(HTTP_TRANSPORT, DriveQuickstart.JSON_FACTORY, DriveQuickstart.getCredentials(HTTP_TRANSPORT)).setApplicationName(DriveQuickstart.APPLICATION_NAME).build();
        // we use this in routes for upload
        
        


        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // dataStore holds all of the data that has been provided via HTTP
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and
        // every time we start the server, we'll have an empty dataStore,
        // with IDs starting over from 0.
        // final DataStore dataStore = new DataStore();

        // GET route that returns all message titles and Ids. All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it. If there's no data, we return "[]", so there's no need
        // for error handling.
        Spark.get("/messages", (request, response) -> { // MUST ADD IN COMMENTS
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // db.createTable();
            // db.createSesTable();
            // db.createComTable();
            // db.createLikeTable();
            // db.createProfileTable();
            // db.insertNewUser();
            // db.userExists();
            return gson.toJson(new StructuredResponse("ok", null, db.selectAll()));
        });

        // GET route that returns all comments. All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it. If there's no data, we return "[]", so there's no need
        // for error handling.

        /** The get route that will get all comments for a specific post */
        Spark.get("/messages/:id/comments", (request, response) -> { // MUST ADD IN COMMENTS
            // ensure status 200 OK, with a MIME type of JSON
            int idx = Integer.parseInt(request.params("id"));
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.getComments(idx)));
        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.

        /**
         * Displays a specific message with an ID - important for the comments display
         */
        Spark.get("/:userId/profile", (request, response) -> {
            int idx = Integer.parseInt(request.params("userId"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            DataRow data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // POST route for adding a new element to the DataStore. This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.

        /** Adds a new messaage to the database that was entered by the client */
        Spark.post("/messages/:userId", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            int userId = Integer.parseInt(request.params("userId"));
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.insertRow(req.mTitle, req.mMessage, userId);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        /** This posts a comment from a user to a specifc post */

        Spark.post("/messages/:id/:userID/comment", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            int messageID = Integer.parseInt(request.params("id"));
            int userID = Integer.parseInt(request.params("userID"));
            SimpleComRequest req = gson.fromJson(request.body(), SimpleComRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newComId = db.insertComRow(req.mComment, userID, messageID);
            if (newComId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newComId, null));
            }
        });

        // PUT route for updating a row in the db. This is almost
        // exactly the same as POST
        Spark.put("/messages/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOne(idx, req.mMessage);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/messages/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int result = db.deleteRow(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // PUT route for incrementing like

        /**
         * Responsible for changing the likes of a given post. The like status
         * demonstrates what happens when a like or dislike occurs
         * When a 0 is routed - the like button has been pressed
         * When a 1 is routed - the dislike button has been pressed
         */

        Spark.put("/messages/:id/:userID/:like_or_dislike", (request, response) -> {
            int result = -1;
            int idx = Integer.parseInt(request.params("id"));
            int userID = Integer.parseInt(request.params("userID"));
            int like_or_dislike = Integer.parseInt(request.params("like_or_dislike"));
            // SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            int status = db.getLikeStatus(userID, idx);
            System.out.println("This is the status: " + status);
            response.status(200);
            response.type("application/json");
            if (status == -2) {
                System.out.println("Inside IF");
                db.insertLikeRow(userID, idx);
                status = 0;
                System.out.println("This is the new status: " + status);
            }
            // 0 is like, 1 is dislike

            if (status == 0) { // If the user is neutral on the post then they will be able to like or dislike
                               // it
                if (like_or_dislike == 0) { // Likes the message
                    result = db.addLike(idx);
                    result = db.setLikeStatus(userID, idx, 1); // Updates user like status
                    System.out.println("User like status: " + db.getLikeStatus(userID, idx));
                    return gson.toJson(new StructuredResponse("ok", null, result));
                } else if (like_or_dislike == 1) { // Dislikes the message
                    result = db.addDislike(idx);
                    result = db.setLikeStatus(userID, idx, -1);
                    return gson.toJson(new StructuredResponse("ok", null, result));
                } else {
                    return gson.toJson(new StructuredResponse("error", "unable to like or dislike " + idx, null));
                }
            } else if (status == 1) { // If message is already liked
                if (like_or_dislike == 0) { // Like button pressed again
                    result = db.removeLike(idx); // Removes a like from the message
                    result = db.setLikeStatus(userID, idx, 0); // Updates like status
                    return gson.toJson(new StructuredResponse("ok", null, result));
                } else if (like_or_dislike == 1) { // If dislike button is pressed
                    result = db.addDislike(idx); // Adds a dislike
                    result = db.removeLike(idx); // Removes a like
                    result = db.setLikeStatus(userID, idx, -1); // Updates the like status for the user
                    return gson.toJson(new StructuredResponse("ok", null, result));
                } else {
                    return gson.toJson(new StructuredResponse("error", "unable to like or dislike " + idx, null));
                }
            } else if (status == -1) { // If message is disliked
                if (like_or_dislike == 0) { // Like button pressed
                    result = db.addLike(idx); // Adds the like
                    result = db.removeDislike(idx); // Removes the dislike
                    result = db.setLikeStatus(userID, idx, 1); // Sets new like status
                    return gson.toJson(new StructuredResponse("ok", null, result));
                } else if (like_or_dislike == 1) { // If disliked
                    result = db.removeDislike(idx); // Removes the dislike
                    result = db.setLikeStatus(userID, idx, 0); // Sets new like status
                    return gson.toJson(new StructuredResponse("ok", null, result));
                } else {
                    return gson.toJson(new StructuredResponse("error", "unable to like or dislike " + idx, null));
                }
            } else {
                return gson.toJson(new StructuredResponse("error", "unable to like or dislike " + idx, null));
            }

        });

        /** End of indiviudal like scenario */

        /** Update Comment */
        Spark.put("/messages/:id/:userID/:commentId", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500-b
            int idx = Integer.parseInt(request.params("id"));
            int userId = Integer.parseInt(request.params("userID"));
            int comId = Integer.parseInt(request.params("commentId"));
            SimpleComRequest req = gson.fromJson(request.body(), SimpleComRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            DataComRow comment = db.selectCommentOne(comId);
            int comCreatorId = comment.mUserId;

            if (userId == comCreatorId) {
                int result = db.changeComment(comId, req.mComment);
                return gson.toJson(new StructuredResponse("ok", null, result));
            } else {
                System.out.print("You did not write this comment");
                return gson.toJson(new StructuredResponse("ok", "Cannot Change Comment", null));
            }
        });

        // Hashtable<String,String> users_hashtable = new Hashtable<String, String>();

        // OAuth
        Spark.post("/oauth", (request, response) -> {
            RequestOAuth reqOAuth = gson.fromJson(request.body(), RequestOAuth.class);
            String id_token = reqOAuth.id_token;
            response.status(200);
            return gson.toJson(new StructuredResponse("ok", null, OAuth.OAuthAuthorize(id_token)));
        });

        // Hashtable<String,String> users_hashtable = new Hashtable<String, String>();

        // OAuth
        Spark.post("/oauth", (request, response) -> {
            RequestOAuth reqOAuth = gson.fromJson(request.body(), RequestOAuth.class);
            String id_token = reqOAuth.id_token;
            response.status(200);
            return gson.toJson(new StructuredResponse("ok", null, OAuth.OAuthAuthorize(id_token)));
        });

        /**End Google API */


        
    } // This ends the main method
    
    public static String fileUpload(Drive service,String fileName, String fileUrl) throws IOException{
        //get the bytes
        byte[] byteArray = fileUrl.getBytes();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(byteArray);
         }
         //now that we have a file, shove it to GDrive and also into cache
         File fileMetadata = new File();
        fileMetadata.setName(fileName);
        java.io.File filePath = new java.io.File(fileName);
        String fileType="";
        if(fileName.contains(".jpg")||fileName.contains(".jfif")){
            fileType="image/jpeg";
        }
        else if(fileName.contains(".png")){
            fileType="image/png";
        }
        else if(fileName.contains(".jfif")){
            fileType="image/jfif";
        }
        else{
            fileType="text/"+fileName.split(".")[1];
        }
        FileContent mediaContent = new FileContent(fileType, filePath);
        File file = service.files().create(fileMetadata, mediaContent)
        .setFields("id")
        .execute();
         //now we have to get the link
         //service.files().get().setFields("webViewLink");
         String pageToken = null;
         String link="";
        do {
        FileList result = service.files().list()
            .setQ("id='"+file.getId()+"'")
            .setSpaces("drive")
            .setFields("nextPageToken, webViewLink")
            .setPageToken(pageToken)
            .execute();
        for (File file1 : result.getFiles()) {
            link=file1.getWebViewLink();
            break;
        }
        pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //now delete the local file
        filePath.delete();
        //since we're done, return the file link
        return link;
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

    /***
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
     */
     

}