package com.example.trendingtrails;

import com.example.trendingtrails.Data.MyMath;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyMathTests {
    List<Integer> values = new ArrayList<>(Arrays.asList(4, 9, 6, 10, 3, 4, 5, 6, 6, 9));

    @Test
    public void averageTest(){
        double avg = MyMath.average(values);
        assertEquals(avg, 6.2, .01);
    }

    @Test
    public void distanceTest(){
        double dist = MyMath.distance(5, 10, 2, 2);
        assertEquals(dist, 589.28, .1);
    }

    @Test
    public void degreesToRadiansTest(){
        double degree = 65;
        double radians = MyMath.deg2rad(degree);
        assertEquals(radians, 1.13, .01);
    }

    @Test
    public void radiansToDegreesTest(){
        double radians = 2.5;
        double degree = MyMath.rad2deg(radians);
        assertEquals(degree, 143.239, .01);
    }
}
