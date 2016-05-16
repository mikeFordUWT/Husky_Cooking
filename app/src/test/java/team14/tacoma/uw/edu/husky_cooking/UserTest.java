package team14.tacoma.uw.edu.husky_cooking;

import junit.framework.TestCase;

import org.junit.Test;


import team14.tacoma.uw.edu.husky_cooking.model.User;

/**
 * Created by Ian Skyles on 5/16/2016.
 */
public class UserTest extends TestCase {
    private User mUser;

    public void setUp() {
        mUser = new User(1001, "setup@setup.com");
    }


    @Test
    public void testConstructor() {
        User user = new User(1000,"UserTest@test.com");
        assertNotNull(user);
    }

    public void testParseUserJSON() {
        String userJSON = "[{\"id\":\"test\",\"email\":\"test@test.com\"," +
                "\"cookbook\":\"null\",\"status\":\"success\"}]";
        Boolean message =  User.parseUserJSON(userJSON);
        assertTrue("JSON With Valid String", message == true);
    }

    @Test
    public void testGetUserId() {
        assertEquals(1001, mUser.getId());
    }

    @Test
    public void testGetUserEmail() {
        assertEquals("setup@setup.com", mUser.getEmail());
    }

    @Test
    public void testGetCookBook() {
        assertEquals(0, mUser.getCookBook().size());
    }


}
