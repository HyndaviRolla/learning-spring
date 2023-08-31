package com.prodapt.learningspring.rank.controller;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/spring";
    private static final String USERNAME = "Database";
    private static final String PASSWORD = "atharva";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
