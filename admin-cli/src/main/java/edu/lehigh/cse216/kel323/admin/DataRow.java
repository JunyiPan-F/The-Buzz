package edu.lehigh.cse216.kel323.admin;

import java.util.Date;

/**
 * DataRow holds a row of information. A row of information consists of
 * an identifier, strings for a "title" and "content", and a creation date.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class DataRow {
    /**
     * The unique identifier associated with this element. It's final, because
     * we never want to change it.
     */
    public final int mId;

    /**
     * The title for this row of data
     */
    public String mTitle;

    /**
     * The content for this row of data
     */
    public String mContent;

    /**
     * The creation date for this row of data. Once it is set, it cannot be
     * changed
     */
    public final Date mCreated;

    /**
     * Like count of the message.
     */
    public int mLikeCount;

    /**
     * Like dislike count of the message.
     */
    public int mDislikeCount;

    /**
     * Like dislike count of the message.
     */
    public int mUserId;

    /**
     * Name of uploaded file.
     */
    public String mFileName;

    /**
     * Link of uploaded file.
     */
    public String mFileLink;

    /**
     * Create a new DataRow with the provided id and title/content, and a
     * creation date based on the system clock at the time the constructor was
     * called
     * 
     * @param id      The id to associate with this row. Assumed to be unique
     *                throughout the whole program.
     * 
     * @param title   The title string for this row of data
     * 
     * @param content The content string for this row of data
     */
    DataRow(int id, String title, int userId, String content) {
        mId = id;
        mTitle = title;
        mContent = content;
        mUserId = userId;
        mCreated = new Date();
        mLikeCount = 0;
        mDislikeCount = 0;
        mFileName = " ";
        mFileLink = " ";
    }

    DataRow(int id, String title, int userId, String content, int likes, int dislikes, String fileName,
            String fileLink) { // Update for userID and String
        // Array of Comments - iterate
        // thrrough and add addcomment
        // button
        mId = id;
        mTitle = title;
        mUserId = userId;
        mContent = content;
        mCreated = new Date();
        mLikeCount = likes;
        mDislikeCount = dislikes;
        mFileName = fileName;
        mFileLink = fileLink;
    }

    /**
     * Copy constructor to create one datarow from another
     */
    DataRow(DataRow data) {
        mId = data.mId;
        // NB: Strings and Dates are immutable, so copy-by-reference is safe
        mTitle = data.mTitle;
        mUserId = data.mUserId;
        mContent = data.mContent;
        mCreated = data.mCreated;
        mLikeCount = 0;
        mDislikeCount = 0;
        mFileName = " ";
        mFileLink = " ";
    }
}