package com.miw.database;

import com.miw.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.TreeMap;

@Repository
public class JdbcTokenDao {

    private final Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTokenDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcTokenDao");
    }

    private PreparedStatement insertTokenStatement(String email, String token, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "insert into Token (email, token) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, email);
        ps.setString(2, token);
        // TODO: toevoegen datum bij insertion token; moet date idd als String (zoals nu in deze klasse) of als DateTime?
        return ps;
    }

    public void saveToken(String email, String token) {
        jdbcTemplate.update(connection -> insertTokenStatement(email, token, connection));
    }

    public TreeMap<String, String> retrieveToken(String token) {
        String sql = "select * from Token where token = ?";
        List<TreeMap<String, String>> result = jdbcTemplate.query(sql, new TokenRowMapper(), token);
        return result.get(0);
    }

    public void deleteToken(String token) {
        String sql = "delete from Token where token = ?";
        jdbcTemplate.update(sql, token);
    }

    private static class TokenRowMapper implements RowMapper<TreeMap<String, String>> {

        @Override
        public TreeMap<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
            String token = resultSet.getString("token");
            String date = resultSet.getString("date");
            TreeMap<String, String> result = new TreeMap<>();
            result.put(token, date);
            return result;
        }
    }
}
