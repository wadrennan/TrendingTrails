package com.example.trendingtrails;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.ServiceTestRule;

import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.Map.MapActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class LocationTrackTest{

    private LocationTrack lt;
    @Rule
    public final ServiceTestRule mServiceRule = ServiceTestRule.withTimeout(60L, TimeUnit.SECONDS);
    public BaseActivity mapActivity;
    @Before
    public void setUp(){
        //activityRule.launchActivity(null);
            if(Looper.myLooper() == null){
                Looper.prepare();
            }
            lt = new LocationTrack(InstrumentationRegistry.getInstrumentation().getContext());

    }

    @Test
    public void canGetLocationTest(){
        System.out.println(lt.canGetLocation());
        assertNotEquals(lt.canGetLocation(), false);
    }



    @After
    public void tearDown(){
        lt = null;
       // activityRule.finishActivity();
        mServiceRule.unbindService();
    }
}
