package com.example.trendingtrails;

import com.example.trendingtrails.Models.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {
    public Location l1,l2;
    @Before
    public void setUp(){
        l1 = new Location("37027", 1,1,"hi");
        l2 = new Location(1,1,"hello");
    }
    @Test
    public void constructorOne(){
        assertNotNull(l1);
    }
    @Test
    public void constructorTwo(){
        assertNotNull(l2);
    }
    @After
    public void tearDown(){
        l1 = null;
        l2 = null;
    }
}
