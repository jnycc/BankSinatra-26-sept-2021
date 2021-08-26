package com.miw.database;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
