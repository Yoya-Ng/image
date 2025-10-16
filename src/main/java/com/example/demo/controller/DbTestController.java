package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DbTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-test")
    public String testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            String url = conn.getMetaData().getURL();
            String databaseName = conn.getMetaData().getDatabaseProductName();
            String databaseVersion = conn.getMetaData().getDatabaseProductVersion();
            
            return String.format(
                "✅ Connected to Neon Database!\n" +
                "Database: %s\n" +
                "Version: %s\n" +
                "URL: %s",
                databaseName, databaseVersion, url
            );
        } catch (SQLException e) {
            return "❌ Connection failed: " + e.getMessage();
        }
    }
}
