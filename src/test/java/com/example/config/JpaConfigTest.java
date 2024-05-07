package com.example.config;


import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JpaConfigTest {

    @Test
    public void testGetDataSourceNotNull() {
        JpaConfig jpaConfig = new JpaConfig();
        DataSource dataSource = jpaConfig.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testDataSourceType() {
        JpaConfig jpaConfig = new JpaConfig();
        DataSource dataSource = jpaConfig.getDataSource();
        assertEquals(HikariDataSource.class, dataSource.getClass());
    }

    @Test
    public void testDataSourceProperties() {
        JpaConfig jpaConfig = new JpaConfig();
        DataSource dataSource = jpaConfig.getDataSource();
        assertEquals("jdbc:mysql://localhost:3306/demo", ((HikariDataSource) dataSource).getJdbcUrl());
        assertEquals("root", ((HikariDataSource) dataSource).getUsername());
        assertEquals("rootroot", ((HikariDataSource) dataSource).getPassword());
    }
}
