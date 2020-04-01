package com.example.trendingtrails;

import com.example.trendingtrails.Data.TrailStats;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class TrailStatsTest {
    public TrailStats ts;

    @Before
    public void setUp(){
        ts = new TrailStats();
    }

    @Test
    public void createTrailStatsTest(){
        assertNotNull(ts);
    }

    @Test
    public void getMaxDistanceTest(){
        //if there is a result, it will be greater than 0. 0 is default value
        assertNotEquals(ts.getMaxDistance(), 0);
    }

    @Test
    public void getNumberOfTrailsTest(){
        //if there is a result, it will be greater than 0. 0 is default value
        assertNotEquals(ts.getNumberOfTrails(), 0);
    }

    @After
    public void tearDown(){
        ts = null;
    }
}