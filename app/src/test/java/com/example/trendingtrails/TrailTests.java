package com.example.trendingtrails;

import com.example.trendingtrails.Models.Trail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrailTests {
    public Trail trail1, trail2;
    @Before
    public void setUp(){
        //Constructor no parameters
        trail1 = new Trail();

        //Constructor with parameters
        trail2 = new Trail("Test Trail", 9, 10, 1.75);
    }

    @Test
    public void createTrail1Test(){
        assertNotNull(trail1);
    }

    @Test
    public void createTrail2Test(){
        assertNotNull(trail2);
    }

    @Test
    public void settingValuesTest(){
        assertEquals(trail2.name, "Test Trail");
        assertEquals(trail2.intensity, 9);
        assertEquals(trail2.rating, 10);
        assertEquals(trail2.distance, 1.75,.10);
    }

    @After
    public void tearDown(){
        trail1 = null;
        trail2 = null;
    }
}
