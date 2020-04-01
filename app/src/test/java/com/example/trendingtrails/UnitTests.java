package com.example.trendingtrails;

import org.junit.Test;

import com.example.trendingtrails.Models.Input;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void checkZipCodeValidator(){
        String z1 = "12345";
        String z2 = "1234";
        String z3 = "123456";
        String z4 = "1234a";
        String z5 = "00499";
        String z6 = "99952";

        assertTrue(Input.isValidZipCode(z1));
        assertFalse(Input.isValidZipCode(z2));
        assertFalse(Input.isValidZipCode(z3));
        assertFalse(Input.isValidZipCode(z4));
        assertFalse(Input.isValidZipCode(z5));
        assertFalse(Input.isValidZipCode(z6));
    }
}