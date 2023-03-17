
package edu.lehigh.cse216.kel323.admin;

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

    private PreparedStatement mDeleteComRow;

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

    private PreparedStatement mDoesUserExistInLikeDataTbl;

    private PreparedStatement mProfileNoteTable;
    private PreparedStatement mDropProfileNoteTable;
    private PreparedStatement mSelectProfileNote;
    private PreparedStatement mEditProfileNote;
    private PreparedStatement mInsertProfileNoteRow;
    private PreparedStatement mAddColumn;
    private PreparedStatement mDeleteProfRow;

    private Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();

    // THESE PREPARED STATEMENTS ARE USED TO PROVIDE THE KEY INFO AND USED IN THE
    // GETDATABASE MEHTOD
    // The methods below are used to tell these prepared statements what to do

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
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
                    + "?sslmode=require";
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
        /*
         * try {
         * Connection conn = DriverManager.getConnection(
         * "postgres://ytwhcceyowndem:67468aedb6de7dfc4dbb32e8c2c07d54ecbccbf3b506b7e79ab5ebac6091f4b2@ec2-34-224-226-38.compute-1.amazonaws.com:5432/dc1vnlqcg514jo"
         * );
         * if (conn == null) {
         * System.err.
         * println("Error: DriverManager.getConnection() returned a null object");
         * return null;
         * }
         * db.mConnection = conn;
         * } catch (SQLException e) {
         * System.err.
         * println("Error: DriverManager.getConnection() threw a SQLException");
         * e.printStackTrace();
         * return null;
         * }
         */
        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            // SQL incorrectly. We really should have things like "tblData"
            // as constants, and then build the strings for the statements
            // from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception

            // This creates all the desired tables that will be stored in the database
            db.mCreateTable = db.mConnection.prepareStatement(
                    "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50), userId INT,"
                            + "message VARCHAR(500), likes INT, dislikes INT)"); // ( filename, filelink, size )

            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE tblData");

            db.mCreateSesTable = db.mConnection.prepareStatement(
                    "CREATE TABLE sessionData (userId SERIAL PRIMARY KEY,"
                            + "sessionId INT)");

            db.mDropSesTable = db.mConnection.prepareStatement("DROP TABLE sessionData");

            db.mCreateComTable = db.mConnection.prepareStatement( // Creates data for comments table
                    "CREATE TABLE comData (comId SERIAL PRIMARY KEY, comment VARCHAR(50), userId INT, msgId INT)");
            db.mDropComTable = db.mConnection.prepareStatement("DROP TABLE comData");

            db.mCreateLikeTable = db.mConnection.prepareStatement( // Creates a table that stores which are liked. 1 for
                                                                   // liked. 0 for neutral. -1 for disliked.
                    "CREATE TABLE likeData (userId INT, id INT, likeStatus INT, PRIMARY KEY(userId, id))");
            db.mDropLikeTable = db.mConnection.prepareStatement("DROP TABLE likeData");

            db.mCreateProfileTable = db.mConnection.prepareStatement( // Creates a table that stores which are liked. 1
                                                                      // for liked. 0 for neutral. -1 for disliked.
                    "CREATE TABLE profData (userId SERIAL PRIMARY KEY, username VARCHAR(20), email VARCHAR(20))");
            db.mDropProfileTable = db.mConnection.prepareStatement("DROP TABLE profData");

            db.mProfileNoteTable = db.mConnection.prepareStatement(
                    "CREATE TABLE profNote (userID INT, note VARCHAR(50), PRIMARY KEY(userId))");
            db.mDropProfileNoteTable = db.mConnection.prepareStatement("DROP TABLE profNote");

            // Standard CRUD operations tblData
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection
                    .prepareStatement(
                            "INSERT INTO tblData (id, subject, message, userid, likes, dislikes, filename, filelink, filesize) VALUES (default, ?, ?, ?, 0, 0,?,?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection
                    .prepareStatement(
                            "UPDATE tblData SET message = ?, filename = ?, filelink = ?, filesize = ? WHERE id = ?");

            db.mAddColumn = db.mConnection
                    .prepareStatement("ALTER TABLE tblData ALTER COLUMN filesize TYPE VARCHAR(255)");

            // ALTER TABLE table_name MODIFY COLUMN column_name datatype

            // Standard CRUD operations sesData
            // db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM sesData WHERE id
            // = ?");
            // db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO sesData VALUES
            // (default, ?)");
            // db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM sesData");
            // db.mSelectOne = db.mConnection.prepareStatement("SELECT * from sesData WHERE
            // id=?");
            // db.mUpdateOne = db.mConnection.prepareStatement("UPDATE sesData SET message =
            // ? WHERE id = ?");

            // Standard CRUD operations comData
            // db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM comData WHERE id
            // = ?");
            // db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO comData VALUES
            // (default, ?, ?)");
            // db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM comData");
            // db.mSelectOne = db.mConnection.prepareStatement("SELECT * from comData WHERE
            // id=?");
            // db.mUpdateOne = db.mConnection.prepareStatement("UPDATE comData SET comment =
            // ? WHERE commentId = ?");

            // Standard CRUD operations likeData
            // db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM likeData WHERE
            // id = ?");
            // db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO likeData VALUES
            // (default, ?, ?, 0, 0)");
            // db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM likeData");
            // db.mSelectOne = db.mConnection.prepareStatement("SELECT * from likeData WHERE
            // id=?");
            // db.mUpdateOne = db.mConnection.prepareStatement("UPDATE likeData SET message
            // = ? WHERE id = ?");

            // liking
            db.mIncrementLike = db.mConnection.prepareStatement("UPDATE tblData SET likes = ? WHERE id = ?");
            db.mIncrementDislike = db.mConnection.prepareStatement("UPDATE tblData SET dislikes = ? WHERE id = ?");

            db.mDecrementLike = db.mConnection.prepareStatement("UPDATE tblData SET likes = ? WHERE id = ?");
            db.mDecrementDislike = db.mConnection.prepareStatement("UPDATE tblData SET dislikes = ? WHERE id = ?");

            // get like count
            db.mGetLikeCount = db.mConnection.prepareStatement("SELECT likes FROM tblData WHERE id = ?");
            db.mGetDislikeCount = db.mConnection.prepareStatement("SELECT dislikes FROM tblData WHERE id = ?");

            // get comments
            // This will only get one comment. I dont know if I even need it
            db.mGetComments = db.mConnection.prepareStatement("SELECT * from comData WHERE msgId = ?");
            db.mComInsertOne = db.mConnection
                    .prepareStatement(
                            "INSERT INTO comData (comid, comment, userid, msgid, filename, filelink, filesize) VALUES (default, ?,?,?,?,?,?)");
            db.mDeleteComRow = db.mConnection.prepareStatement("DELETE FROM comData WHERE comId = ? ");
            // db.mSelectComAll = db.mConnection.prepareStatement("SELECT * FROM comData
            // WHERE id = ?");

            db.mGetLikeStatus = db.mConnection
                    .prepareStatement("SELECT likeStatus FROM likeData WHERE id = ? AND userID = ?"); // How do I give
                                                                                                      // two parameters
                                                                                                      // to the prepared
                                                                                                      // statement

            db.mSetLikeStatus = db.mConnection
                    .prepareStatement("UPDATE likeData SET likeStatus = ? WHERE id = ? AND userID = ?"); // Need to do
                                                                                                         // the same as
                                                                                                         // above
            // I think I will need a getComments section that will provide all comments to
            // the database that can then be returned
            // I think I will also need to return a given like status for a given person to
            // see if they can like it again

            // This will create a userId for an individual and a session Id if they have
            // already previously logged in
            // Call each depending on if user has logged in before
            db.mInsertNewUser = db.mConnection.prepareStatement("INSERT INTO sesData VALUES (default, default)");
            db.mInsertUserSes = db.mConnection.prepareStatement("INSERT INTO sesData VALUES (?, default)");

            db.mFindEmail = db.mConnection.prepareStatement("Select userId FROM profData WHERE email = ?");

            db.mInsertProfOne = db.mConnection.prepareStatement("INSERT INTO profData VALUES (default, ?, ?)");

            db.mGetSingleComment = db.mConnection.prepareStatement("Select * FROM comData WHERE comId = ?");

            db.mUpdateComment = db.mConnection.prepareStatement(
                    "UPDATE comData SET comment = ?, filename = ?, filelink = ?, filesize = ?  WHERE comId = ?");
            // "UPDATE commentData SET comment = ?, filename = ?, filelink = ?, Filesize = ?
            // WHERE id = ?"

            db.mProfileInformation = db.mConnection.prepareStatement("SELECT * FROM profData WHERE userId = ?");

            db.mInsertLikeRow = db.mConnection.prepareStatement("INSERT INTO likeData VALUES (?, ?, ?)");

            db.mDoesUserExistInLikeDataTbl = db.mConnection
                    .prepareStatement("SELECT * from likeData WHERE id = ? AND userId = ?");

            db.mSelectProfileNote = db.mConnection.prepareStatement("SELECT note from profNote WHERE userId = ?");
            db.mInsertProfileNoteRow = db.mConnection.prepareStatement("INSERT INTO profNote VALUES (?, ?)");
            db.mEditProfileNote = db.mConnection.prepareStatement("UPDATE profNote SET note = ? WHERE userId = ?");
            db.mDeleteProfRow = db.mConnection.prepareStatement("DELETE FROM profData WHERE userId = ?");
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

    int deleteProfRow(int id){
        int res = -1;
        try {
            mDeleteProfRow.setInt(1, id);
            return mDeleteProfRow.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
    }

    int deleteComRow(int comId){
        int res = -1;
        try {
            mDeleteComRow.setInt(1, comId);
            return mDeleteComRow.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
    }

    void dropProfileTable(){
        try {
            mDropProfileTable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int insertDefaultProfileNoteRow(int userId) {
        int res = -1;
        String note = "";
        try {
            mInsertProfileNoteRow.setInt(1, userId);
            mInsertProfileNoteRow.setString(2, note);
            mInsertProfileNoteRow.executeUpdate();
            return res = 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
    }

    String getProfileNote(int userId) {
        String profileNote = null;
        try {
            mSelectProfileNote.setInt(1, userId);
            ResultSet rs = mSelectProfileNote.executeQuery();
            if (rs.next())
                profileNote = rs.getString("note");
            return profileNote;
        } catch (SQLException e) {
            e.printStackTrace();
            return profileNote;
        }
    }

    int editProfileNote(int userId, String note) {
        int res = -1;
        try {
            mEditProfileNote.setString(1, note);
            mEditProfileNote.setInt(2, userId);
            mEditProfileNote.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
    }

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
    int insertRow(String subject, String message, int user_id, String filename, String filelink, float filesize) {
        int count = 0;

        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            mInsertOne.setInt(3, user_id);
            mInsertOne.setString(4, filename);
            mInsertOne.setString(5, filelink);
            mInsertOne.setFloat(6, filesize);
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
    int insertComRow(String comment, int userID, int messID, String filename, String filelink, Float filesize) {
        int count = 0;
        try {
            mComInsertOne.setString(1, comment);
            mComInsertOne.setInt(2, userID);
            mComInsertOne.setInt(3, messID);
            mComInsertOne.setString(4, filename);
            mComInsertOne.setString(5, filelink);
            mComInsertOne.setFloat(6, filesize);
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
            count += mInsertProfOne.executeUpdate();
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
        ArrayList<DataRow> res = new ArrayList<DataRow>(); // Thoughts on creating an array and setting the array to be
                                                           // a string of comments - or an arraylist
        // With array of comments I could add that as a section of the data row
        // Then include it as part of the structured response
        // Then the front end will need to iterate through the array to produce the
        // comments to be displayed
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) { // Gets all of the information for
                res.add(new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getInt("userId"),
                        rs.getString("message"), rs.getInt("likes"),
                        rs.getInt("dislikes"), rs.getString("filename"), rs.getString("filelink")));
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
    DataRow selectOne(int id) { // Similar to above but I could just use 1 array instead of a new one for each
                                // list
        DataRow res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getInt("userId"),
                        rs.getString("message"), rs.getInt("likes"),
                        rs.getInt("dislikes"), rs.getString("filename"), rs.getString("filelink"));
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
    // UPDATE tblData SET message = ?, filename = ?, filelink = ?, filesize = ?
    // WHERE id = ?
    int updateOne(int id, String message, String filename, String fileLink, float fileSize) {
        int res = -1;
        try {
            mUpdateOne.setInt(5, id);
            mUpdateOne.setString(1, message);
            mUpdateOne.setString(2, filename);
            mUpdateOne.setString(3, fileLink);
            mUpdateOne.setFloat(4, fileSize);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    void createProfileNoteTable() {
        try {
            mProfileNoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropProfileNoteTable() {
        try {
            mDropProfileNoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void AddColumn() {
        try {
            mAddColumn.executeUpdate();
            System.out.println("SUCCESS!!!!!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            System.out.println("RES RETURN ON ADDLIKE: " + res);
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
            // System.out.println(getLikeCount(id));
            mDecrementLike.setInt(2, id);
            res = mDecrementLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // returns -1 on error
    }

    int removeDislike(int id) {
        int res = -1;
        try {
            mDecrementDislike.setInt(1, getDislikeCount(id) - 1);
            // System.out.println(getDislikeCount(id));
            // mIncrementDislike.setInt(2, id);
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

    int getLikeStatus(int userID, int id) {
        int likeStatus = 0;
        try {
            mGetLikeStatus.setInt(1, id); // How does setInt work? //Do I need to use some kind of super key here?
            mGetLikeStatus.setInt(2, userID);

            ResultSet rs = mGetLikeStatus.executeQuery();

            if (rs.next()) {
                likeStatus = rs.getInt(1);
                System.out.println("1) LIKE STATUS FROM FXN CALL: " + likeStatus);
            }

            System.out.println("2) LIKE STATUS FROM FXN CALL: " + likeStatus);
            return likeStatus;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // How do I get these to work
    void setLikeStatus(int userID, int id, int val) {
        try {
            mSetLikeStatus.setInt(1, val); // How do these work
            // System.out.println(getLikeCount(id));
            mSetLikeStatus.setInt(2, id);
            mSetLikeStatus.setInt(3, userID);
            mSetLikeStatus.executeUpdate();
            System.out.println("Like status set to: " + val);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insertLikeRow(int userId, int id, int status) {
        try {
            mInsertLikeRow.setInt(1, userId);
            mInsertLikeRow.setInt(2, id);
            mInsertLikeRow.setInt(3, status);
            mInsertLikeRow.executeUpdate();
            System.out.println("Row Added Sucess");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Gives all the comments in the form of an array list for a given message id */
    ArrayList<DataComRow> getComments(int msgId) {
        ArrayList<DataComRow> res = new ArrayList<DataComRow>(); // Thoughts on creating an array and setting the array
                                                                 // to be a string of comments - or an arraylist
        // With array of comments I could add that as a section of the data row
        // Then include it as part of the structured response
        // Then the front end will need to iterate through the array to produce the
        // comments to be displayed
        try {
            mGetComments.setInt(1, msgId);
            ResultSet rs = mGetComments.executeQuery();
            while (rs.next()) { // Gets all of the information for
                res.add(new DataComRow(rs.getInt("comId"), rs.getString("comment"), rs.getInt("userId"),
                        rs.getInt("msgId"), rs.getString("filename"), rs.getString("filelink")));
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
    int getUserId(String email) {
        int userId = -1;
        try {
            mFindEmail.setString(1, email);
            ResultSet rs = mFindEmail.executeQuery();
            if (rs.next())
                userId = rs.getInt("userId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    DataComRow selectCommentOne(int comId) { // Similar to above but I could just use 1 array instead of a new one for
                                             // each list
        DataComRow res = null;
        try {
            mGetSingleComment.setInt(1, comId);
            ResultSet rs = mGetSingleComment.executeQuery();
            if (rs.next()) {
                res = new DataComRow(rs.getInt("comId"), rs.getString("comment"), rs.getInt("userId"),
                        rs.getInt("msgId"), rs.getString("filename"), rs.getString("filelink"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // Allows update of comments
    // "UPDATE comData SET comment = ?, filename = ?, filelink = ?, filesize = ?
    // WHERE comId = ?");
    int changeComment(int comId, String newCom, String filename, String fileLink, float fileSize) { // Similar to above
                                                                                                  // but I could just
        int res = -1;

        // for each list
        try {
            mUpdateComment.setInt(5, comId);
            mUpdateComment.setString(1, newCom);
            mUpdateComment.setString(2, filename);
            mUpdateComment.setString(3, fileLink);
            mUpdateComment.setFloat(4, fileSize);
            res = mUpdateComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;

    }

    // Gets profile information
    ProfileRow getProfile(int userId) { // Similar to above but I could just use 1 array instead of a new one for each
                                        // list
        // String username = null;
        // String email = null;
        ProfileRow profileRow = new ProfileRow();
        try {
            mProfileInformation.setInt(1, userId);
            ResultSet rs = mProfileInformation.executeQuery();
            while (rs.next()) {
                profileRow = new ProfileRow(rs.getInt("userId"), rs.getString("username"), rs.getString("email"));
            }
            rs.close();
            System.out.println("PROFILE USER ID: " + profileRow.userId);
            System.out.println("PROFILE USERNAME: " + profileRow.username);
            System.out.println("PROFILE EMAIL: " + profileRow.email);
            return profileRow;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileRow;
    }

    boolean doesUserExistInLikeDataTbl(int msgId, int userId) {
        try {
            mDoesUserExistInLikeDataTbl.setInt(1, msgId);
            mDoesUserExistInLikeDataTbl.setInt(2, userId);
            ResultSet rs = mDoesUserExistInLikeDataTbl.executeQuery();
            if (rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Very simple hash function
    int hashFunction(int key) {
        return key + 5;
    }

}