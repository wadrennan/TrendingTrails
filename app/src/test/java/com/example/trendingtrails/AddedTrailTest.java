package com.example.trendingtrails;

import com.example.trendingtrails.Models.AddedTrail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddedTrailTest {
    public AddedTrail t1, t2;

    @Before
    public void setUp(){
        t1 = new AddedTrail(1,"test1",9.0, 10, 9);
        t2 = new AddedTrail(2,"test2",10.0, 1,8,"Very good");
    }

    @Test
    public void constructorOne(){
        assertNotNull(t1);
    }

    @Test
    public void constructorTwo(){
        assertNotNull(t2);
    }

    @Test
    public void testReview(){
        assertEquals("Very good", t2.review);
    }

    @After
    public void tearDown(){
        t1 = null;
        t2 = null;
    }
}
