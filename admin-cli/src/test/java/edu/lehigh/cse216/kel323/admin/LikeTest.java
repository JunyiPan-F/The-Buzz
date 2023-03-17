package edu.lehigh.cse216.kel323.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import spark.Spark;
import java.util.Map;
import java.lang.reflect.Field;


public class LikeTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LikeTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( LikeTest.class );
    }

    /**
     * Multiple classes of tests TEST - THIS CLASS IS NOT USED
     */
    public void testLike()
    {
        
        
        // 
        assertTrue( true );
        // int curLikes = db.getLikeCount(1);
        // db.addLike(1);
		// int newLike = db.getLikeCount(1);
		
		//assertTrue( true );

        //db.removeLike(1);

        //Test adding dislike
        //Test adding Like
        //Test updating a comment
    }

    // public void testLike()
    // {
    //     Database db = connect();
        
    //     int curLikes = db.getLikeCount(1);
    //     db.addLike(1);

    //     assertTrue(db.getLikeCount(1) == curLikes +1);

    //     db.removeLike(1);

    //     //Test adding dislike
    //     //Test adding Like
    //     //Test updating a comment
    // }

    public Database connect()
    {
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
            return null;
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

        return db;

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
