
package edu.lehigh.cse216.kel323.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should
     * be null. Otherwise, there is a valid open connection
     */
    Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectComAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mComInsertOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mProfInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateSesTable;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateComTable;
    
    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateLikeTable;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateProfileTable;


    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mDropProfileTable;


    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropSesTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropComTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropLikeTable;

    /**
     * A prepared statement for incrementing like/dislike count
     */
    private PreparedStatement mIncrementLike;
    private PreparedStatement mIncrementDislike;

    /**
     * A prepared statement for decrementing like/dislike count
     */
    private PreparedStatement mDecrementLike;
    private PreparedStatement mDecrementDislike;

    /**
     * A prepared statement for getting like/dislike count
     */
    private PreparedStatement mGetLikeCount;
    private PreparedStatement mGetDislikeCount;

    /**
     * A prepared statement for getting like/dislike status
     */
    private PreparedStatement mGetLikeStatus;

    private PreparedStatement mSetLikeStatus;

    private PreparedStatement mGetComments;

    private PreparedStatement mGetSingleComment;

    private PreparedStatement mInsertNewUser;
    private PreparedStatement mInsertUserSes;

    private PreparedStatement mFindEmail;

    private PreparedStatement mInsertProfOne;

    private PreparedStatement mUpdateComment;

    private PreparedStatement mProfileInformation;

    private PreparedStatement mInsertLikeRow;

    private Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();

    //THESE PREPARED STATEMENTS ARE USED TO PROVIDE THE KEY INFO AND USED IN THE GETDATABASE MEHTOD
    //The methods below are used to tell these prepared statements what to do

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url) {
        // static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +"?sslmode=require";
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }
        /*try {
            Connection conn = DriverManager.getConnection(
                    "postgres://ytwhcceyowndem:67468aedb6de7dfc4dbb32e8c2c07d54ecbccbf3b506b7e79ab5ebac6091f4b2@ec2-34-224-226-38.compute-1.amazonaws.com:5432/dc1vnlqcg514jo");
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }*/
        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            // SQL incorrectly. We really should have things like "tblData"
            // as constants, and then build the strings for the statements
            // from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception

            //This creates all the desired tables that will be stored in the database
            db.mCreateTable = db.mConnection.prepareStatement(
                    "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50), userId INT," 
                            + "message VARCHAR(500), likes INT, dislikes INT)");

            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE tblData");

            db.mCreateSesTable = db.mConnection.prepareStatement(
                    "CREATE TABLE sessionData (userId SERIAL PRIMARY KEY," 
                            + "sessionId INT)");
            

            db.mDropSesTable = db.mConnection.prepareStatement("DROP TABLE sessionData");

            db.mCreateComTable = db.mConnection.prepareStatement( //Creates data for comments table
                    "CREATE TABLE comData (id SERIAL PRIMARY KEY, commment VARCHAR(50), userId INT, comId INT)");
            db.mDropComTable = db.mConnection.prepareStatement("DROP TABLE comData");

            db.mCreateLikeTable = db.mConnection.prepareStatement( //Creates a table that stores which are liked. 1 for liked. 0 for neutral. -1 for disliked.
                    "CREATE TABLE likeData (userId SERIAL PRIMARY KEY, id INT, likeStatus INT)");
            db.mDropLikeTable = db.mConnection.prepareStatement("DROP TABLE likeData");

            db.mCreateProfileTable = db.mConnection.prepareStatement( //Creates a table that stores which are liked. 1 for liked. 0 for neutral. -1 for disliked.
                    "CREATE TABLE profData (userId SERIAL PRIMARY KEY, username VARCHAR(20), email VARCHAR(20))");
            db.mDropProfileTable = db.mConnection.prepareStatement("DROP TABLE profData");


            

            // Standard CRUD operations tblData
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?, ?, 0, 0)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ? WHERE id = ?");

            // Standard CRUD operations sesData
            //db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM sesData WHERE id = ?");
            //db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO sesData VALUES (default, ?)");
            //db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM sesData");
            //db.mSelectOne = db.mConnection.prepareStatement("SELECT * from sesData WHERE id=?");
            //db.mUpdateOne = db.mConnection.prepareStatement("UPDATE sesData SET message = ? WHERE id = ?");

            // Standard CRUD operations comData
            //db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM comData WHERE id = ?");
            //db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO comData VALUES (default, ?, ?)");
            //db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM comData");
            //db.mSelectOne = db.mConnection.prepareStatement("SELECT * from comData WHERE id=?");
            //db.mUpdateOne = db.mConnection.prepareStatement("UPDATE comData SET comment = ? WHERE commentId = ?");

            // Standard CRUD operations likeData
            //db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM likeData WHERE id = ?");
            //db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO likeData VALUES (default, ?, ?, 0, 0)");
            //db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM likeData");
            //db.mSelectOne = db.mConnection.prepareStatement("SELECT * from likeData WHERE id=?");
            //db.mUpdateOne = db.mConnection.prepareStatement("UPDATE likeData SET message = ? WHERE id = ?");
            
            
            // liking
            db.mIncrementLike = db.mConnection.prepareStatement("UPDATE tblData SET likes = ? WHERE id = ?");
            db.mIncrementDislike = db.mConnection.prepareStatement("UPDATE tblData SET dislikes = ? WHERE id = ?");

            db.mDecrementLike = db.mConnection.prepareStatement("UPDATE tblData SET likes = ? WHERE id = ?");
            db.mDecrementDislike = db.mConnection.prepareStatement("UPDATE tblData SET dislikes = ? WHERE id = ?");

            // get like count
            db.mGetLikeCount = db.mConnection.prepareStatement("SELECT likes FROM tblData WHERE id = ?");
            db.mGetDislikeCount = db.mConnection.prepareStatement("SELECT dislikes FROM tblData WHERE id = ?");

            //get comments
            //This will only get one comment. I dont know if I even need it
            db.mGetComments = db.mConnection.prepareStatement("SELECT * from comData WHERE id = ?");
            db.mComInsertOne = db.mConnection.prepareStatement("INSERT INTO comData VALUES (default, ?, ?, ?)");
            //db.mSelectComAll = db.mConnection.prepareStatement("SELECT * FROM comData WHERE id = ?");

            db.mGetLikeStatus = db.mConnection.prepareStatement("SELECT likeStatus FROM likeData WHERE id = ? AND userID = ?"); //How do I give two parameters to the prepared statement

            db.mSetLikeStatus = db.mConnection.prepareStatement("UPDATE likeData SET likeStatus = ? WHERE id = ? AND userID = ?"); //Need to do the same as above
            //I think I will need a getComments section that will provide all comments to the database that can then be returned
            //I think I will also need to return a given like status for a given person to see if they can like it again

            //This will create a userId for an individual and a session Id if they have already previously logged in
            //Call each depending on if user has logged in before
            db.mInsertNewUser = db.mConnection.prepareStatement("INSERT INTO sesData VALUES (default, default)");
            db.mInsertUserSes = db.mConnection.prepareStatement("INSERT INTO sesData VALUES (?, default)");

            db.mFindEmail = db.mConnection.prepareStatement("Select userId FROM profData WHERE email = ?");

            db.mInsertProfOne = db.mConnection.prepareStatement("INSERT INTO profData VALUES (default, ?, ?)");

            db.mGetSingleComment = db.mConnection.prepareStatement("Select * FROM comData WEHRE commentId = ?");

            db.mUpdateComment = db.mConnection.prepareStatement("UPDATE comData SET comment = ? WEHRE commentId = ?");

            db.mProfileInformation = db.mConnection.prepareStatement("SELECT * FROM profData WHERE userId = ?");

            db.mInsertLikeRow = db.mConnection.prepareStatement("INSERT INTO likeData VALUES (?, ?, ?)");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String subject, String message, int userId) {
        int count = 0;
       
        try {
            
            mInsertOne.setString(1, subject);
            mInsertOne.setString(3, message);
            mInsertOne.setInt(2, userId);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }




    /**
     * Insert a row into the comment table in database
     * 
     * @param comment The comment for this new rowfind
     * 
     * 
     * @return The number of rows that were inserted
     */
    int insertComRow(String comment, int userID, int messID) {
        int count = 0;
        try {
            mComInsertOne.setString(1, comment);
            mComInsertOne.setInt(2, userID);
            mComInsertOne.setInt(3, messID);
            count += mComInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertProfRow(String username, String email) {
        int count = 0;
        try {
            mInsertProfOne.setString(1, username);
            mInsertProfOne.setString(2, email);
            count += mProfInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<DataRow> selectAll() {
        ArrayList<DataRow> res = new ArrayList<DataRow>(); //Thoughts on creating an array and setting the array to be a string of comments - or an arraylist
        //With array of comments I could add that as a section of the data row
        //Then include it as part of the structured response
        //Then the front end will need to iterate through the array to produce the comments to be displayed
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) { //Gets all of the information for
                res.add(new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getInt("userId"), rs.getString("message"), rs.getInt("likes"),
                rs.getInt("dislikes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    DataRow selectOne(int id) { //Similar to above but I could just use 1 array instead of a new one for each list
        DataRow res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getInt("userId"), rs.getString("message"), rs.getInt("likes"),
                        rs.getInt("dislikes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id      The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createLikeTable() {
        try {
            mCreateLikeTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createSesTable() {
        try {
            mCreateSesTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createProfileTable() {
        try {
            mCreateProfileTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createComTable() {
        try {
            mCreateComTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropLikeTable() {
        try {
            mDropLikeTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropSesTable() {
        try {
            mDropSesTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropComTable() {
        try {
            mDropComTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates/increments like.
     */
    int addLike(int id) {
        int res = -1;
        try {
            mIncrementLike.setInt(1, getLikeCount(id) + 1);
            System.out.println("Before incremnet" + getLikeCount(id));
            mIncrementLike.setInt(2, id);
            res = mIncrementLike.executeUpdate();
            System.out.println("Updated: " + getLikeCount(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // returns -1 on error
    }

    int addDislike(int id) {
        int res = -1;
        try {
            mIncrementDislike.setInt(1, getDislikeCount(id) + 1);
            System.out.println(getDislikeCount(id));
            mIncrementDislike.setInt(2, id);
            res = mIncrementDislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // returns -1 on error
    }


    int removeLike(int id) {
        int res = -1;
        try {
            mDecrementLike.setInt(1, getLikeCount(id) - 1);
            //System.out.println(getLikeCount(id));
            mDecrementLike.setInt(2, id);
            res = mIncrementLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // returns -1 on error
    }

    int removeDislike(int id) {
        int res = -1;
        try {
            mDecrementDislike.setInt(1, getDislikeCount(id) - 1);
            //System.out.println(getDislikeCount(id));
            //mIncrementDislike.setInt(2, id);
            mDecrementDislike.setInt(2, id);
            res = mDecrementDislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // returns -1 on error
    }

    int getLikeCount(int id) {
        int count = 0;
        try {
            mGetLikeCount.setInt(1, id);
            ResultSet rs = mGetLikeCount.executeQuery();
            if (rs.next())
                count = rs.getInt(1);
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int getDislikeCount(int id) {
        int count = 0;
        try {
            mGetDislikeCount.setInt(1, id);
            ResultSet rs = mGetDislikeCount.executeQuery();
            if (rs.next())
                count = rs.getInt(1);
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int getLikeStatus(int userID, int id){
        int likeStatus = -2;
        try {
            mGetLikeStatus.setInt(1, id); //How does setInt work? //Do I need to use some kind of super key here?
            mGetLikeStatus.setInt(2, userID);
            
            ResultSet rs = mGetLikeStatus.executeQuery();

            if((rs != null) && (rs.next())){
                likeStatus = rs.getInt(1);
            }
            System.out.println("Getting Like Status");
            return likeStatus;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //How do I get these to work
    int setLikeStatus(int userID, int id, int val) {
        int res = -2;
        try {
            mSetLikeStatus.setInt(1, val); //How do these work
            //System.out.println(getLikeCount(id));
            mSetLikeStatus.setInt(2, id);
            mSetLikeStatus.setInt(3, userID);
            mSetLikeStatus.executeUpdate();
            System.out.println("Like status set");
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // returns -1 on error
    }

    void insertLikeRow(int userId, int id){
        try{
            mInsertLikeRow.setInt(1, userId);
            mInsertLikeRow.setInt(2, id);
            mInsertLikeRow.setInt(3, 0);
            mInsertLikeRow.executeUpdate();
            System.out.println("Row Added Sucess");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Gives all the comments in the form of an array list for a given message id */
    ArrayList<DataComRow> getComments(int id) {
    ArrayList<DataComRow> res = new ArrayList<DataComRow>(); //Thoughts on creating an array and setting the array to be a string of comments - or an arraylist
        //With array of comments I could add that as a section of the data row
        //Then include it as part of the structured response
        //Then the front end will need to iterate through the array to produce the comments to be displayed
        try {
            mGetComments.setInt(1, id);
            ResultSet rs = mGetComments.executeQuery();
            while (rs.next()) { //Gets all of the information for
                res.add(new DataComRow(rs.getInt("id"), rs.getString("commment"), rs.getInt("userId"), rs.getInt("comId")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    /**
     * Insert a row into the database
     * 
     * @param None
     * 
     * @return The number of new users added
     */
    int insertNewUser() {
        int count = 0;
        try {
            count += mInsertNewUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertUserSes(int userID) {
        int count = 0;
        try {
            mInsertUserSes.setInt(1, userID);
            count += mInsertUserSes.executeUpdate();
            ht.put(hashFunction(count), userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int userExists(String email) {
        int exists = -1;
        try {
            mFindEmail.setString(1, email);
            ResultSet rs = mFindEmail.executeQuery();
            exists = rs.getInt("userId");
          
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }


    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    DataComRow selectCommentOne(int comId) { //Similar to above but I could just use 1 array instead of a new one for each list
        DataComRow res = null;
        try {
            mGetSingleComment.setInt(1, comId);
            ResultSet rs = mGetSingleComment.executeQuery();
            if (rs.next()) {
                res = new DataComRow(rs.getInt("comId"), rs.getString("message"), rs.getInt("userId"), rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //Allows update of comments
    int changeComment(int comId, String newCom) { //Similar to above but I could just use 1 array instead of a new one for each list
        try {
            mUpdateComment.setString(1, newCom);
            mUpdateComment.setInt(2, comId);
            mUpdateComment.executeQuery();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
        
    }

    //Gets profile information
    String[] getProfile(int userId) { //Similar to above but I could just use 1 array instead of a new one for each list
        String username = null;
        String email = null;
        try {
            mProfileInformation.setInt(1, userId);
            ResultSet rs = mProfileInformation.executeQuery();
            while(rs.next()){
                username = rs.getString("username");
                email = rs.getString("email");
            }

            String[] array = new String[2];
            array[0] = username;
            array[1] = email;
            return array;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[0];  
    }

    //Very simple hash function
    int hashFunction(int key){
        return key+5;
    }


}