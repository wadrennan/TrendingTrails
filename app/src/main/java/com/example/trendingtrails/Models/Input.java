package com.example.trendingtrails.Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    public static boolean isValidZipCode(String code){
        String regex = "^[0-9]{5}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        if(matcher.matches() && code.compareTo("00501") >= 0 && code.compareTo("99950") <= 0)
            return true;
        return false;
    }
}
