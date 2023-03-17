package edu.lehigh.cse216.kel323.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DataComRowTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DataComRowTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DataComRowTest.class);
    }


    /**
	 * 
	 * 
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testConstructor() {
        String title = "Comment";
        int comId = 17;
		int id = 12;
		int userId = 13;
        DataComRow d = new DataComRow(comId, title, userId, id); //Must fix

        assertTrue(d.mComment.equals(title));
        assertTrue(d.mcomId == comId);
        assertTrue(d.mId == id);
		assertTrue(d.mUserId == userId);
    }


    /**
     * Ensure that the copy constructor works correctly
     */
    /*
    public void testCopyconstructor() {
        String title = "Comment";
        int comId = 11;
		int id = 19;
		int userId = 44;
        DataComRow d = new DataComRow(comId, title, userId, id); //Must fix
        DataComRow d2 = new DataComRow(d);
        assertTrue(d2.mTitle.equals(d.mTitle));
        assertTrue(d2.mcomId.equals(d.mcomId));
        assertTrue(d2.mId == d.mId);
        
    } */
}