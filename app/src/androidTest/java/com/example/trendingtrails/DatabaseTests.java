package com.example.trendingtrails;

import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Data.Queries;
import com.example.trendingtrails.Models.AccountInfo;
import com.example.trendingtrails.Models.Global;
import com.example.trendingtrails.Models.Location;
import com.example.trendingtrails.Models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class DatabaseTests {
    Connection connection;
    String email = "example@example.com";

    @Before
    public void setUp() throws Exception {
        Global.AccountInfo = new AccountInfo(email);
        connection = Database.connect();
        connection.setAutoCommit(false);
    }

    @After
    public void closeDb() throws SQLException {
        connection.rollback();
    }

    @Test
    public void WriteAndDeleteDatabaseTest() throws Exception{
        User testUser = new User(email, "John Smith", 2);
        Queries.insertUser(connection, testUser);
        String zip = "12345";
        double lat = 100.0;
        double lng = 100.0;
        String name = "Tuscaloosa, AL";
        Queries.insertLocation(connection, zip, lat, lng, name);
        Location l = Queries.getLocation(connection, zip);
        assertEquals(l.zipCode, zip);
        assertEquals(l.latitude, lat, .01);
        assertEquals(l.longitude, lng, .01);
        assertEquals(l.locationName, name);
        Queries.deleteZipCode(connection, zip);
        l = Queries.getLocation(connection, zip);
        assertNull(l);
    }

    @Test
    public void WriteAndReadDatabaseTest() throws Exception {
        String email = "johnsmith@test.com";
        User testUser = new User(email, "John Smith", 2);
        Queries.insertUser(connection, testUser);
        User dbUser = Queries.getUser(connection, email);
        assertNotNull(dbUser);
        assertThat(dbUser.displayName, equalTo(testUser.displayName));
        assertThat(dbUser.email, equalTo(testUser.email));
        assertThat(dbUser.rank, equalTo(testUser.rank));
    }

    @Test
    public void UpdateDatabaseTest() throws SQLException {
        String email = "test@test.com";
        String newName = "Jane Doe";
        int newRank = 1;

        User testUser = Queries.getUser(connection, email);
        Queries.updateUser(connection, newName, newRank, testUser);
        testUser = Queries.getUser(connection, testUser.email);
        assertThat(testUser.displayName, equalTo(newName));
        assertThat(testUser.rank, equalTo(newRank));
    }
}
