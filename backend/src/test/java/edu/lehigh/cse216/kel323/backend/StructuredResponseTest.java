package edu.lehigh.cse216.kel323.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StructuredResponseTest extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StructuredResponseTest(String testName) {
        super(testName);
    }

    

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StructuredResponseTest.class);
    }

    // test constructor for structured response
    public void testConstructor() {
        String status = "ok";
        String message = "test message";
        Object data = 1;
        StructuredResponse s = new StructuredResponse(status, message, data);
        assertTrue(s.mStatus.equals(status));
        assertTrue(s.mMessage.equals(message));
        assertTrue(s.mData.equals(data));

        assertFalse(s.mStatus == null || s.mStatus.equals("invalid") );
        assertFalse(s.mData == null);
    }

  

    
}
