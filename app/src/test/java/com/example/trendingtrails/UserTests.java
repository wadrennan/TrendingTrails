package com.example.trendingtrails;

import com.example.trendingtrails.Models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTests {
    public User user1, user2;
    @Before
    public void setUp(){
        //Constructor no parameters
        user1 = new User();

        //Constructor with parameters
        user2 = new User("test@trendingtrails.com", "jamal", 2);
    }

    @Test
    public void createTrail1Test(){
        assertNotNull(user1);
    }

    @Test
    public void createTrail2Test(){
        assertNotNull(user2);
    }

    @Test
    public void settingValuesTest(){
        assertEquals(user2.email, "test@trendingtrails.com");
        assertEquals(user2.displayName, "jamal");
        assertEquals(user2.rank, 2);
    }

    @After
    public void tearDown(){
        user1 = null;
        user2 = null;
    }
}
