package com.example.trendingtrails;

import com.example.trendingtrails.Data.Database;
import com.example.trendingtrails.Models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DatabaseTests {
    Connection connection;

    @Before
    public void setUp() throws Exception {
        connection = Database.connect();
        connection.setAutoCommit(false);
    }

    @After
    public void closeDb() throws SQLException {
        connection.rollback();
    }

    @Test
    public void WriteAndReadDatabaseTest() throws Exception {
        String email = "johnsmith@test.com";
        User testUser = new User(email, "John Smith", 2);
        Database.insertUser(connection, testUser);
        User dbUser = Database.getUser(connection, email);
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

        User testUser = Database.getUser(connection, email);
        Database.updateUser(connection, newName, newRank, testUser);
        testUser = Database.getUser(connection, testUser.email);
        assertThat(testUser.displayName, equalTo(newName));
        assertThat(testUser.rank, equalTo(newRank));
    }
}
