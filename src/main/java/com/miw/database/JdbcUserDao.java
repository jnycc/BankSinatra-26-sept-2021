package com.miw.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcUserDao {
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    //public String getClientAdminByID()

    public String getRoleByEmail(String email) {
        String sql = "SELECT userRole FROM user WHERE email = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, email);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User does not exist in the database");
            return null;
        }
    }

    public String getRoleByID(int userID) {
        String sql = "SELECT userRole FROM user WHERE userID= ?;";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userID);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User does not exist in the database");
            return null;
        }
    }

    public int getIDByEmail(String email) {
        String sql = "SELECT userID FROM user WHERE email= ?;";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User does not exist in the database");
            return 0;
        }
    }

}





